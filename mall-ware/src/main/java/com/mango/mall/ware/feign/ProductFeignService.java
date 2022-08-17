package com.mango.mall.ware.feign;

import com.mango.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author Mango
 * @Date 2022/3/15 22:49
 */
@FeignClient("mall-gateway")
public interface ProductFeignService {

    @RequestMapping("/api/product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);
}
