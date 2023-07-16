package com.atguigu.gulimall.product;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;


@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void redisTest(){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("hello", "world");
    }

    @Test
    public void redissonTest(){
        System.out.println(redissonClient);
    }

}
