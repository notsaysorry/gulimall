package com.atguigu.gulimall.product.feign;

import com.atguigu.gulimall.common.to.SkuReductionTo;
import com.atguigu.gulimall.common.to.SpuBoundsTo;
import com.atguigu.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    @PostMapping("/coupon/spubounds/save")
    R save(@RequestBody SpuBoundsTo spuBoundsTo);

    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveinfo(@RequestBody SkuReductionTo skuReductionTo);
}
