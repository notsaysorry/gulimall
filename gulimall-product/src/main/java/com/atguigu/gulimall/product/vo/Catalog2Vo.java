package com.atguigu.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Catalog2Vo implements Serializable{



    // 一级分类的id
    private String catalog1Id;
    // 三级子分类的集合
    private List<Catalog3Vo> catalog3List;
    private String id;
    private String name;

    /**
     * 三级分类
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Catalog3Vo implements Serializable{
        // 父分类，二级分类id
        private String catalog2Id;
        private String id;
        private String name;
    }

}
