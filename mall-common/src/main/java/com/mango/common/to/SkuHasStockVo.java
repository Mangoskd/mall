package com.mango.common.to;

import lombok.Data;

/**
 * @Author Mango
 * @Date 2022/3/19 15:01
 */
@Data
public class SkuHasStockVo {
    private Long skuId;
    private Boolean hasStock;
}
