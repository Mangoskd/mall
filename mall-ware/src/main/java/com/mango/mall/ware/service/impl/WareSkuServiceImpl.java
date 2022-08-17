package com.mango.mall.ware.service.impl;

import com.mango.common.utils.R;
import com.mango.mall.ware.entity.PurchaseDetailEntity;
import com.mango.mall.ware.feign.ProductFeignService;
import com.mango.mall.ware.vo.SkuHasStockVo;
import com.mango.mall.ware.vo.SkuStockVo;
import com.mango.mall.ware.vo.WareSkuVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mango.common.utils.PageUtils;
import com.mango.common.utils.Query;

import com.mango.mall.ware.dao.WareSkuDao;
import com.mango.mall.ware.entity.WareSkuEntity;
import com.mango.mall.ware.service.WareSkuService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Resource
    WareSkuDao wareSkuDao;

    @Resource
    ProductFeignService feignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if (!StringUtils.isEmpty(skuId)) {
            queryWrapper.eq("sku_id", skuId);
        }
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)) {
            queryWrapper.eq("ware_id",wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(WareSkuVo wareSkuVo) {
        Long count = wareSkuDao.selectCount(new QueryWrapper<WareSkuEntity>().eq("sku_id", wareSkuVo.getSkuId()).eq("ware_id", wareSkuVo.getWareId()));
        if (count!=0) {
            wareSkuDao.addStock(wareSkuVo);
        }else{
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(wareSkuVo.getSkuId());
            wareSkuEntity.setStock(wareSkuVo.getSkuNum());
            wareSkuEntity.setWareId(wareSkuVo.getWareId());
            wareSkuEntity.setStockLocked(0);
            //TODO 查询sku 名字
            //TODO 还有其他方法
            try {
                R info = feignService.info(wareSkuVo.getSkuId());
                Map skuInfo = (Map) info.get("skuInfo");
                if (info.getCode()==0){
                    wareSkuEntity.setSkuName((String) skuInfo.get("skuName"));
                }
            } catch (Exception e) {
            }
            wareSkuDao.insert(wareSkuEntity);
        }
    }

    @Override
    public List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds) {
      List<SkuStockVo> wareSkuEntities= baseMapper.getSkuStock(skuIds);
        return wareSkuEntities.stream().map(item -> {
            SkuHasStockVo skuHasStockVo = new SkuHasStockVo();
            skuHasStockVo.setSkuId(item.getSkuId());
            skuHasStockVo.setHasStock(item.getStock() > 0);
            return skuHasStockVo;
        }).collect(Collectors.toList());

    }


}