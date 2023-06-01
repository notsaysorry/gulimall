package com.atguigu.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
        List<PmsCategoryEntity> listWithTree = categoryList.stream().filter(menu -> menu.getParentCid() == 0)
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

    private List<PmsCategoryEntity> childrenCategory(PmsCategoryEntity root, List<PmsCategoryEntity> all){
        return all.stream().filter(menu -> menu.getParentCid() == root.getCatId())
                .map(menu -> {
                    menu.setChildren(childrenCategory(menu, all));
                    return menu;
                })
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
    }

}