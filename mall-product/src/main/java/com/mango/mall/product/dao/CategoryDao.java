package com.mango.mall.product.dao;

import com.mango.mall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 *
 * @author Mango
 * @email mango@mango.com
 * @date 2022-03-03 17:28:47
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {

}
