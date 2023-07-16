package com.atguigu.gulimall.search.controller;

import com.atguigu.gulimall.common.to.es.SkuEsModel;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.search.service.ElasticSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/search/save")
public class ElasticSaveController {

    @Autowired
    private ElasticSaveService elasticSaveService;

    /**
     * 保存上架商品
     * @param skuEsModels
     * @return
     */
    @PostMapping("product")
    public R productUp(@RequestBody List<SkuEsModel> skuEsModels){
        boolean result = false;
        try {
            result = elasticSaveService.productUp(skuEsModels);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("商品上架异常，{}", e);
        }
        if (!result){
            return R.ok();
        }else {
            return R.error();
        }
    }
}
