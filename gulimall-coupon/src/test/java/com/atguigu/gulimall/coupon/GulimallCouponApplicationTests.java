package com.atguigu.gulimall.coupon;

import com.atguigu.gulimall.coupon.entity.CouponEntity;
import com.atguigu.gulimall.coupon.service.CouponService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GulimallCouponApplicationTests {

    @Autowired
    private CouponService couponService;

    @Test
    public void couponTest1(){
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setCouponName("普通优惠券");
        CouponEntity one = couponService.getOne(new QueryWrapper<CouponEntity>().eq("coupon_name", "普通优惠券"));
        System.out.println(one);
    }


    @Test
    void contextLoads() {
    }

}
