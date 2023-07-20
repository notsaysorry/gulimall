package com.atguigu.gulimall.search.vo;

import com.atguigu.gulimall.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.List;


@Data
public class SearchResult {

    /**
     * 查询到的所有商品信息
     */
    private List<SkuEsModel> product;


    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页码
     */
    private Integer totalPages;


    /**
     * 当前查询到的结果，所有涉及到的品牌
     */
    private List<BrandVo> brands;

    /**
     * 当前查询到的结果，所有涉及到的所有属性
     */
    private List<AttrVo> attrs;

    /**
     * 当前查询到的结果，所有涉及到的所有分类
     */
    private List<CatalogVo> catalogs;


    //===========================以上是返回给页面的所有信息============================//




    @Data
    public static class BrandVo {

        private String brandId;

        private String brandName;

        private String brandImg;
    }


    @Data
    public static class AttrVo {

        private String attrId;

        private String attrName;

        private List<String> attrValue;
    }


    @Data
    public static class CatalogVo {

        private String catalogId;

        private String catalogName;
    }
}
