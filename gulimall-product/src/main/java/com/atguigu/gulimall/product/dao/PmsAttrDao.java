package com.atguigu.gulimall.product.dao;

import com.atguigu.gulimall.product.entity.PmsAttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 * 
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 20:12:53
 */
@Mapper
public interface PmsAttrDao extends BaseMapper<PmsAttrEntity> {

    List<String> querySearchAttr(@Param("attrIdList") List<String> attrIdList);
}
