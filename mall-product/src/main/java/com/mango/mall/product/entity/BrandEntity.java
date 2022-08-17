package com.mango.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.mango.common.valid.AddGroup;
import com.mango.common.valid.ListValue;
import com.mango.common.valid.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 *
 * @author Mango
 * @email mango@mango.com
 * @date 2022-03-03 17:28:47
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */



    @NotNull(message = "修改的时候ID不能为空",groups = {UpdateGroup.class})
    @Null(message = "新增的时候不能有ID",groups = {AddGroup.class})
    @TableId
    private Long brandId;
    /**
     * 品牌名
     */
    @NotBlank(message = "品牌名必须提交",groups = {UpdateGroup.class,AddGroup.class})
    private String name;
    /**
     * 品牌logo地址
     */
    @NotEmpty
    @URL(message = "必须是合法的url地址",groups = {AddGroup.class})
    private String logo;
    /**
     * 介绍
     */
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     */
    @NotNull
    @ListValue(values = {1,0},groups = {AddGroup.class})
    private Integer showStatus;
    /**
     * 检索首字母
     */
    @NotEmpty
    @Pattern(regexp="^[a-zA-Z]$",message = "必须是一个字母",groups = {UpdateGroup.class,AddGroup.class})
    private String firstLetter;
    /**
     * 排序
     */
    @NotNull(message = "不能为空",groups = {UpdateGroup.class,AddGroup.class})
    @Min(value = 0,message = "必须大于0",groups = {UpdateGroup.class,AddGroup.class})
    @Max(value = 1000,message = "必须小于1000",groups = {UpdateGroup.class,AddGroup.class})
    private Integer sort;

}
