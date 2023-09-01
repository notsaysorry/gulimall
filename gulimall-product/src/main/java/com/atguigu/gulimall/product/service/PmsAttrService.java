package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.vo.PmsAttrRespVo;
import com.atguigu.gulimall.product.vo.PmsAttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.PmsAttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 20:12:53
 */
public interface PmsAttrService extends IService<PmsAttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryBasePage(Map<String, Object> params, String categoryId);

    PageUtils querySalePage(Map<String, Object> params, String categoryId);

    void saveDetail(PmsAttrVo pmsAttrVo);

    PmsAttrRespVo getAttrInfo(String attrId);

    void updateAttr(PmsAttrVo pmsAttrVo);

    List<PmsAttrEntity> attrRelation(String groupId);

    PageUtils attrNoRelation(Map<String, Object> params, String groupId);

    List<String> querySearchAttr(List<String> attrIdList);
}

