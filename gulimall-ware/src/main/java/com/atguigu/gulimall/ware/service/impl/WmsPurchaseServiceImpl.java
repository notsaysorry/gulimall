package com.atguigu.gulimall.ware.service.impl;

import com.atguigu.gulimall.common.constant.WareConstant;
import com.atguigu.gulimall.ware.entity.WmsPurchaseDetailEntity;
import com.atguigu.gulimall.ware.service.WmsPurchaseDetailService;
import com.atguigu.gulimall.ware.service.WmsWareSkuService;
import com.atguigu.gulimall.ware.vo.MergeVo;
import com.atguigu.gulimall.ware.vo.PurchaseDoneVo;
import com.atguigu.gulimall.ware.vo.PurchaseItemVo;
import org.apache.commons.lang3.StringUtils;
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

import com.atguigu.gulimall.ware.dao.WmsPurchaseDao;
import com.atguigu.gulimall.ware.entity.WmsPurchaseEntity;
import com.atguigu.gulimall.ware.service.WmsPurchaseService;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;


@Service("wmsPurchaseService")
public class WmsPurchaseServiceImpl extends ServiceImpl<WmsPurchaseDao, WmsPurchaseEntity> implements WmsPurchaseService {

    @Autowired
    private WmsPurchaseDetailService purchaseDetailService;
    @Autowired
    private WmsWareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WmsPurchaseEntity> page = this.page(
                new Query<WmsPurchaseEntity>().getPage(params),
                new QueryWrapper<WmsPurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils unreceiveList(Map<String, Object> params) {
        QueryWrapper<WmsPurchaseEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0).or().eq("status", 1);
        IPage<WmsPurchaseEntity> page = this.page(
                new Query<WmsPurchaseEntity>().getPage(params),
                wrapper

        );
        return new PageUtils(page);
    }

    @Override
    public void merge(MergeVo mergeVo) {
        String purchaseId = mergeVo.getPurchaseId();
        if (StringUtils.isBlank(purchaseId)){
            WmsPurchaseEntity purchaseEntity = new WmsPurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }
        List<String> items = mergeVo.getItems();
        String finalPurchaseId = purchaseId;
        List<WmsPurchaseDetailEntity> collect = items.stream().map(item -> {
            WmsPurchaseDetailEntity purchaseDetailEntity = new WmsPurchaseDetailEntity();
            purchaseDetailEntity.setId(item);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.CREATED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.saveOrUpdateBatch(collect);
        WmsPurchaseEntity purchaseEntity = new WmsPurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }

    @Transactional
    @Override
    public void receive(List<String> ids) {
        // 确认这些采购单都是新建和已分配状态
        List<WmsPurchaseEntity> purchaseEntities = ids.stream().map(id -> this.baseMapper.selectById(id))
                .filter(item -> item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode()
                        || item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode())
                .collect(Collectors.toList());
        // 修改采购单的状态为已领取
        List<WmsPurchaseEntity> received = purchaseEntities.stream().map(item -> {
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVED.getCode());
            item.setUpdateTime(new Date());
            this.updateById(item);
            return item;
        }).collect(Collectors.toList());
        // 修改每个采购需求状态为正在购买
        received.forEach(item -> {
            String purchaseId = item.getId();
            List<WmsPurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService
                    .list(new QueryWrapper<WmsPurchaseDetailEntity>().eq("purchase_id", purchaseId));
            List<WmsPurchaseDetailEntity> collect = purchaseDetailEntities.stream().map(detail -> {
                detail.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return detail;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(collect);
        });
    }

    @Transactional
    @Override
    public void done(PurchaseDoneVo purchaseDoneVo) {
        // 修改每一个采购项状态
        List<PurchaseItemVo> items = purchaseDoneVo.getItems();
        boolean flag = true;
        for (PurchaseItemVo item: items) {
            if (item.getStatus() == WareConstant.PurchaseDetailStatusEnum.FAILED.getCode()){
                flag = false;
            }else if (item.getStatus() == WareConstant.PurchaseDetailStatusEnum.FINISHED.getCode()){
                // 给仓库增加商品库存
                WmsPurchaseDetailEntity byId = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(byId.getSkuId(), byId.getSkuNum(), byId.getWareId());
            }
            WmsPurchaseDetailEntity purchaseDetailEntity = new WmsPurchaseDetailEntity();
            purchaseDetailEntity.setId(item.getItemId());
            purchaseDetailEntity.setStatus(item.getStatus());
            purchaseDetailService.updateById(purchaseDetailEntity);
        }
        // 修改采购单状态
        String purchaseId = purchaseDoneVo.getId();
        WmsPurchaseEntity purchaseEntity = new WmsPurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        purchaseEntity.setStatus(flag ? WareConstant.PurchaseStatusEnum.FINISHED.getCode() : WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        this.baseMapper.updateById(purchaseEntity);
    }

}