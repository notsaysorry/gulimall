package com.atguigu.gulimall.product.dao;

import com.atguigu.gulimall.product.entity.PmsSkuSaleAttrValueEntity;
import com.atguigu.gulimall.product.vo.SkuItemSaleAttrVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 20:12:53
 */
@Mapper
public interface PmsSkuSaleAttrValueDao extends BaseMapper<PmsSkuSaleAttrValueEntity> {

    List<SkuItemSaleAttrVo> getSaleAttrsBySpuId(String spuId);

    List<String> getSkuSaleAttrValuesAsStringList(@Param("skuId") String skuId);
}
