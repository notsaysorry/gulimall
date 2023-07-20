package com.atguigu.gulimall.search.controller;

import com.atguigu.gulimall.search.service.SearchService;
import com.atguigu.gulimall.search.vo.SearchParam;
import com.atguigu.gulimall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping("/list.html")
    public String search(SearchParam searchParam){
        // 封装检索条件，根据检索条件得到值
        SearchResult searchResult = searchService.search(searchParam);
        return "list";
    }
}
