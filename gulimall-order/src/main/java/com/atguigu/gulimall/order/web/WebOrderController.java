package com.atguigu.gulimall.order.web;

import com.atguigu.gulimall.order.service.OmsOrderService;
import com.atguigu.gulimall.order.vo.OrderConfirmVo;
import com.atguigu.gulimall.order.vo.OrderSubmitVo;
import com.atguigu.gulimall.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.ExecutionException;

@Controller
public class WebOrderController {

    @Autowired
    private OmsOrderService orderService;

    @RequestMapping("list.html")
    public String list() {
        return "list";
    }

    @RequestMapping("toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = orderService.confirmOrder();
        model.addAttribute("confirmOrderData", confirmVo);
        return "confirm";
    }

    /**
     * 下单功能
     * @param vo
     * @return
     */
    @PostMapping(value = "/submitOrder")
    public String submitOrder(OrderSubmitVo vo) {
        SubmitOrderResponseVo responseVo = orderService.submitOrder(vo);
        return null;
    }

    @RequestMapping("detail.html")
    public String detail() {
        return "detail";
    }

    @RequestMapping("pay.html")
    public String pay() {
        return "pay";
    }
}
