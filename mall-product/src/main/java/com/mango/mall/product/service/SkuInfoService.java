package com.mango.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mango.common.utils.PageUtils;
import com.mango.mall.product.entity.SkuInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * sku信息
 *
 * @author Mango
 * @email mango@mango.com
 * @date 2022-03-03 17:28:47
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuInfo(SkuInfoEntity skuInfoEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);

    List<SkuInfoEntity> getSkusBySpuId(Long spuId);
}

