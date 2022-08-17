package com.mango.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mango.common.utils.PageUtils;
import com.mango.mall.product.entity.AttrEntity;
import com.mango.mall.product.vo.AttrGroupRelationVo;
import com.mango.mall.product.vo.AttrRespVo;
import com.mango.mall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author Mango
 * @email mango@mango.com
 * @date 2022-03-03 17:28:46
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelationVo[] vos);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    /**
     *  在指定所有属性集合里面筛选出检索属性
     * @param attrIds
     * @return
     */
    List<Long> selectSearchAttrs(List<Long> attrIds);
}

