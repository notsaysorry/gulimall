package com.atguigu.gulimall.order.vo;


import com.atguigu.gulimall.order.entity.OmsOrderEntity;
import lombok.Data;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-07-04 22:34
 **/

@Data
public class SubmitOrderResponseVo {

    private OmsOrderEntity order;

    /** 错误状态码 **/
    private Integer code;


}
