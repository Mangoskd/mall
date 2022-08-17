package com.mango.mall.search.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.ScoreMode;
import co.elastic.clients.util.ObjectBuilder;
import com.mango.mall.search.config.ElasticSearchConfig;
import com.mango.mall.search.service.MallSearchService;
import com.mango.mall.search.vo.SearchParm;
import com.mango.mall.search.vo.SearchResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Mango
 * @Date 2022/4/3 20:24
 */
@Service
public class MallSearchServiceImpl implements MallSearchService {

    @Resource
    ElasticsearchClient elasticsearchClient;

    @Override
    public SearchResult search(SearchParm searchParm) {
        SearchResult searchResult = null;
        SearchRequest searchRequest = bulidSearchRequest(searchParm);
        /**
         * 执行检索请求
         */
        try {
            SearchResponse<SearchResult>  response = elasticsearchClient.search(searchRequest, SearchResult.class);
            // 分析响应数据

        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    private SearchRequest bulidSearchRequest(SearchParm searchParm) {
        // 构建bool-query
        BoolQuery.Builder boolQueryBuilder = QueryBuilders.bool();
        //1.1 bool-must 模糊匹配
        if (!StringUtils.isEmpty(searchParm.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.match().field("skuTitle").query(searchParm.getKeyword()).build()._toQuery());
        }
        //三级分类id
        if (null != searchParm.getCatalog3Id()) {
            boolQueryBuilder.filter(QueryBuilders.term().field("catalogId").value(searchParm.getCatalog3Id()).build()._toQuery());
        }
        //品牌id
        if (null != searchParm.getBrandId() && searchParm.getBrandId().size() > 0) {
            ObjectBuilder<TermsQueryField> termsQueryFieldObjectBuilder = getTermsQueryFieldObjectBuilder(searchParm.getBrandId());
            boolQueryBuilder.filter(QueryBuilders.terms().field("brandId").terms(termsQueryFieldObjectBuilder.build()).build()._toQuery());
        }
        //指定属性
        if (searchParm.getAttrs() != null && searchParm.getAttrs().size() > 0) {
            searchParm.getAttrs().forEach(item -> {
                //attrs=1_5寸:8寸&2_16G:8G
                BoolQuery.Builder boolQuery = QueryBuilders.bool();
                //attrs=1_5寸:8寸
                String[] s = item.split("_");
                String attrId = s[0]; // 检索的属性id
                String[] attrValues = s[1].split(":");//这个属性检索用的值
                boolQuery.must(QueryBuilders.term().field("attrs.attrId").value(attrId).build()._toQuery());
                boolQuery.must(QueryBuilders.terms().field("attrs.attrValue").terms(t -> t
                        .value(Arrays.stream(attrValues)
                                .map(attrValue -> new FieldValue.Builder().stringValue(attrValue).build())
                                .collect(Collectors.toList()))).build()._toQuery());
                // 每一个属性都要生成一个 nested 查询
//                QueryBuilders.nested("attrs", boolQuery, ScoreMode.None);
//                boolQueryBuilder.filter(nestedQueryBuilder);
            });
        }
        //


        return new SearchRequest.Builder()
                .build();
    }

    private ObjectBuilder<TermsQueryField> getTermsQueryFieldObjectBuilder(List<Long> list) {
        ObjectBuilder<TermsQueryField> termsQueryFieldObjectBuilder = new TermsQueryField.Builder()
                .value(list
                        .stream()
                        .map(item -> new FieldValue.Builder().longValue(item).build())
                        .collect(Collectors.toList()));
        return termsQueryFieldObjectBuilder;
    }
}
