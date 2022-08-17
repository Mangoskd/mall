package com.mango.mall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.mango.common.constant.ProductConstant;
import com.mango.common.to.SkuHasStockVo;
import com.mango.common.to.SkuReductionTo;
import com.mango.common.to.SpuBoundTo;
import com.mango.common.to.es.SkuModel;
import com.mango.common.utils.R;
import com.mango.mall.product.entity.*;
import com.mango.mall.product.feign.CouponFeignService;
import com.mango.mall.product.feign.SearchFeignService;
import com.mango.mall.product.feign.WareFeignService;
import com.mango.mall.product.service.*;
import com.mango.mall.product.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mango.common.utils.PageUtils;
import com.mango.common.utils.Query;

import com.mango.mall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Resource
    SpuInfoDescService spuInfoDescService;

    @Resource
    SpuImagesService spuImagesService;

    @Resource
    AttrService attrService;

    @Resource
    ProductAttrValueService productAttrValueService;

    @Resource
    SkuInfoService skuInfoService;

    @Resource
    SkuImagesService skuImagesService;

    @Resource
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Resource
    CouponFeignService couponFeignService;
    @Resource
    BrandService brandService;
    @Resource
    CategoryService categoryService;

    @Resource
    WareFeignService wareFeignService;

    @Resource
    SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {

        // 保存基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);
        //保存描述 pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",", decript));

        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);
        //保存图片 pms_spu_images
        List<String> images = vo.getImages();

        spuImagesService.saveImages(spuInfoEntity.getId(), images);
        //保存规格参数 pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> attrValueEntities = baseAttrs.stream().map(item -> {
            ProductAttrValueEntity attrValueEntity = new ProductAttrValueEntity();
            attrValueEntity.setAttrId(item.getAttrId());
            AttrEntity attrEntity = attrService.getById(item.getAttrId());
            attrValueEntity.setAttrName(attrEntity.getAttrName());
            attrValueEntity.setAttrValue(item.getAttrValues());
            attrValueEntity.setQuickShow(item.getShowDesc());
            attrValueEntity.setSpuId(spuInfoEntity.getId());
            return attrValueEntity;
        }).collect(Collectors.toList());

        productAttrValueService.saveProductAttr(attrValueEntities);
        //spu 积分信息 sms
        Bounds bounds = vo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds, spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if (r.getCode() != 0) {
            log.error("远程保存spu积分信息失败");
        }
        //保存当前spu 对应的sku 信息

        List<Skus> skus = vo.getSkus();
        if (!CollectionUtils.isEmpty(skus)) {
            skus.forEach(item -> {
                //1.sku 基本信息 pms_sku_info
                String defaultImage = "";
                for (Images image : item.getImages()) {
                    if (image.getDefaultImg() == 1) {
                        defaultImage = image.getImgUrl();
                    }
                }
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item, skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImage);
                skuInfoService.saveSkuInfo(skuInfoEntity);
                Long skuId = skuInfoEntity.getSkuId();
                List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter(entity -> !StringUtils.isEmpty(entity.getImgUrl())).collect(Collectors.toList());
                //2. sku 图片信息 pms_sku_images
                //TODO 没有图片路径的无需保存
                skuImagesService.saveBatch(imagesEntities);
                // 3.sku 销售属性 pms_sku_sale_attr_value
                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(a -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);
                // 4.优惠信息满减信息 sms 数据库
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item, skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1) {
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if (r1.getCode() != 0) {
                        log.error("远程保存sku优惠信息失败");
                    }
                }


            });
        }


        //sku 信息


    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            queryWrapper.and(wapper->{
               wapper.eq("id",key).or().like("spu_name",key);
            });
        }
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)){
            queryWrapper.eq("publish_status",status);

        }
        String brandId = (String) params.get("brandId");
        if (!(StringUtils.isEmpty(brandId)||"0".equalsIgnoreCase(brandId))){
            queryWrapper.eq("brand_id",brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (!(StringUtils.isEmpty(catelogId)||("0").equalsIgnoreCase(catelogId))){
            queryWrapper.eq("catalog_id",catelogId);
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {
        List<SkuInfoEntity> skuInfoEntities=skuInfoService.getSkusBySpuId(spuId);
        //TODO 查询所有可以被检索的规格属性
        List<ProductAttrValueEntity> productAttrValueEntityList = productAttrValueService.baseAttrList(spuId);
        List<Long> attrIds = productAttrValueEntityList.stream().map(item -> item.getAttrId()).collect(Collectors.toList());
        List<Long> searchAttrIds = attrService.selectSearchAttrs(attrIds);
        HashSet<Long> idSet = new HashSet<>(searchAttrIds);
        List<SkuModel.Attrs> collect = productAttrValueEntityList
                .stream()
                .filter(entity -> idSet.contains(entity.getAttrId()))
                .map(item -> {
                    SkuModel.Attrs attrs = new SkuModel.Attrs();
                    BeanUtils.copyProperties(item, attrs);
                    return attrs;
                })
                .collect(Collectors.toList());
        //TODO 发送远程调用,查询库存
        //得到skuId的集合
        List<Long> skuIds = skuInfoEntities
                .stream()
                .map(SkuInfoEntity::getSkuId)
                .collect(Collectors.toList());
        Map<Long, Boolean> map = null;
        try {
            R stock = wareFeignService.getSkusHasStock(skuIds);
            if(stock!=null) {
                map = stock
                        .getData(new TypeReference<List<SkuHasStockVo>>(){})
                        .stream()
                        .collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
            }
        } catch (Exception e) {
            log.error("库存服务异常:{}",e);
        }
            Map<Long, Boolean> finalMap = map;
            // 将sku数据处理成发送给 elasticSearch 的集合
            List<SkuModel> list = skuInfoEntities.stream().map(item -> {
                SkuModel skuModel = new SkuModel();
                BeanUtils.copyProperties(item, skuModel);
                skuModel.setSkuPrice(item.getPrice());
                skuModel.setSkuImg(item.getSkuDefaultImg());
                //设置库存信息
                if (finalMap == null) {
                    skuModel.setHasStock(true);
                } else {
                    skuModel.setHasStock(finalMap.get(item.getSkuId()));
                }

                //TODO 热度评分
                skuModel.setHotScore(0L);
                //TODO 查询分类和品牌名字
                BrandEntity brandEntity = brandService.getById(skuModel.getBrandId());
                //设置品牌名字和品牌图片
                skuModel.setBrandName(brandEntity.getName());
                skuModel.setBrandImg(brandEntity.getLogo());
                //设置分类名称
                CategoryEntity categoryEntity = categoryService.getById(skuModel.getCatalogId());
                skuModel.setCatalogName(categoryEntity.getName());
                //设置检索属性
                skuModel.setAttrs(collect);

                return skuModel;
            }).collect(Collectors.toList());
            //TODO 数据发给es保存 mall-search
            R r = searchFeignService.productStatusUP(list);
            if (r.getCode() == 0) {
                //TODO 更改商品上架状态
                baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
            } else {
                //远程调用失败
                //TODO  重复调用
            }
        }
}