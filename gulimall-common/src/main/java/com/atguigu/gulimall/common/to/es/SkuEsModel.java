package com.atguigu.gulimall.common.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class SkuEsModel {

    private String skuId;

    private String spuId;

    private String skuTitle;

    private BigDecimal skuPrice;

    private String skuImg;

    private Long saleCount;

    private Boolean hasStock;

    private Long hotScore;

    private String brandId;

    private String catalogId;

    private String brandName;

    private String brandImg;

    private String catalogName;

    private List<Attrs> attrs;

    @Data
    public static class Attrs {

        private String attrId;

        private String attrName;

        private String attrValue;
    }


}
