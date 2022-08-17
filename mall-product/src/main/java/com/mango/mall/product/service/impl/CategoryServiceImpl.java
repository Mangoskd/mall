package com.mango.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mango.mall.product.service.CategoryBrandRelationService;
import com.mango.mall.product.vo.Catelog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mango.common.utils.PageUtils;
import com.mango.common.utils.Query;

import com.mango.mall.product.dao.CategoryDao;
import com.mango.mall.product.entity.CategoryEntity;
import com.mango.mall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {


    @Resource
    CategoryBrandRelationService categoryBrandRelationService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    RedissonClient redissonClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        List<CategoryEntity> collect = categoryEntities.stream()
                .filter(entity -> entity.getParentCid() == 0)
                .map((menu) -> {
                    menu.setChildren(getSub(menu, categoryEntities));
                    return menu;
                })
                .sorted(Comparator.comparingInt(m -> (m.getSort() == null ? 0 : m.getSort())))
                .collect(Collectors.toList());

        return collect;
    }

    @Override
    public void removeMenusByIds(List<Long> asList) {
        //TODO 检查删除菜单是否被引用
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        findParantPath(catelogId, paths);
        return (Long[]) paths.toArray(new Long[paths.size()]);

    }

//    @CacheEvict(value = "category",key = "'getLevelCategorys'")
    @Caching(
            evict = {
                    @CacheEvict(value = "category",key = "'getLevelCategorys'"),
                    @CacheEvict(value = "category",key = "'getCatalogJson'")
            }
    )
    @Transactional
    @Override
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);
        if (!StringUtils.isEmpty(category.getName())) {
            categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());

            //TODO 更新其他关联
        }
    }   

    @Cacheable(value = "category",key = "#root.methodName")
    @Override
    public List<CategoryEntity> getLevelCategorys() {
        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));

    }
    @Cacheable(value = "category",key = "#root.methodName")
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        List<CategoryEntity> selectList = baseMapper.selectList(null);
//        一级菜单对象集合
        List<CategoryEntity> levelCategorys = this.getLevelCategorys();
        Map<String, List<Catelog2Vo>> collect = levelCategorys
                .stream()
                .collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                    // 二级菜单对象集合
                    List<CategoryEntity> parent_cid = this.getParent_cid(selectList, v.getCatId());
                    List<Catelog2Vo> catelog2Vos = null;
                    if (parent_cid != null) {
                        catelog2Vos = parent_cid
                                .stream()
                                .map(l2 -> {
                                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                                    List<CategoryEntity> level3Catalog = this.getParent_cid(selectList, l2.getCatId());
                                    if (level3Catalog != null) {
                                        List<Catelog2Vo.Catelog3Vo> catelog3Vos = level3Catalog
                                                .stream()
                                                .map(l3 -> new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName())
                                                ).collect(Collectors.toList());
                                        catelog2Vo.setCatalog3List(catelog3Vos);
                                    }
                                    return catelog2Vo;
                                }).collect(Collectors.toList());
                    }
                    return catelog2Vos;
                }));
        return collect;

    }
    //TODO 产生堆外内存溢出
    public Map<String, List<Catelog2Vo>> getCatalogJson2() {
        //缓存中存的都是json字符串
        //json 跨语言跨平台的
        String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");
        if (StringUtils.isEmpty(catalogJson)) {
            Map<String, List<Catelog2Vo>> catalogJsonFromDB = this.getCatalogJsonFromDB();
            return catalogJsonFromDB;
        }

        return JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });
    }

    /**
     * 数据库查询并封装整个分类数据
     *
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDB()  {
        //使用分布式
        RLock lock = redissonClient.getLock("catalogJson_lock");
        lock.lock();
        Map<String, List<Catelog2Vo>> dataFromDB;
            try {
                dataFromDB = getDataFromDB();
            } finally {
                lock.unlock();
            }
        return dataFromDB;
    }

    private Map<String, List<Catelog2Vo>> getDataFromDB() {
        //所有菜单对象集合
        List<CategoryEntity> selectList = baseMapper.selectList(null);
//        一级菜单对象集合
        List<CategoryEntity> levelCategorys = this.getLevelCategorys();
        Map<String, List<Catelog2Vo>> collect = levelCategorys
                .stream()
                .collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                    // 二级菜单对象集合
                    List<CategoryEntity> parent_cid = this.getParent_cid(selectList, v.getCatId());
                    List<Catelog2Vo> catelog2Vos = null;
                    if (parent_cid != null) {
                        catelog2Vos = parent_cid
                                .stream()
                                .map(l2 -> {
                                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                                    List<CategoryEntity> level3Catalog = this.getParent_cid(selectList, l2.getCatId());
                                    if (level3Catalog != null) {
                                        List<Catelog2Vo.Catelog3Vo> catelog3Vos = level3Catalog
                                                .stream()
                                                .map(l3 -> new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName())
                                                ).collect(Collectors.toList());
                                        catelog2Vo.setCatalog3List(catelog3Vos);
                                    }
                                    return catelog2Vo;
                                }).collect(Collectors.toList());
                    }
                    return catelog2Vos;
                }));

        String jsonString = JSON.toJSONString(collect);
        stringRedisTemplate.opsForValue().set("catalogJson", jsonString);
        return collect;
    }


    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList, Long parent_cid) {
        return selectList
                .stream()
                .filter(item -> item.getParentCid().equals(parent_cid))
                .collect(Collectors.toList());
    }


    private void findParantPath(Long catelogId, List<Long> list) {
        Long parentCid = this.getById(catelogId).getParentCid();
        if (parentCid != 0) {
            findParantPath(parentCid, list);
        }
        list.add(catelogId);
    }

    /**
     * 通过Stream 获取子菜单数据并返回
     *
     * @param rootMenu 父类菜单
     * @param allList  所有菜单数据
     * @return
     */
    private List<CategoryEntity> getSub(CategoryEntity rootMenu, List<CategoryEntity> allList) {

        return allList.stream()
                .filter(menu -> Objects.equals(menu.getParentCid(), rootMenu.getCatId()))
                .map(menu -> {
                    menu.setChildren(getSub(menu, allList));
                    return menu;
                })
                .sorted(Comparator.comparingInt(m -> (m.getSort() == null ? 0 : m.getSort())))
                .collect(Collectors.toList());

    }

}