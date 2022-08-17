package com.mango.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mango.common.utils.PageUtils;
import com.mango.mall.ware.entity.PurchaseEntity;
import com.mango.mall.ware.vo.MergeVo;
import com.mango.mall.ware.vo.PurchaseDoneVo;
import com.mango.mall.ware.vo.PurchaseItemDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author Mango
 * @email mango@mango.com
 * @date 2022-03-14 22:11:23
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceive(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);

    void received(List<Long> ids);


    void done(PurchaseDoneVo doneVo);
}

