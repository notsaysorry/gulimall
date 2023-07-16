package com.atguigu.gulimall.product.feign;


import com.atguigu.gulimall.common.to.SkuHasStockTo;
import com.atguigu.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("gulimall-ware")
public interface WareFeignService {

    @RequestMapping("/ware/wmswaresku/hasstock")
    R hasStock(@RequestBody List<String> skuIds);

}
