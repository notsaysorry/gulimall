package com.atguigu.gulimall.product.entity;

import com.atguigu.gulimall.common.valid.AddGroup;
import com.atguigu.gulimall.common.valid.ListValue;
import com.atguigu.gulimall.common.valid.UpdateGroup;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 20:12:53
 */
@Data
@TableName("pms_brand")
public class PmsBrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	@Null(message="新增时不能传品牌id",groups={AddGroup.class})
	@NotBlank(message="修改时品牌id不能为空",groups={UpdateGroup.class})
	private String brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message="品牌名不能为空",groups={AddGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(message="品牌logo不能为空", groups={AddGroup.class})
	@URL(message = "logo地址必须是一个合法的url地址",groups={AddGroup.class,UpdateGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	@NotBlank(message="介绍不能为空", groups={AddGroup.class})
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(message = "显示状态不能为空", groups={AddGroup.class})
	@ListValue(values = {0, 1}, groups={AddGroup.class,UpdateGroup.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotBlank(message="首字母不能为空", groups={AddGroup.class})
	@Pattern(regexp="^[a-zA-Z]$", groups={AddGroup.class,UpdateGroup.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(message = "排序不能为空", groups={AddGroup.class})
	@Min(value = 0, groups={AddGroup.class,UpdateGroup.class})
	private Integer sort;

}
