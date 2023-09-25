package com.atguigu.gulimall.order.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.order.entity.OmsOrderEntity;
import com.atguigu.gulimall.order.service.OmsOrderService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.R;



/**
 * 订单
 *
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 20:10:34
 */
@RestController
@RequestMapping("order/omsorder")
public class OmsOrderController {
    @Autowired
    private OmsOrderService omsOrderService;

    /**
     * 根据订单编号查询订单状态
     * @param orderSn
     * @return
     */
    @GetMapping(value = "/status/{orderSn}")
    public R getOrderStatus(@PathVariable("orderSn") String orderSn) {
        OmsOrderEntity orderEntity = omsOrderService.getOrderByOrderSn(orderSn);
        R ok = R.ok();
        ok.setData(orderEntity);
        return ok;
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("order:omsorder:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = omsOrderService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 分页查询当前登录用户的所有订单信息
     * @param params
     * @return
     */
    @PostMapping("/listWithItem")
    public R listWithItem(@RequestBody Map<String, Object> params){
        PageUtils page = omsOrderService.queryPageWithItem(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("order:omsorder:info")
    public R info(@PathVariable("id") Long id){
		OmsOrderEntity omsOrder = omsOrderService.getById(id);

        return R.ok().put("omsOrder", omsOrder);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("order:omsorder:save")
    public R save(@RequestBody OmsOrderEntity omsOrder){
		omsOrderService.save(omsOrder);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("order:omsorder:update")
    public R update(@RequestBody OmsOrderEntity omsOrder){
		omsOrderService.updateById(omsOrder);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("order:omsorder:delete")
    public R delete(@RequestBody Long[] ids){
		omsOrderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
