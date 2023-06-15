package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.service.PmsCategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.PmsBrandDao;
import com.atguigu.gulimall.product.entity.PmsBrandEntity;
import com.atguigu.gulimall.product.service.PmsBrandService;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsBrandService")
public class PmsBrandServiceImpl extends ServiceImpl<PmsBrandDao, PmsBrandEntity> implements PmsBrandService {

    @Autowired
    private PmsCategoryBrandRelationService pmsCategoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsBrandEntity> page = this.page(
                new Query<PmsBrandEntity>().getPage(params),
                new QueryWrapper<PmsBrandEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void updateDetail(PmsBrandEntity pmsBrand) {
        this.updateById(pmsBrand);
        if (StringUtils.isNotBlank(pmsBrand.getName())){
            pmsCategoryBrandRelationService.updateBrand(pmsBrand.getBrandId(), pmsBrand.getName());
        }
    }

}