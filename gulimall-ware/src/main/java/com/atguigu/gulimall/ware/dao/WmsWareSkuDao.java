package com.atguigu.gulimall.ware.dao;

import com.atguigu.gulimall.ware.entity.WmsWareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 * 
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 20:14:51
 */
@Mapper
public interface WmsWareSkuDao extends BaseMapper<WmsWareSkuEntity> {

    void updateStock(String skuId, Integer skuNum, String wareId);

    Long queryStock(String skuId);

    List<String> listWareIdHasSkuStock(String skuId);

    Long lockSkuStock(@Param("skuId") String skuId, @Param("wareId") String wareId, @Param("num") Integer num);

    void unLockStock(@Param("skuId") String skuId, @Param("wareId") String wareId, @Param("num") Integer num);
}
