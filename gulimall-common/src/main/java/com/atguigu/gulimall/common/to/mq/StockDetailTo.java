package com.atguigu.gulimall.common.to.mq;

import lombok.Data;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-07-06 21:14
 **/

@Data
public class StockDetailTo {

    private String id;
    /**
     * sku_id
     */
    private String skuId;
    /**
     * sku_name
     */
    private String skuName;
    /**
     * 购买个数
     */
    private Integer skuNum;
    /**
     * 工作单id
     */
    private String taskId;

    /**
     * 仓库id
     */
    private String wareId;

    /**
     * 锁定状态
     */
    private Integer lockStatus;

}
