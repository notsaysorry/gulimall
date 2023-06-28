package com.atguigu.gulimall.ware.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.ware.dao.WmsPurchaseDetailDao;
import com.atguigu.gulimall.ware.entity.WmsPurchaseDetailEntity;
import com.atguigu.gulimall.ware.service.WmsPurchaseDetailService;


@Service("wmsPurchaseDetailService")
public class WmsPurchaseDetailServiceImpl extends ServiceImpl<WmsPurchaseDetailDao, WmsPurchaseDetailEntity> implements WmsPurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WmsPurchaseDetailEntity> wrapper = new QueryWrapper<>();
        String key = (String)params.get("key");
        String status = (String)params.get("status");
        String wareId = (String)params.get("wareId");
        if (StringUtils.isNotBlank(key)){
            wrapper.and(item -> {
                item.eq("id", key).or().eq("sku_id", key);
            });
        }
        if (StringUtils.isNotBlank(status)){
            wrapper.eq("status", status);
        }
        if (StringUtils.isNotBlank(wareId)){
            wrapper.eq("ware_id", wareId);
        }
        IPage<WmsPurchaseDetailEntity> page = this.page(
                new Query<WmsPurchaseDetailEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}