package com.mango.mall.search.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import com.mango.common.to.es.SkuModel;
import com.mango.mall.search.constant.EsConstant;
import com.mango.mall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Mango
 * @Date 2022/3/19 16:26
 */
@Slf4j
@Service("ProductSaveService")
public class ProductSaveServiceImpl implements ProductSaveService {

    @Resource
    ElasticsearchClient client;


    @Override
    public Boolean productStatusUP(List<SkuModel> skuModelList) throws IOException {
        //es 建立索引和映射关系
        //保存数据
        List<BulkOperation> list = skuModelList.stream().map(item -> {
            IndexOperation<SkuModel> indexOperation = new IndexOperation.Builder<SkuModel>()
                    .document(item)
                    .build();
            BulkOperation operation = new BulkOperation.Builder()
                    .index(indexOperation)
                    .build();
            return operation;
        }).collect(Collectors.toList());
        BulkRequest request = new BulkRequest.Builder()
                .index("product")
                .operations(list)
                .build();
        BulkResponse bulk = client.bulk(request);
        List<String> collect = bulk.items().stream().map(item -> {
            return item.id();
        }).collect(Collectors.toList());
        boolean errors = bulk.errors();
        log.error("上架成功{}",collect);
        return  errors;
    }
}
