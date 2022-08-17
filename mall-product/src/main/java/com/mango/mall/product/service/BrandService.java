package com.mango.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mango.common.utils.PageUtils;

import com.mango.mall.product.entity.BrandEntity;



import java.util.Map;

/**
 * 品牌
 *
 * @author Mango
 * @email mango@mango.com
 * @date 2022-03-03 17:28:47
 */

public interface BrandService extends IService<BrandEntity> {



    PageUtils queryPage(Map<String, Object> params);

     void updateStatus(BrandEntity brand);

    void updateDetail(BrandEntity brand);
}

