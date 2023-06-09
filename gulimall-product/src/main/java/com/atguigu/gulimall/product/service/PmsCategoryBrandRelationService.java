package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.entity.PmsBrandEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.PmsCategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 20:12:53
 */
public interface PmsCategoryBrandRelationService extends IService<PmsCategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetail(PmsCategoryBrandRelationEntity pmsCategoryBrandRelation);

    void updateBrand(String brandId, String name);

    void updateCategory(String categoryId, String name);

    List<PmsBrandEntity> queryBrands(String catId);
}

