package com.atguigu.gulimall.ware.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.ware.dao.WmsWareInfoDao;
import com.atguigu.gulimall.ware.entity.WmsWareInfoEntity;
import com.atguigu.gulimall.ware.service.WmsWareInfoService;


@Service("wmsWareInfoService")
public class WmsWareInfoServiceImpl extends ServiceImpl<WmsWareInfoDao, WmsWareInfoEntity> implements WmsWareInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WmsWareInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String)params.get("key");
        if (StringUtils.isNotBlank(key)){
            wrapper.eq("id", key)
                    .or().like("name", key)
                    .or().like("address", key)
                    .or().eq("areacode", key);
        }

        IPage<WmsWareInfoEntity> page = this.page(
                new Query<WmsWareInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}