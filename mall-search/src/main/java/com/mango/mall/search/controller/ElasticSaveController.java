package com.mango.mall.search.controller;

import com.mango.common.exception.BizCodeEnum;
import com.mango.common.to.es.SkuModel;
import com.mango.common.utils.R;
import com.mango.mall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author Mango
 * @Date 2022/3/19 16:18
 */
@Slf4j
@RequestMapping("/search/save")
@RestController
public class ElasticSaveController {

    @Resource
    ProductSaveService productSaveService;
    @PostMapping("/product")
    public R productStatusUP(@RequestBody List<SkuModel> skuModelList){
        Boolean res= false;
        try{
            res = productSaveService.productStatusUP(skuModelList);
        }catch (Exception e){
            log.error("ElasticSaveController商品上架错误{}",e);
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION. getCode(),BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }

        return res ?R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION. getCode(),BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg()):R.ok();
    }
}
