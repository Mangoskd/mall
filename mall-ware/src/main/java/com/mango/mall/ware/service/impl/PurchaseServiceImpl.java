package com.mango.mall.ware.service.impl;

import com.mango.common.constant.WareConstant;
import com.mango.mall.ware.entity.PurchaseDetailEntity;
import com.mango.mall.ware.service.PurchaseDetailService;
import com.mango.mall.ware.service.WareSkuService;
import com.mango.mall.ware.vo.MergeVo;
import com.mango.mall.ware.vo.PurchaseDoneVo;
import com.mango.mall.ware.vo.PurchaseItemDoneVo;
import com.mango.mall.ware.vo.WareSkuVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mango.common.utils.PageUtils;
import com.mango.common.utils.Query;

import com.mango.mall.ware.dao.PurchaseDao;
import com.mango.mall.ware.entity.PurchaseEntity;
import com.mango.mall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {


    @Resource
    PurchaseDetailService purchaseDetailService;

    @Resource
    WareSkuService wareSkuService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().in("status", Arrays.asList(new String[]{"0","1"}))
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId==null){
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            purchaseId=purchaseEntity.getId();
        }
        //TODO 合并判断
        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(i -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(i);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }

    @Override
    public void received(List<Long> ids) {
        List<PurchaseEntity> collect = this.listByIds(ids).stream()
                .filter(item -> item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() || item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode())
                .map(item->{
                    item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
                    item.setUpdateTime(new Date());
                    return item;
                })
                .collect(Collectors.toList());
        this.updateBatchById(collect);
//        List<PurchaseEntity> purchaseEntities = ids.stream().map(id -> this.getById(id)).collect(Collectors.toList());
        List<Long> idList = collect.stream().map(entity -> entity.getId()).collect(Collectors.toList());
       List<PurchaseDetailEntity> purchaseDetailEntityList= purchaseDetailService.listDetialBatchByPurchaseId(idList);
        List<PurchaseDetailEntity> entities = purchaseDetailEntityList.stream()
                .map(item -> {
                    PurchaseDetailEntity entity = new PurchaseDetailEntity();
                    entity.setId(item.getId());
                    entity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                    return entity;
                }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(entities);

    }
    @Transactional
    @Override
    public void done(PurchaseDoneVo doneVo) {
        //改变采购单状态

        //改变采购项状态
        List<PurchaseItemDoneVo> items = doneVo.getItems();
        boolean flag=true;
        List<PurchaseDetailEntity> detailEntities = items.stream().map(i -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(i.getItemId());
            purchaseDetailEntity.setStatus(i.getStatus());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        List<Long> collect = detailEntities
                .stream()
                .filter(i -> i.getStatus() == WareConstant.PurchaseStatusEnum.FINISH.getCode()).map(i -> i.getId()).collect(Collectors.toList());
        if (detailEntities==null||items.size()!=collect.size()){
            flag=false;
        }
        purchaseDetailService.updateBatchById(detailEntities);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(doneVo.getId());
        purchaseEntity.setStatus(flag?WareConstant.PurchaseStatusEnum.FINISH.getCode() : WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
        //入库
        List<PurchaseDetailEntity> purchaseDetailEntityList = purchaseDetailService.listByIds(collect);
        purchaseDetailEntityList.forEach(entity->{
            WareSkuVo wareSkuVo = new WareSkuVo();
            BeanUtils.copyProperties(entity,wareSkuVo);
            wareSkuService.addStock(wareSkuVo);
        });
    }

}