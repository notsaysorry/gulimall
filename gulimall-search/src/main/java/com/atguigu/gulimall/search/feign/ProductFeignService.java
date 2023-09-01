package com.atguigu.gulimall.search.feign;

import com.atguigu.gulimall.common.to.es.SkuEsModel;
import com.atguigu.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Component
@FeignClient("gulimall-product")
public interface ProductFeignService {

    @RequestMapping("/product/pmsattr/info/{attrId}")
    R info(@PathVariable("attrId") String attrId);
}
