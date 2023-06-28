package com.atguigu.gulimall.coupon.service;

import com.atguigu.gulimall.common.to.SkuReductionTo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 18:12:05
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveinfo(SkuReductionTo skuReductionTo);
}

