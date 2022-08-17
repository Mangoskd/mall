package com.mango.mall.ware.vo;

import lombok.Data;

/**
 * @Author Mango
 * @Date 2022/3/15 20:13
 */
@Data
public class PurchaseItemDoneVo {
    private Long itemId;
    private Integer status;
    private String reason;
}
