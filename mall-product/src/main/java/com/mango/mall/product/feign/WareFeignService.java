package com.mango.mall.product.feign;

import com.mango.common.to.SkuHasStockVo;
import com.mango.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author Mango
 * @Date 2022/3/19 15:46
 */
@FeignClient("mall-ware")
public interface WareFeignService {
    @PostMapping("/ware/waresku/hasStock")
    R getSkusHasStock(@RequestBody List<Long> skuIds);
}
