package com.mango.mall.ware.dao;

import com.mango.mall.ware.entity.PurchaseDetailEntity;
import com.mango.mall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mango.mall.ware.vo.SkuStockVo;
import com.mango.mall.ware.vo.WareSkuVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 *
 * @author Mango
 * @email mango@mango.com
 * @date 2022-03-03 21:22:55
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {


    void addStock(WareSkuVo wareSkuVo);

    List<SkuStockVo> getSkuStock(@Param("skuIds") List<Long> skuIds);
}
