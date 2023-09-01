package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.common.constant.ProductConstant;
import com.atguigu.gulimall.product.dao.PmsAttrAttrgroupRelationDao;
import com.atguigu.gulimall.product.dao.PmsAttrGroupDao;
import com.atguigu.gulimall.product.dao.PmsCategoryDao;
import com.atguigu.gulimall.product.entity.PmsAttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.entity.PmsAttrGroupEntity;
import com.atguigu.gulimall.product.entity.PmsCategoryEntity;
import com.atguigu.gulimall.product.service.PmsAttrAttrgroupRelationService;
import com.atguigu.gulimall.product.service.PmsCategoryService;
import com.atguigu.gulimall.product.vo.PmsAttrRespVo;
import com.atguigu.gulimall.product.vo.PmsAttrVo;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.PmsAttrDao;
import com.atguigu.gulimall.product.entity.PmsAttrEntity;
import com.atguigu.gulimall.product.service.PmsAttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsAttrService")
public class PmsAttrServiceImpl extends ServiceImpl<PmsAttrDao, PmsAttrEntity> implements PmsAttrService {

    @Autowired
    private PmsAttrAttrgroupRelationService pmsAttrAttrgroupRelationService;
    @Autowired
    private PmsCategoryService pmsCategoryService;
    @Autowired
    private PmsAttrDao pmsAttrDao;
    @Autowired
    private PmsAttrAttrgroupRelationDao pmsAttrAttrgroupRelationDao;
    @Autowired
    private PmsAttrGroupDao pmsAttrGroupDao;
    @Autowired
    private PmsCategoryDao pmsCategoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsAttrEntity> page = this.page(
                new Query<PmsAttrEntity>().getPage(params),
                new QueryWrapper<PmsAttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryBasePage(Map<String, Object> params, String categoryId) {
        QueryWrapper<PmsAttrEntity> wrapper = new QueryWrapper<PmsAttrEntity>().eq("attr_type", 1);
        if (!categoryId.equals("0")) {
            wrapper.eq("catelog_id", categoryId);
        }
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            wrapper.eq("attr_id", key).or().like("attr_name", key);
        }
        IPage<PmsAttrEntity> page = this.page(
                new Query<PmsAttrEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<PmsAttrEntity> records = page.getRecords();
        List<PmsAttrRespVo> PmsAttrRespVos = records.stream().map(item -> {
            PmsAttrRespVo pmsAttrRespVo = new PmsAttrRespVo();
            BeanUtils.copyProperties(item, pmsAttrRespVo);
            String attrId = item.getAttrId();
            PmsAttrAttrgroupRelationEntity attrgroupRelation = pmsAttrAttrgroupRelationDao.selectOne(new QueryWrapper<PmsAttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if (attrgroupRelation != null) {
                PmsAttrGroupEntity attrGroup = pmsAttrGroupDao.selectOne(new QueryWrapper<PmsAttrGroupEntity>().eq("attr_group_id",
                        attrgroupRelation.getAttrGroupId()));
                pmsAttrRespVo.setGroupName(attrGroup.getAttrGroupName());
            }
            PmsCategoryEntity category = pmsCategoryDao.selectOne(new QueryWrapper<PmsCategoryEntity>().eq("cat_id",
                    item.getCatelogId()));
            if (category != null) {
                pmsAttrRespVo.setCatelogName(category.getName());
            }
            return pmsAttrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(PmsAttrRespVos);
        return pageUtils;
    }

    @Override
    public PageUtils querySalePage(Map<String, Object> params, String categoryId) {
        QueryWrapper<PmsAttrEntity> wrapper = new QueryWrapper<PmsAttrEntity>().eq("attr_type", 0);
        if (!categoryId.equals("0")) {
            wrapper.eq("catelog_id", categoryId);
        }
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            wrapper.eq("attr_id", key).or().like("attr_name", key);
        }
        IPage<PmsAttrEntity> page = this.page(
                new Query<PmsAttrEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<PmsAttrEntity> records = page.getRecords();
        List<PmsAttrRespVo> PmsAttrRespVos = records.stream().map(item -> {
            PmsAttrRespVo pmsAttrRespVo = new PmsAttrRespVo();
            BeanUtils.copyProperties(item, pmsAttrRespVo);
            String attrId = item.getAttrId();
            PmsAttrAttrgroupRelationEntity attrgroupRelation = pmsAttrAttrgroupRelationDao.selectOne(new QueryWrapper<PmsAttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if (attrgroupRelation != null) {
                PmsAttrGroupEntity attrGroup = pmsAttrGroupDao.selectOne(new QueryWrapper<PmsAttrGroupEntity>().eq("attr_group_id",
                        attrgroupRelation.getAttrGroupId()));
                pmsAttrRespVo.setGroupName(attrGroup.getAttrGroupName());
            }
            PmsCategoryEntity category = pmsCategoryDao.selectOne(new QueryWrapper<PmsCategoryEntity>().eq("cat_id",
                    item.getCatelogId()));
            if (category != null) {
                pmsAttrRespVo.setCatelogName(category.getName());
            }
            return pmsAttrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(PmsAttrRespVos);
        return pageUtils;
    }

    @Transactional
    @Override
    public void saveDetail(PmsAttrVo pmsAttrVo) {
        PmsAttrEntity pmsAttrEntity = new PmsAttrEntity();
        BeanUtils.copyProperties(pmsAttrVo, pmsAttrEntity);
        baseMapper.insert(pmsAttrEntity);
        if (StringUtils.isNotBlank(pmsAttrVo.getAttrGroupId())) {
            pmsAttrAttrgroupRelationService.saveAttrgroup(pmsAttrEntity.getAttrId(), pmsAttrVo.getAttrGroupId());
        }
    }

    @Override
    public PmsAttrRespVo getAttrInfo(String attrId) {
        PmsAttrEntity pmsAttrEntity = this.getById(attrId);
        PmsAttrRespVo pmsAttrRespVo = new PmsAttrRespVo();
        BeanUtils.copyProperties(pmsAttrEntity, pmsAttrRespVo);
        PmsAttrAttrgroupRelationEntity attrgroupRelationEntity = pmsAttrAttrgroupRelationDao.selectOne(new QueryWrapper<PmsAttrAttrgroupRelationEntity>()
                .eq("attr_id", attrId));
        if (attrgroupRelationEntity != null) {
            pmsAttrRespVo.setAttrGroupId(attrgroupRelationEntity.getAttrGroupId());
            PmsAttrGroupEntity attrGroupEntity = pmsAttrGroupDao.selectOne(new QueryWrapper<PmsAttrGroupEntity>()
                    .eq("attr_group_id", attrgroupRelationEntity.getAttrGroupId()));
            pmsAttrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
        }

        String[] catelogs = pmsCategoryService.queryCatelogs(pmsAttrEntity.getCatelogId());
        pmsAttrRespVo.setCatelogPath(catelogs);
        return pmsAttrRespVo;
    }

    @Override
    public void updateAttr(PmsAttrVo pmsAttrVo) {
        PmsAttrEntity pmsAttrEntity = new PmsAttrEntity();
        BeanUtils.copyProperties(pmsAttrVo, pmsAttrEntity);
        this.updateById(pmsAttrEntity);
        if (pmsAttrVo.getAttrType() == ProductConstant.AttrConstant.ATTR_TYPE_BASE.getCode()) {
            PmsAttrAttrgroupRelationEntity attrgroupRelationEntity = new PmsAttrAttrgroupRelationEntity();
            attrgroupRelationEntity.setAttrId(pmsAttrEntity.getAttrId());
            attrgroupRelationEntity.setAttrGroupId(pmsAttrVo.getAttrGroupId());

            Long count = pmsAttrAttrgroupRelationDao.selectCount(new QueryWrapper<PmsAttrAttrgroupRelationEntity>()
                    .eq("attr_id", pmsAttrEntity.getAttrId()));
            if (count > 0) {
                // 修改属性对应的分组
                pmsAttrAttrgroupRelationDao.update(attrgroupRelationEntity, new UpdateWrapper<PmsAttrAttrgroupRelationEntity>()
                        .eq("attr_id", pmsAttrEntity.getAttrId()));
            } else {
                // 新增
                pmsAttrAttrgroupRelationDao.insert(attrgroupRelationEntity);
            }
        }

    }

    @Override
    public List<PmsAttrEntity> attrRelation(String groupId) {
        List<PmsAttrEntity> result = new ArrayList<>();
        List<PmsAttrAttrgroupRelationEntity> attrGroups = pmsAttrAttrgroupRelationDao.selectList(new QueryWrapper<PmsAttrAttrgroupRelationEntity>()
                .eq("attr_group_id", groupId));
        List<String> attrIdList = attrGroups.stream().map(item -> item.getAttrId()).collect(Collectors.toList());
        if (attrIdList != null && attrIdList.size() > 0) {
            result = this.listByIds(attrIdList);
        }
        return result;
    }

    @Override
    public PageUtils attrNoRelation(Map<String, Object> params, String groupId) {
        // 查询当前分组所属的三级分类
        PmsAttrGroupEntity attrGroupEntity = pmsAttrGroupDao.selectOne(new QueryWrapper<PmsAttrGroupEntity>().eq("attr_group_id", groupId));
        // 查询当前三级分类所有分组
        List<PmsAttrGroupEntity> attrGroupEntityList = pmsAttrGroupDao.selectList(new QueryWrapper<PmsAttrGroupEntity>().eq("catelog_id", attrGroupEntity.getCatelogId()));
        List<String> groupIdList = attrGroupEntityList.stream().map(item -> item.getAttrGroupId()).collect(Collectors.toList());
        List<PmsAttrAttrgroupRelationEntity> relationEntities = pmsAttrAttrgroupRelationDao.selectList(new QueryWrapper<PmsAttrAttrgroupRelationEntity>()
                .in("attr_group_id", groupIdList));
        List<String> attrIdList = relationEntities.stream().map(item -> item.getAttrId()).collect(Collectors.toList());
        QueryWrapper<PmsAttrEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("catelog_id", attrGroupEntity.getCatelogId()).eq("attr_type", ProductConstant.AttrConstant.ATTR_TYPE_BASE.getCode());
        String key = (String)params.get("key");
        if (StringUtils.isNotBlank(key)){
            wrapper.and(item -> item.eq("attr_id", key).or().like("attr_name", key));
        }
        if (attrIdList != null && attrIdList.size() > 0){
            wrapper.notIn("attr_id", attrIdList);
        }
        IPage<PmsAttrEntity> page = this.page(
                new Query<PmsAttrEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    @Override
    public List<String> querySearchAttr(List<String> attrIdList) {
        return this.baseMapper.querySearchAttr(attrIdList);
    }

}