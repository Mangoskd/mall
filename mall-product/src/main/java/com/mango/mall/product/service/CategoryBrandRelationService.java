package com.mango.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mango.common.utils.PageUtils;
import com.mango.mall.product.entity.BrandEntity;
import com.mango.mall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author Mango
 * @email mango@mango.com
 * @date 2022-03-03 17:28:47
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    void updateBrand(Long brandId, String name);

    void updateCategory(Long catId, String name);

    List<BrandEntity> getBrandsByCatId(Long catId);
}

