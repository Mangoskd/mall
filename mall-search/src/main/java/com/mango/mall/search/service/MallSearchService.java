package com.mango.mall.search.service;

import com.mango.mall.search.vo.SearchParm;
import com.mango.mall.search.vo.SearchResult;

/**
 * @Author Mango
 * @Date 2022/4/3 20:24
 */
public interface MallSearchService {

    /**
     * 检索服务
     * @param searchParm 检索参数
     * @return 返回结果
     */
    SearchResult search(SearchParm searchParm);
}
