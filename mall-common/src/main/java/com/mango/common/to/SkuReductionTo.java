package com.mango.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author Mango
 * @Date 2022/3/13 12:19
 */
@Data
public class SkuReductionTo {
    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
