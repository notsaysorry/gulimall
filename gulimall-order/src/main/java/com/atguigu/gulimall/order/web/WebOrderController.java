package com.atguigu.gulimall.order.web;

import com.atguigu.gulimall.order.service.OmsOrderService;
import com.atguigu.gulimall.order.vo.OrderConfirmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.ExecutionException;

@Controller
public class WebOrderController {

    @Autowired
    private OmsOrderService orderService;

    @RequestMapping("list.html")
    public String list(){
        return "list";
    }

    @RequestMapping("toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = orderService.confirmOrder();
        model.addAttribute("confirmOrderData", confirmVo);
        return "confirm";
    }

    @RequestMapping("detail.html")
    public String detail(){
        return "detail";
    }

    @RequestMapping("pay.html")
    public String pay(){
        return "pay";
    }
}
