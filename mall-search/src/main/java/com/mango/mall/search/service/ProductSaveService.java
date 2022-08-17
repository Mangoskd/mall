package com.mango.mall.search.service;


import com.mango.common.to.es.SkuModel;

import java.io.IOException;
import java.util.List;

/**
 * @Author Mango
 * @Date 2022/3/19 16:23
 */

public interface ProductSaveService {

    /**
     *  将数据保存到 es 中
     * @param skuModelList
     */
    Boolean productStatusUP(List<SkuModel> skuModelList) throws IOException;
}
