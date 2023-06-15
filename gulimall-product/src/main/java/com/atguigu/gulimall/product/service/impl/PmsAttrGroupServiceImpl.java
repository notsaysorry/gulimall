package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.vo.PmsAttrGroupRelationVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

}