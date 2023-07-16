package com.atguigu.gulimall.product.app;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.gulimall.product.entity.PmsSkuImagesEntity;
import com.atguigu.gulimall.product.service.PmsSkuImagesService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.R;



/**
 * sku图片
 *
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 20:12:53
 */
@RestController
@RequestMapping("product/pmsskuimages")
public class PmsSkuImagesController {
    @Autowired
    private PmsSkuImagesService pmsSkuImagesService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:pmsskuimages:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pmsSkuImagesService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:pmsskuimages:info")
    public R info(@PathVariable("id") Long id){
		PmsSkuImagesEntity pmsSkuImages = pmsSkuImagesService.getById(id);

        return R.ok().put("pmsSkuImages", pmsSkuImages);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:pmsskuimages:save")
    public R save(@RequestBody PmsSkuImagesEntity pmsSkuImages){
		pmsSkuImagesService.save(pmsSkuImages);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:pmsskuimages:update")
    public R update(@RequestBody PmsSkuImagesEntity pmsSkuImages){
		pmsSkuImagesService.updateById(pmsSkuImages);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:pmsskuimages:delete")
    public R delete(@RequestBody Long[] ids){
		pmsSkuImagesService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
