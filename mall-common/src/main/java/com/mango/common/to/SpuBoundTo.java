package com.mango.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author Mango
 * @Date 2022/3/13 12:05
 */
@Data
public class SpuBoundTo {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
