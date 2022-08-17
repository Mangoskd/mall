package com.mango.mall.product.dao;

import com.mango.common.utils.R;
import com.mango.mall.product.entity.BrandEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 品牌
 *
 * @author Mango
 * @email mango@mango.com
 * @date 2022-03-03 17:28:47
 */
@Mapper
public interface BrandDao extends BaseMapper<BrandEntity> {


    void updateStatus(BrandEntity brand);

}
