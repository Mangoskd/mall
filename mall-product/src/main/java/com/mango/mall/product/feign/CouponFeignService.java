package com.mango.mall.product.feign;

import com.mango.common.to.SkuReductionTo;
import com.mango.common.to.SpuBoundTo;
import com.mango.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author Mango
 * @Date 2022/3/13 11:56
 */
@FeignClient("mall-coupon")
public interface CouponFeignService {

    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skufullreduction/save/info")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
