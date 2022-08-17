package com.mango.mall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author Mango
 * @Date 2022/3/12 17:13
 */
@Data
public class MemberPrice {
    private Long id;
    private String name;
    private BigDecimal price;
}
