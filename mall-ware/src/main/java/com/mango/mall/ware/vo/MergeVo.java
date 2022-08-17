package com.mango.mall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author Mango
 * @Date 2022/3/14 22:40
 */
@Data
public class MergeVo {

  private Long  purchaseId;
   private List<Long> items;
}
