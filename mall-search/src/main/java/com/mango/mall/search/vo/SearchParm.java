package com.mango.mall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author Mango
 * @Date 2022/4/3 20:23
 */
@Data
public class SearchParm {
    private String keyword;
    private Long catalog3Id;
    /**
     * 排序条件 saleCount_asc/hotScore
     */
    private String sort;
    private Integer hasStock;
    private String skuPrice;
    private List<Long> brandId;
    private List<String> attrs;
    private Integer pageNum;
}
