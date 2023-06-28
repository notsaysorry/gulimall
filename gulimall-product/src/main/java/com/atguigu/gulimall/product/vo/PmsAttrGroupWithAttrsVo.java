package com.atguigu.gulimall.product.vo;

import com.atguigu.gulimall.product.entity.PmsAttrEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class PmsAttrGroupWithAttrsVo {

    /**
     * 分组id
     */
    private String attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private String catelogId;

    private List<PmsAttrEntity> attrs;
}
