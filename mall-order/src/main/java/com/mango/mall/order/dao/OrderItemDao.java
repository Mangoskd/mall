package com.mango.mall.order.dao;

import com.mango.mall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 *
 * @author Mango
 * @email mango@mango.com
 * @date 2022-03-03 21:12:08
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {

}
