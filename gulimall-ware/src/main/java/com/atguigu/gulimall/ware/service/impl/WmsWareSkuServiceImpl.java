package com.atguigu.gulimall.ware.service.impl;

import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.ware.feign.ProductFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.ware.dao.WmsWareSkuDao;
import com.atguigu.gulimall.ware.entity.WmsWareSkuEntity;
import com.atguigu.gulimall.ware.service.WmsWareSkuService;


@Service("wmsWareSkuService")
public class WmsWareSkuServiceImpl extends ServiceImpl<WmsWareSkuDao, WmsWareSkuEntity> implements WmsWareSkuService {

    @Autowired
    private WmsWareSkuDao wareSkuDao;

    @Autowired
    private ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WmsWareSkuEntity> page = this.page(
                new Query<WmsWareSkuEntity>().getPage(params),
                new QueryWrapper<WmsWareSkuEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(String skuId, Integer skuNum, String wareId) {
        List<WmsWareSkuEntity> wareSkuEntities = this.baseMapper.selectList(new QueryWrapper<WmsWareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if (wareSkuEntities == null || wareSkuEntities.size() == 0){
            WmsWareSkuEntity wareSkuEntity = new WmsWareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setWareId(wareId);
            R skuInfo = productFeignService.info(skuId);
            if (skuInfo.getCode() == 0){
                Map<String, Object> skuInfoMap = (Map<String, Object>)skuInfo.get("pmsSkuInfo");
                wareSkuEntity.setSkuName((String) skuInfoMap.get("skuName"));
            }
            wareSkuEntity.setStockLocked(0);
            this.baseMapper.insert(wareSkuEntity);
        }else {
            wareSkuDao.updateStock(skuId, skuNum, wareId);
        }
    }

}