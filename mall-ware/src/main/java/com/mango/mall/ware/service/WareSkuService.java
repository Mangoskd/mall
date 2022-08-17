package com.mango.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mango.common.utils.PageUtils;
import com.mango.mall.ware.entity.PurchaseDetailEntity;
import com.mango.mall.ware.entity.WareSkuEntity;
import com.mango.mall.ware.vo.SkuHasStockVo;
import com.mango.mall.ware.vo.WareSkuVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author Mango
 * @email mango@mango.com
 * @date 2022-03-03 21:22:55
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);


    void addStock(WareSkuVo wareSkuVo);

    List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds);
}

