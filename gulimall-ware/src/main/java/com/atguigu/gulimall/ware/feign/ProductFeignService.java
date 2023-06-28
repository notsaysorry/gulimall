package com.atguigu.gulimall.ware.feign;

import com.atguigu.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimall-product")
public interface ProductFeignService {


    @RequestMapping("/product/pmsskuinfo/info/{skuId}")
    R info(@PathVariable("skuId") String skuId);
}
