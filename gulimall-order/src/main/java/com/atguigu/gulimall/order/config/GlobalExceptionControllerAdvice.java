package com.atguigu.gulimall.order.config;

import com.atguigu.gulimall.common.exception.BizCodeEnum;
import com.atguigu.gulimall.common.utils.R;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.atguigu.gulimall.order")
public class GlobalExceptionControllerAdvice {


    @ExceptionHandler(value=Exception.class)
    public R handleException(Exception e){
        return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), BizCodeEnum.UNKNOWN_EXCEPTION.getMessage()).put("data", e.getMessage());
    }
}
