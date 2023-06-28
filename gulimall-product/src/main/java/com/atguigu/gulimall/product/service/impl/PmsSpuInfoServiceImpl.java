package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.common.to.SkuReductionTo;
import com.atguigu.gulimall.common.to.SpuBoundsTo;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.product.dao.PmsAttrDao;
import com.atguigu.gulimall.product.dao.PmsProductAttrValueDao;
import com.atguigu.gulimall.product.entity.*;
import com.atguigu.gulimall.product.feign.CouponFeignService;
import com.atguigu.gulimall.product.service.*;
import com.atguigu.gulimall.product.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.PmsSpuInfoDao;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsSpuInfoService")
public class PmsSpuInfoServiceImpl extends ServiceImpl<PmsSpuInfoDao, PmsSpuInfoEntity> implements PmsSpuInfoService {

    @Autowired
    private PmsSpuInfoDescService spuInfoDescService;
    @Autowired
    private PmsSpuImagesService pmsSpuImagesService;
    @Autowired
    private PmsProductAttrValueService pmsProductAttrValueService;
    @Autowired
    private PmsSkuInfoService skuInfoService;
    @Autowired
    private PmsSkuImagesService skuImagesService;
    @Autowired
    private PmsSkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private CouponFeignService couponFeignService;
    @Autowired
    private PmsAttrDao pmsAttrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsSpuInfoEntity> page = this.page(
                new Query<PmsSpuInfoEntity>().getPage(params),
                new QueryWrapper<PmsSpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSpuInfo(SpuSaveVo spuSaveVo) {
        // 1、保存spu基本信息  pms_spu_info
        PmsSpuInfoEntity pmsSpuInfoEntity = new PmsSpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo, pmsSpuInfoEntity);
        pmsSpuInfoEntity.setCreateTime(new Date());
        pmsSpuInfoEntity.setUpdateTime(new Date());
        this.save(pmsSpuInfoEntity);
        // 2、保存spu的描述图片信息 pms_spu_info_desc
        List<String> decript = spuSaveVo.getDecript();
        PmsSpuInfoDescEntity pmsSpuInfoDescEntity = new PmsSpuInfoDescEntity();
        pmsSpuInfoDescEntity.setSpuId(pmsSpuInfoEntity.getId());
        pmsSpuInfoDescEntity.setDecript(String.join(",",decript));
        spuInfoDescService.saveSpuDesc(pmsSpuInfoDescEntity);
        // 3、保存spu的图片集 pms_spu_images
        List<String> images = spuSaveVo.getImages();
        pmsSpuImagesService.saveImages(pmsSpuInfoEntity.getId(), images);
        // 4、保存spu的规格参数 pms_product_attr_value
        List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();
        List<PmsProductAttrValueEntity> collect = baseAttrs.stream().map(item -> {
            PmsProductAttrValueEntity pmsProductAttrValueEntity = new PmsProductAttrValueEntity();
            pmsProductAttrValueEntity.setSpuId(pmsSpuInfoEntity.getId());
            pmsProductAttrValueEntity.setAttrId(item.getAttrId());
            PmsAttrEntity pmsAttrEntity = pmsAttrDao.selectById(item.getAttrId());
            pmsProductAttrValueEntity.setAttrName(pmsAttrEntity.getAttrName());
            pmsProductAttrValueEntity.setAttrValue(item.getAttrValues());
            pmsProductAttrValueEntity.setQuickShow(item.getShowDesc());
            return pmsProductAttrValueEntity;
        }).collect(Collectors.toList());
        pmsProductAttrValueService.saveBatch(collect);
        // 5、保存spu的积分信息 gulimall-sms sms_spu_bounds
        Bounds bounds = spuSaveVo.getBounds();
        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
        BeanUtils.copyProperties(bounds, spuBoundsTo);
        spuBoundsTo.setSpuId(pmsSpuInfoEntity.getId());
        R couponSave = couponFeignService.save(spuBoundsTo);
        if (couponSave.getCode() != 0){
            log.error("spu积分信息保存失败");
        }
        // 6、保存当前spu对应所有的sku信息
        List<Skus> skus = spuSaveVo.getSkus();
        if (skus != null && skus.size() > 0){
            skus.forEach(sku -> {
                // 6.1 保存sku的基本信息 pms_sku_info
                List<Images> skuImages = sku.getImages();
                String defaultImg = "";
                for (Images img: skuImages) {
                    if (img.getDefaultImg() == 1){
                        defaultImg = img.getImgUrl();
                        break;
                    }
                }
                PmsSkuInfoEntity pmsSkuInfoEntity = new PmsSkuInfoEntity();
                BeanUtils.copyProperties(sku, pmsSkuInfoEntity);
                pmsSkuInfoEntity.setSpuId(pmsSpuInfoEntity.getId());
                pmsSkuInfoEntity.setCatalogId(pmsSpuInfoEntity.getCatalogId());
                pmsSkuInfoEntity.setBrandId(pmsSpuInfoEntity.getBrandId());
                pmsSkuInfoEntity.setSkuDefaultImg(defaultImg);
                pmsSkuInfoEntity.setSaleCount(0l);
                skuInfoService.save(pmsSkuInfoEntity);
                // 6.2 保存sku的图片信息 pms_sku_images
                List<PmsSkuImagesEntity> skuImagesEntityList = skuImages.stream().map(skuImg -> {
                    PmsSkuImagesEntity pmsSkuImagesEntity = new PmsSkuImagesEntity();
                    BeanUtils.copyProperties(skuImg, pmsSkuImagesEntity);
                    pmsSkuImagesEntity.setSkuId(pmsSkuInfoEntity.getSkuId());
                    return pmsSkuImagesEntity;
                }).filter(item -> StringUtils.isNotBlank(item.getImgUrl())).collect(Collectors.toList());
                skuImagesService.saveBatch(skuImagesEntityList);
                // 6.3 保存sku的销售属性
                List<Attr> attr = sku.getAttr();
                List<PmsSkuSaleAttrValueEntity> attrValueEntities = attr.stream().map(a -> {
                    PmsSkuSaleAttrValueEntity pmsSkuSaleAttrValueEntity = new PmsSkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, pmsSkuSaleAttrValueEntity);
                    pmsSkuSaleAttrValueEntity.setSkuId(pmsSkuInfoEntity.getSkuId());
                    return pmsSkuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(attrValueEntities);
                // 6.4 保存sku的优惠、满减等信息
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(sku, skuReductionTo);
                skuReductionTo.setSkuId(pmsSkuInfoEntity.getSkuId());
                R skuSaveinfo = couponFeignService.saveinfo(skuReductionTo);
                if (skuSaveinfo.getCode() != 0){
                    log.error("sku优惠信息保存失败");
                }
            });
        }
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<PmsSpuInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)){
            wrapper.and(item -> item.eq("id", key).or().like("spu_name", key));
        }
        String status = (String) params.get("status");
        if (StringUtils.isNotBlank(status)){
            wrapper.eq("publish_status", status);
        }
        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotBlank(brandId) && !"0".equals(brandId)){
            wrapper.eq("brand_id", brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (StringUtils.isNotBlank(catelogId) && !"0".equals(catelogId)){
            wrapper.eq("catalog_id", catelogId);
        }
        IPage<PmsSpuInfoEntity> page = this.page(
                new Query<PmsSpuInfoEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

}