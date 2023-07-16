package com.atguigu.gulimall.product.web;

import com.atguigu.gulimall.product.entity.PmsCategoryEntity;
import com.atguigu.gulimall.product.service.PmsCategoryService;
import com.atguigu.gulimall.product.vo.Catalog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {


    @Autowired
    private PmsCategoryService categoryService;

    @RequestMapping({"/", "/index.html"})
    public String index(Model model){
        // 查询一级分类
        List<PmsCategoryEntity> categoryEntities = categoryService.getLevel1Category();
        model.addAttribute("category", categoryEntities);
        return "index";
    }

    @ResponseBody
    @RequestMapping("/index/catalog.json")
    public Map<String, List<Catalog2Vo>> catalogJson(){
        Map<String, List<Catalog2Vo>> catalogJson = categoryService.catalogJson();
        return catalogJson;
    }


    @ResponseBody
    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

}
