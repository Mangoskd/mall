package com.mango.mall.ware.vo;

import lombok.Data;

/**
 * @Author Mango
 * @Date 2022/3/15 21:12
 */
@Data
public class WareSkuVo {
    private Long skuId;
    /**
     * 仓库id
     */
    private Long wareId;
    /**
     * sku_name
     */
    private Integer skuNum;
}
