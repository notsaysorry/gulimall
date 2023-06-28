package com.atguigu.gulimall.common.to;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SpuBoundsTo {

    private String spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
