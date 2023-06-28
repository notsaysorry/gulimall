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

import com.atguigu.gulimall.product.dao.PmsProductAttrValueDao;
import com.atguigu.gulimall.product.entity.PmsProductAttrValueEntity;
import com.atguigu.gulimall.product.service.PmsProductAttrValueService;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsProductAttrValueService")
public class PmsProductAttrValueServiceImpl extends ServiceImpl<PmsProductAttrValueDao, PmsProductAttrValueEntity> implements PmsProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsProductAttrValueEntity> page = this.page(
                new Query<PmsProductAttrValueEntity>().getPage(params),
                new QueryWrapper<PmsProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<PmsProductAttrValueEntity> listForSpu(String spuId) {
        List<PmsProductAttrValueEntity> attrValueEntities = this.baseMapper.selectList(new QueryWrapper<PmsProductAttrValueEntity>().eq("spu_id", spuId));
        return attrValueEntities;
    }

    @Transactional
    @Override
    public void updateSpuAttr(String spuId, List<PmsProductAttrValueEntity> productAttrValueEntities) {
        // 删除原来的属性
        this.baseMapper.delete(new QueryWrapper<PmsProductAttrValueEntity>().eq("spu_id", spuId));
        List<PmsProductAttrValueEntity> collect = productAttrValueEntities.stream().map(item -> {
            item.setSpuId(spuId);
            return item;
        }).collect(Collectors.toList());
        // 新增属性
        this.saveBatch(collect);
    }

}