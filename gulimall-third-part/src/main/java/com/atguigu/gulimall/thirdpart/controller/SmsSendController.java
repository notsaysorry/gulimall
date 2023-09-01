package com.atguigu.gulimall.thirdpart.controller;


import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.thirdpart.component.TencentSmsComponent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-06-27 10:04
 **/

@Controller
@RequestMapping(value = "/sms")
public class SmsSendController {

    @Resource
    private TencentSmsComponent tencentSmsComponent;

    /**
     * 提供给别的服务进行调用
     * @param phone
     * @param code
     * @return
     */
    @GetMapping(value = "/sendCode")
    public R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code) {

        //发送验证码
        tencentSmsComponent.send(phone,code);

        return R.ok();
    }

}
