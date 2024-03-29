package com.atguigu.gulimall.order.to;

import com.atguigu.gulimall.order.entity.OmsOrderEntity;
import com.atguigu.gulimall.order.entity.OmsOrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-07-04 23:04
 **/

@Data
public class OrderCreateTo {

    private OmsOrderEntity order;

    private List<OmsOrderItemEntity> orderItems;

    /** 订单计算的应付价格 **/
    private BigDecimal payPrice;

    /** 运费 **/
    private BigDecimal fare;

}
