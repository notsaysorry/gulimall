package com.atguigu.gulimall.cart.service;


import com.atguigu.gulimall.cart.vo.CartItemVo;
import com.atguigu.gulimall.cart.vo.CartVo;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-06-30 17:06
 **/
public interface CartService {

    /**
     * 将商品添加至购物车
     * @param skuId
     * @param num
     * @return
     */
    CartItemVo addToCart(String skuId, Integer num) throws ExecutionException, InterruptedException;

    CartItemVo getCartItem(String skuId);

    CartVo getCart() throws ExecutionException, InterruptedException;

    /**
     * 清空购物车的数据
     * @param cartKey
     */
    void clearCartInfo(String cartKey);

    void checkItem(String skuId, Integer checked);

    void changeItemCount(String skuId, Integer num);

    void deleteIdCartInfo(String skuId);
}
