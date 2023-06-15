package com.atguigu.gulimall.product.dao;

import com.atguigu.gulimall.product.entity.PmsAttrGroupEntity;
import com.atguigu.gulimall.product.vo.PmsAttrGroupRelationVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 * 
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 20:12:53
 */
@Mapper
public interface PmsAttrGroupDao extends BaseMapper<PmsAttrGroupEntity> {

    void deleteAttrRelationBatch(@Param("attrGroupRelationList") List<PmsAttrGroupRelationVo> attrGroupRelationList);
}
