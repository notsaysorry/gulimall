package com.atguigu.gulimall.seckill.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-07-09 21:13
 **/

@Data
public class SeckillSkuVo {

    private String id;
    /**
     * 活动id
     */
    private String promotionId;
    /**
     * 活动场次id
     */
    private String promotionSessionId;
    /**
     * 商品id
     */
    private String skuId;
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 秒杀总量
     */
    private Integer seckillCount;
    /**
     * 每人限购数量
     */
    private Integer seckillLimit;
    /**
     * 排序
     */
    private Integer seckillSort;

}
