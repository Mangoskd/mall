package com.mango.mall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author Mango
 * @Date 2022/3/26 18:13
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Catelog2Vo {
    private String catalogId;
    private List<Catelog3Vo> catalog3List;
    private  String id;
    private String name;


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Catelog3Vo{
        private String catalog2Id;
        private  String id;
        private String name;
    }

}
