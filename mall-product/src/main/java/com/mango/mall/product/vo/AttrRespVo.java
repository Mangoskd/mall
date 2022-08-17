package com.mango.mall.product.vo;

import lombok.Data;

/**
 * @Author Mango
 * @Date 2022/3/9 20:20
 */
@Data
public class AttrRespVo extends AttrVo{
    /**
     * 			"catelogName": "手机/数码/手机", //所属分类名字
     * 			"groupName": "主体", //所属分组名字
     */
    private String  catelogName;
    private String  groupName;
    private Long[] catelogPath;
}
