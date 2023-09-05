package com.atguigu.gulimall.member.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.member.dao.UmsMemberReceiveAddressDao;
import com.atguigu.gulimall.member.entity.UmsMemberReceiveAddressEntity;
import com.atguigu.gulimall.member.service.UmsMemberReceiveAddressService;


@Service("umsMemberReceiveAddressService")
public class UmsMemberReceiveAddressServiceImpl extends ServiceImpl<UmsMemberReceiveAddressDao, UmsMemberReceiveAddressEntity> implements UmsMemberReceiveAddressService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UmsMemberReceiveAddressEntity> page = this.page(
                new Query<UmsMemberReceiveAddressEntity>().getPage(params),
                new QueryWrapper<UmsMemberReceiveAddressEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<UmsMemberReceiveAddressEntity> getAddress(String memberId) {
        List<UmsMemberReceiveAddressEntity> addressList = this.baseMapper.selectList
                (new QueryWrapper<UmsMemberReceiveAddressEntity>().eq("member_id", memberId));
        return addressList;
    }

}