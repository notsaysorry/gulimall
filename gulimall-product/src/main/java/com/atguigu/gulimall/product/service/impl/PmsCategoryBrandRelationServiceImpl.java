package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.dao.PmsBrandDao;
import com.atguigu.gulimall.product.entity.PmsBrandEntity;
import com.atguigu.gulimall.product.entity.PmsCategoryEntity;
import com.atguigu.gulimall.product.service.PmsBrandService;
import com.atguigu.gulimall.product.service.PmsCategoryService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.PmsCategoryBrandRelationDao;
import com.atguigu.gulimall.product.entity.PmsCategoryBrandRelationEntity;
import com.atguigu.gulimall.product.service.PmsCategoryBrandRelationService;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsCategoryBrandRelationService")
public class PmsCategoryBrandRelationServiceImpl extends ServiceImpl<PmsCategoryBrandRelationDao, PmsCategoryBrandRelationEntity> implements PmsCategoryBrandRelationService {

    @Autowired
    private PmsCategoryService pmsCategoryService;
    @Autowired
    private PmsBrandService pmsBrandService;
    @Autowired
    private PmsCategoryBrandRelationDao pmsCategoryBrandRelationDao;
    @Autowired
    private PmsBrandDao pmsBrandDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsCategoryBrandRelationEntity> page = this.page(
                new Query<PmsCategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<PmsCategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveDetail(PmsCategoryBrandRelationEntity pmsCategoryBrandRelation) {
        String brandId = pmsCategoryBrandRelation.getBrandId();
        String catelogId = pmsCategoryBrandRelation.getCatelogId();
        PmsBrandEntity brand = pmsBrandService.getOne(new QueryWrapper<PmsBrandEntity>().eq("brand_id", brandId));
        PmsCategoryEntity category = pmsCategoryService.getOne(new QueryWrapper<PmsCategoryEntity>().eq("cat_id", catelogId));
        pmsCategoryBrandRelation.setBrandName(brand.getName());
        pmsCategoryBrandRelation.setCatelogName(category.getName());
        baseMapper.insert(pmsCategoryBrandRelation);

    }

    @Override
    public void updateBrand(String brandId, String name) {
        PmsCategoryBrandRelationEntity pmsCategoryBrandRelationEntity = new PmsCategoryBrandRelationEntity();
        pmsCategoryBrandRelationEntity.setBrandId(brandId);
        pmsCategoryBrandRelationEntity.setBrandName(name);
        baseMapper.update(pmsCategoryBrandRelationEntity,
                new UpdateWrapper<PmsCategoryBrandRelationEntity>().eq("brand_id", brandId));
    }

    @Override
    public void updateCategory(String categoryId, String name) {
        PmsCategoryBrandRelationEntity pmsCategoryBrandRelationEntity = new PmsCategoryBrandRelationEntity();
        pmsCategoryBrandRelationEntity.setCatelogId(categoryId);
        pmsCategoryBrandRelationEntity.setCatelogName(name);
        baseMapper.update(pmsCategoryBrandRelationEntity,
                new UpdateWrapper<PmsCategoryBrandRelationEntity>().eq("catelog_id", categoryId));


    }

    @Override
    public List<PmsBrandEntity> queryBrands(String catId) {
        List<PmsCategoryBrandRelationEntity> brandRelationEntities = pmsCategoryBrandRelationDao.selectList(new QueryWrapper<PmsCategoryBrandRelationEntity>().eq("catelog_id", catId));
        List<PmsBrandEntity> brandEntities = brandRelationEntities.stream().map(item -> {
            String brandId = item.getBrandId();
            PmsBrandEntity brand = pmsBrandDao.selectOne(new QueryWrapper<PmsBrandEntity>().eq("brand_id", brandId));
            return brand;
        }).collect(Collectors.toList());
        return brandEntities;
    }

}