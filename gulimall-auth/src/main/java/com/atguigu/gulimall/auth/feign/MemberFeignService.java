package com.atguigu.gulimall.auth.feign;


import com.atguigu.gulimall.auth.vo.SocialUser;
import com.atguigu.gulimall.auth.vo.UserLoginVo;
import com.atguigu.gulimall.auth.vo.UserRegisterVo;
import com.atguigu.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@FeignClient("gulimall-member")
public interface MemberFeignService {

    @PostMapping(value = "/member/umsmember/register")
    R register(@RequestBody UserRegisterVo vo);

    @PostMapping(value = "/member/umsmember/login")
    R login(@RequestBody UserLoginVo vo);

    @PostMapping(value = "/member/umsmember/oauth2/login")
    R oauthLogin(@RequestBody SocialUser socialUser) throws Exception;

}
