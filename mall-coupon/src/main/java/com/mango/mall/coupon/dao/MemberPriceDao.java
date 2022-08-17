package com.mango.mall.coupon.dao;

import com.mango.mall.coupon.entity.MemberPriceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品会员价格
 *
 * @author Mango
 * @email mango@mango.com
 * @date 2022-03-03 20:55:18
 */
@Mapper
public interface MemberPriceDao extends BaseMapper<MemberPriceEntity> {

}
