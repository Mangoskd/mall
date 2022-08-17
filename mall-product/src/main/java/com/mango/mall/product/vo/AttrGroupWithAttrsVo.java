package com.mango.mall.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.mango.mall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @Author Mango
 * @Date 2022/3/11 21:40
 */
@Data
public class AttrGroupWithAttrsVo {
    @TableId
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    private List<AttrEntity> attrs;
}
