package com.mango.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mango.common.utils.PageUtils;
import com.mango.mall.order.entity.OrderItemEntity;

import java.util.Map;

/**
 * 订单项信息
 *
 * @author Mango
 * @email mango@mango.com
 * @date 2022-03-03 21:12:08
 */
public interface OrderItemService extends IService<OrderItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

