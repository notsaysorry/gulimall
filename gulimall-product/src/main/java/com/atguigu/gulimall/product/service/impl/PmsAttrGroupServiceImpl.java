package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.dao.PmsAttrAttrgroupRelationDao;
import com.atguigu.gulimall.product.entity.PmsAttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.entity.PmsAttrEntity;
import com.atguigu.gulimall.product.service.PmsAttrService;
import com.atguigu.gulimall.product.vo.PmsAttrGroupRelationVo;
import com.atguigu.gulimall.product.vo.PmsAttrGroupWithAttrsVo;
import com.atguigu.gulimall.product.vo.SpuItemAttrGroupVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.PmsAttrGroupDao;
import com.atguigu.gulimall.product.entity.PmsAttrGroupEntity;
import com.atguigu.gulimall.product.service.PmsAttrGroupService;


@Service("pmsAttrGroupService")
public class PmsAttrGroupServiceImpl extends ServiceImpl<PmsAttrGroupDao, PmsAttrGroupEntity> implements PmsAttrGroupService {

    @Autowired
    private PmsAttrGroupDao pmsAttrGroupDao;
    @Autowired
    private PmsAttrAttrgroupRelationDao pmsAttrAttrgroupRelationDao;
    @Autowired
    private PmsAttrService pmsAttrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsAttrGroupEntity> page = this.page(
                new Query<PmsAttrGroupEntity>().getPage(params),
                new QueryWrapper<PmsAttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, String catalogId) {
        if (catalogId.equals("0")){
            IPage<PmsAttrGroupEntity> page = this.page(
                    new Query<PmsAttrGroupEntity>().getPage(params),
                    new QueryWrapper<PmsAttrGroupEntity>()
            );
            return new PageUtils(page);
        }else {
            String key = (String)params.get("key");
            QueryWrapper<PmsAttrGroupEntity> wrapper = new QueryWrapper<PmsAttrGroupEntity>().eq("catelog_id", catalogId);
            if (StringUtils.isNotBlank(key)){
                wrapper.and(item -> {
                    item.eq("attr_group_id", key).or().like("attr_group_name", key);
                });
            }
            IPage<PmsAttrGroupEntity> page = this.page(
                    new Query<PmsAttrGroupEntity>().getPage(params),
                    wrapper
            );
            return new PageUtils(page);
        }
    }

    @Override
    public void deleteAttrRelation(PmsAttrGroupRelationVo[] pmsAttrGroupRelationVos) {
        List<PmsAttrGroupRelationVo> attrGroupRelationList = Arrays.asList(pmsAttrGroupRelationVos);
        pmsAttrGroupDao.deleteAttrRelationBatch(attrGroupRelationList);
    }

    @Override
    public List<PmsAttrGroupWithAttrsVo> attrGroupWithAttrs(String catId) {
        List<PmsAttrGroupEntity> relationEntities = pmsAttrGroupDao.selectList(new QueryWrapper<PmsAttrGroupEntity>()
                .eq("catelog_id", catId));
        List<PmsAttrGroupWithAttrsVo> collect = relationEntities.stream().map(item -> {
            PmsAttrGroupWithAttrsVo pmsAttrGroupWithAttrsVo = new PmsAttrGroupWithAttrsVo();
            BeanUtils.copyProperties(item, pmsAttrGroupWithAttrsVo);
            List<PmsAttrEntity> pmsAttrEntities = pmsAttrService.attrRelation(item.getAttrGroupId());
            pmsAttrGroupWithAttrsVo.setAttrs(pmsAttrEntities);
            return pmsAttrGroupWithAttrsVo;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(String spuId, String catalogId) {
        List<SpuItemAttrGroupVo> spuItemAttrGroupVos = pmsAttrGroupDao.getAttrGroupWithAttrsBySpuId(spuId, catalogId);
        return spuItemAttrGroupVos;
    }

}