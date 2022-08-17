package com.mango.mall.ware.vo;

import com.sun.el.lang.ELArithmetic;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author Mango
 * @Date 2022/3/15 20:11
 */
@Data
public class PurchaseDoneVo {
    @NotNull
    private Long id;

    private List<PurchaseItemDoneVo> items;
}
