package com.atguigu.gulimall.product.vo;

import lombok.Data;

@Data
public class PmsAttrRespVo extends PmsAttrVo{

    private String catelogName;

    private String groupName;

    private String[] catelogPath;
}
