package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.service.PmsCategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.PmsCategoryDao;
import com.atguigu.gulimall.product.entity.PmsCategoryEntity;
import com.atguigu.gulimall.product.service.PmsCategoryService;


@Service("pmsCategoryService")
public class PmsCategoryServiceImpl extends ServiceImpl<PmsCategoryDao, PmsCategoryEntity> implements PmsCategoryService {

    @Autowired
    private PmsCategoryBrandRelationService pmsCategoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsCategoryEntity> page = this.page(
                new Query<PmsCategoryEntity>().getPage(params),
                new QueryWrapper<PmsCategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<PmsCategoryEntity> listWithTree() {
        // 查询所有的分类
        List<PmsCategoryEntity> categoryList= baseMapper.selectList(null);
        // 为所有的一级分类添加子分类
        List<PmsCategoryEntity> listWithTree = categoryList.stream().filter(menu -> menu.getParentCid().equals("0"))
                .map(menu -> {
                    menu.setChildren(childrenCategory(menu, categoryList));
                    return menu;
                })
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());

        return listWithTree;
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        // TODO 判断菜单是否被其他地方引用

        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public String[] queryCatelogs(String catalogId) {
        List<String> catelogs = new ArrayList<>();
        catelogs = queryParentCatelogs(catalogId, catelogs);
        Collections.reverse(catelogs);
        return catelogs.toArray(new String[catelogs.size()]);
    }

    @Override
    public void updateDetail(PmsCategoryEntity pmsCategory) {
        this.updateById(pmsCategory);
        if (StringUtils.isNotBlank(pmsCategory.getName())){
            pmsCategoryBrandRelationService.updateCategory(pmsCategory.getCatId(), pmsCategory.getName());
        }
    }

    public List<String> queryParentCatelogs(String catalogId, List<String> catelogs){
        catelogs.add(catalogId);
        PmsCategoryEntity pmsCategoryEntity = baseMapper.selectById(catalogId);
        if (!pmsCategoryEntity.getParentCid().equals("0")){
            queryParentCatelogs(pmsCategoryEntity.getParentCid(), catelogs);
        }
        return catelogs;
    }



    private List<PmsCategoryEntity> childrenCategory(PmsCategoryEntity root, List<PmsCategoryEntity> all){
        return all.stream().filter(menu -> menu.getParentCid().equals(root.getCatId()))
                .map(menu -> {
                    menu.setChildren(childrenCategory(menu, all));
                    return menu;
                })
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
    }

}