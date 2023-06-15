package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.vo.PmsAttrGroupRelationVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.PmsAttrAttrgroupRelationDao;
import com.atguigu.gulimall.product.entity.PmsAttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.service.PmsAttrAttrgroupRelationService;


@Service("pmsAttrAttrgroupRelationService")
public class PmsAttrAttrgroupRelationServiceImpl extends ServiceImpl<PmsAttrAttrgroupRelationDao, PmsAttrAttrgroupRelationEntity> implements PmsAttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsAttrAttrgroupRelationEntity> page = this.page(
                new Query<PmsAttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<PmsAttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveAttrgroup(String attrId, String groupId) {
        PmsAttrAttrgroupRelationEntity pmsAttrAttrgroupRelationEntity = new PmsAttrAttrgroupRelationEntity();
        pmsAttrAttrgroupRelationEntity.setAttrId(attrId);
        pmsAttrAttrgroupRelationEntity.setAttrGroupId(groupId);
        baseMapper.insert(pmsAttrAttrgroupRelationEntity);
    }

    @Override
    public void addRelation(List<PmsAttrGroupRelationVo> pmsAttrGroupRelationVos) {
        List<PmsAttrAttrgroupRelationEntity> collect = pmsAttrGroupRelationVos.stream().map(item -> {
            PmsAttrAttrgroupRelationEntity attrgroupRelationEntity = new PmsAttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, attrgroupRelationEntity);
            return attrgroupRelationEntity;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }

}