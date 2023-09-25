package com.atguigu.gulimall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.product.entity.PmsSkuImagesEntity;
import com.atguigu.gulimall.product.entity.PmsSpuInfoDescEntity;
import com.atguigu.gulimall.product.feign.SeckillFeignService;
import com.atguigu.gulimall.product.service.*;
import com.atguigu.gulimall.product.vo.SeckillSkuVo;
import com.atguigu.gulimall.product.vo.SkuItemSaleAttrVo;
import com.atguigu.gulimall.product.vo.SkuItemVo;
import com.atguigu.gulimall.product.vo.SpuItemAttrGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.PmsSkuInfoDao;
import com.atguigu.gulimall.product.entity.PmsSkuInfoEntity;


@Service("pmsSkuInfoService")
public class PmsSkuInfoServiceImpl extends ServiceImpl<PmsSkuInfoDao, PmsSkuInfoEntity> implements PmsSkuInfoService {

    @Autowired
    private PmsSkuImagesService skuImagesService;
    @Autowired
    private PmsSpuInfoDescService spuInfoDescService;
    @Autowired
    private PmsAttrGroupService attrGroupService;
    @Autowired
    private PmsSkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private SeckillFeignService seckillFeignService;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsSkuInfoEntity> page = this.page(
                new Query<PmsSkuInfoEntity>().getPage(params),
                new QueryWrapper<PmsSkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public SkuItemVo item(String skuId) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = new SkuItemVo();


        CompletableFuture<PmsSkuInfoEntity> SkuInfoFuture = CompletableFuture.supplyAsync(() -> {
            // 1、sku基本信息获取
            PmsSkuInfoEntity skuInfoEntity = this.baseMapper.selectById(skuId);
            skuItemVo.setInfo(skuInfoEntity);
            return skuInfoEntity;
        }, threadPoolExecutor);

        CompletableFuture<Void> skuImagesFuture = CompletableFuture.runAsync(() -> {
            // 2、sku图片信息
            List<PmsSkuImagesEntity> skuImagesEntityList = skuImagesService.getImagesBySkuId(skuId);
            skuItemVo.setImages(skuImagesEntityList);
        }, threadPoolExecutor);

        CompletableFuture<Void> skuItemSaleFuture = SkuInfoFuture.thenAcceptAsync((res) -> {
            // 3、获取spu的销售属性组合
            List<SkuItemSaleAttrVo> skuItemSaleAttrVos = skuSaleAttrValueService.getSaleAttrsBySpuId(res.getSpuId());
            skuItemVo.setSaleAttr(skuItemSaleAttrVos);
        }, threadPoolExecutor);



        CompletableFuture<Void> spuInfoFuture = SkuInfoFuture.thenAcceptAsync((res) -> {
            // 4、获取spu的介绍
            PmsSpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getDescBySpuId(res.getSpuId());
            skuItemVo.setDesc(spuInfoDescEntity);
        }, threadPoolExecutor);


        CompletableFuture<Void> spuItemAttrFuture = SkuInfoFuture.thenAcceptAsync((res) -> {
            // 5、获取spu的规格参数信息
            List<SpuItemAttrGroupVo> spuItemAttrGroupVos = attrGroupService.getAttrGroupWithAttrsBySpuId(res.getSpuId(), res.getCatalogId());
            skuItemVo.setGroupAttrs(spuItemAttrGroupVos);
        }, threadPoolExecutor);

        CompletableFuture<Void> seckillFuture = CompletableFuture.runAsync(() -> {
            //3、远程调用查询当前sku是否参与秒杀优惠活动
            R skuSeckilInfo = seckillFeignService.getSkuSeckilInfo(skuId);
            if (skuSeckilInfo.getCode() == 0) {
                //查询成功
                SeckillSkuVo seckilInfoData = skuSeckilInfo.getData("data", new TypeReference<SeckillSkuVo>() {
                });
                skuItemVo.setSeckillSkuVo(seckilInfoData);

                if (seckilInfoData != null) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime > seckilInfoData.getEndTime()) {
                        skuItemVo.setSeckillSkuVo(null);
                    }
                }
            }
        }, threadPoolExecutor);

        CompletableFuture.allOf(skuImagesFuture, skuItemSaleFuture, spuInfoFuture, spuItemAttrFuture, seckillFuture).get();
        return skuItemVo;
    }

}