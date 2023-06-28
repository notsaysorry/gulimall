package com.atguigu.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.PmsSpuImagesDao;
import com.atguigu.gulimall.product.entity.PmsSpuImagesEntity;
import com.atguigu.gulimall.product.service.PmsSpuImagesService;


@Service("pmsSpuImagesService")
public class PmsSpuImagesServiceImpl extends ServiceImpl<PmsSpuImagesDao, PmsSpuImagesEntity> implements PmsSpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsSpuImagesEntity> page = this.page(
                new Query<PmsSpuImagesEntity>().getPage(params),
                new QueryWrapper<PmsSpuImagesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveImages(String id, List<String> images) {
        List<PmsSpuImagesEntity> collect = images.stream().map(item -> {
            PmsSpuImagesEntity pmsSpuImagesEntity = new PmsSpuImagesEntity();
            pmsSpuImagesEntity.setSpuId(id);
            pmsSpuImagesEntity.setImgUrl(item);
            return pmsSpuImagesEntity;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }

}