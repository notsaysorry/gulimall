package com.atguigu.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.PmsProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 20:12:53
 */
public interface PmsProductAttrValueService extends IService<PmsProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PmsProductAttrValueEntity> listForSpu(String spuId);

    void updateSpuAttr(String spuId, List<PmsProductAttrValueEntity> productAttrValueEntities);
}

