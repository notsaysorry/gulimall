package com.atguigu.gulimall.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.atguigu.gulimall.product.entity.PmsProductAttrValueEntity;
import com.atguigu.gulimall.product.service.PmsProductAttrValueService;
import com.atguigu.gulimall.product.vo.PmsAttrRespVo;
import com.atguigu.gulimall.product.vo.PmsAttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.gulimall.product.service.PmsAttrService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.R;



/**
 * 商品属性
 *
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 20:12:53
 */
@RestController
@RequestMapping("product/pmsattr")
public class PmsAttrController {
    @Autowired
    private PmsAttrService pmsAttrService;
    @Autowired
    private PmsProductAttrValueService productAttrValueService;

    @RequestMapping("/base/listforspu/{spuId}")
    //@RequiresPermissions("product:pmsattr:list")
    public R listForSpu(@PathVariable("spuId") String spuId){
        List<PmsProductAttrValueEntity> attrValueEntities = productAttrValueService.listForSpu(spuId);

        return R.ok().put("data", attrValueEntities);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:pmsattr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pmsAttrService.queryPage(params);

        return R.ok().put("page", page);
    }

    @RequestMapping("/base/list/{categoryId}")
    //@RequiresPermissions("product:pmsattr:list")
    public R baseList(@RequestParam Map<String, Object> params, @PathVariable("categoryId") String categoryId){
        PageUtils page = pmsAttrService.queryBasePage(params, categoryId);

        return R.ok().put("page", page);
    }

    @RequestMapping("/sale/list/{categoryId}")
    //@RequiresPermissions("product:pmsattr:list")
    public R saleList(@RequestParam Map<String, Object> params, @PathVariable("categoryId") String categoryId){
        PageUtils page = pmsAttrService.querySalePage(params, categoryId);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:pmsattr:info")
    public R info(@PathVariable("attrId") Long attrId){
        PmsAttrRespVo attrInfo = pmsAttrService.getAttrInfo(attrId);
        return R.ok().put("attr", attrInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:pmsattr:save")
    public R save(@RequestBody PmsAttrVo pmsAttrVo){
		pmsAttrService.saveDetail(pmsAttrVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:pmsattr:update")
    public R update(@RequestBody PmsAttrVo pmsAttrVo){
        pmsAttrService.updateAttr(pmsAttrVo);
        return R.ok();
    }

    @RequestMapping("/update/{spuId}")
    //@RequiresPermissions("product:pmsattr:update")
    public R updateSpuAttr(@PathVariable("spuId") String spuId,  @RequestBody List<PmsProductAttrValueEntity> productAttrValueEntities){
        productAttrValueService.updateSpuAttr(spuId, productAttrValueEntities);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:pmsattr:delete")
    public R delete(@RequestBody Long[] attrIds){
		pmsAttrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
