package com.atguigu.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * sku销售属性&值
 * 
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 20:12:53
 */
@Data
@TableName("pms_sku_sale_attr_value")
public class PmsSkuSaleAttrValueEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private String id;
	/**
	 * sku_id
	 */
	private String skuId;
	/**
	 * attr_id
	 */
	private String attrId;
	/**
	 * 销售属性名
	 */
	private String attrName;
	/**
	 * 销售属性值
	 */
	private String attrValue;
	/**
	 * 顺序
	 */
	private Integer attrSort;

}
