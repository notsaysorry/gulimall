package com.atguigu.gulimall.product.vo;


import com.atguigu.gulimall.product.entity.PmsSkuImagesEntity;
import com.atguigu.gulimall.product.entity.PmsSkuInfoEntity;
import com.atguigu.gulimall.product.entity.PmsSpuInfoDescEntity;
import lombok.Data;

import java.util.List;



@Data
public class SkuItemVo {

    //1、sku基本信息的获取  pms_sku_info
    private PmsSkuInfoEntity info;

    private boolean hasStock = true;

    //2、sku的图片信息    pms_sku_images
    private List<PmsSkuImagesEntity> images;

    //3、获取spu的销售属性组合
    private List<SkuItemSaleAttrVo> saleAttr;

    //4、获取spu的介绍
    private PmsSpuInfoDescEntity desc;

    //5、获取spu的规格参数信息
    private List<SpuItemAttrGroupVo> groupAttrs;


}