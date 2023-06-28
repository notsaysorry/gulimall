package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.vo.PmsAttrGroupRelationVo;
import com.atguigu.gulimall.product.vo.PmsAttrGroupWithAttrsVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.PmsAttrGroupEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 20:12:53
 */
public interface PmsAttrGroupService extends IService<PmsAttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, String catalogId);

    void deleteAttrRelation(PmsAttrGroupRelationVo[] pmsAttrGroupRelationVos);

    List<PmsAttrGroupWithAttrsVo> attrGroupWithAttrs(String catId);
}

