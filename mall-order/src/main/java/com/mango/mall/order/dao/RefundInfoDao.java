package com.mango.mall.order.dao;

import com.mango.mall.order.entity.RefundInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退款信息
 *
 * @author Mango
 * @email mango@mango.com
 * @date 2022-03-03 21:12:08
 */
@Mapper
public interface RefundInfoDao extends BaseMapper<RefundInfoEntity> {

}
