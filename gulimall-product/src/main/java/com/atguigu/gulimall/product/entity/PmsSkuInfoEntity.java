package com.atguigu.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * sku信息
 * 
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 20:12:53
 */
@Data
@TableName("pms_sku_info")
public class PmsSkuInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * skuId
	 */
	@TableId
	private String skuId;
	/**
	 * spuId
	 */
	private String spuId;
	/**
	 * sku名称
	 */
	private String skuName;
	/**
	 * sku介绍描述
	 */
	private String skuDesc;
	/**
	 * 所属分类id
	 */
	private String catalogId;
	/**
	 * 品牌id
	 */
	private String brandId;
	/**
	 * 默认图片
	 */
	private String skuDefaultImg;
	/**
	 * 标题
	 */
	private String skuTitle;
	/**
	 * 副标题
	 */
	private String skuSubtitle;
	/**
	 * 价格
	 */
	private BigDecimal price;
	/**
	 * 销量
	 */
	private Long saleCount;

}
