package com.mango.mall.search.vo;

import com.mango.common.to.es.SkuModel;
import lombok.Data;

import java.util.List;

/**
 * @Author Mango
 * @Date 2022/4/3 20:42
 */
@Data
public class SearchResult {
    private List<SkuModel> products;
    private Integer pageNum;
    private Long total;
    private Integer totalPages;
    /**
     * 当前查询到的结果
     */
    private List<BrandVo> brands;
    private List<AttrVo> attrs;
    private List<CatalogVo> catalogs;
    @Data
    public static class BrandVo{
        private Long brandId;
        private String brandName;
        private String brandImg;
    }
    @Data
    public static class AttrVo{
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }
    @Data
    public static class CatalogVo{
        private Long catalogId;
        private String catalogName;

    }
}
