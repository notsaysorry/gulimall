package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.vo.PmsAttrGroupRelationVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.PmsAttrAttrgroupRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 20:12:53
 */
public interface PmsAttrAttrgroupRelationService extends IService<PmsAttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttrgroup(String attrId, String groupId);

    void addRelation(List<PmsAttrGroupRelationVo> pmsAttrGroupRelationVos);
}

