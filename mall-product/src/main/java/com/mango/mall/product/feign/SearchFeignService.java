package com.mango.mall.product.feign;

import com.mango.common.to.es.SkuModel;
import com.mango.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author Mango
 * @Date 2022/3/20 19:47
 */
@FeignClient("mall-search")
public interface SearchFeignService {
    @PostMapping("/search/save/product")
     R productStatusUP(@RequestBody List<SkuModel> skuModelList);
}
