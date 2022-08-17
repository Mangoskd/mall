package com.mango.mall.coupon.dao;

import com.mango.mall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 *
 * @author Mango
 * @email mango@mango.com
 * @date 2022-03-03 20:55:18
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {

}
