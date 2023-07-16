package com.atguigu.gulimall.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.atguigu.gulimall.product.entity.PmsBrandEntity;
import com.atguigu.gulimall.product.vo.PmsBrandVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.product.entity.PmsCategoryBrandRelationEntity;
import com.atguigu.gulimall.product.service.PmsCategoryBrandRelationService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 20:12:53
 */
@RestController
@RequestMapping("product/pmscategorybrandrelation")
public class PmsCategoryBrandRelationController {
    @Autowired
    private PmsCategoryBrandRelationService pmsCategoryBrandRelationService;

    /**
     * 获得品牌对应的三级分类
     * @param brandId
     * @return
     */
    @GetMapping("/catelog/list")
    public R catelogList(@RequestParam("brandId") String brandId) {
        List<PmsCategoryBrandRelationEntity> data = pmsCategoryBrandRelationService
                .list(new QueryWrapper<PmsCategoryBrandRelationEntity>().eq("brand_id", brandId));
        return R.ok().put("data", data);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:pmscategorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pmsCategoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 查询当前分类下所有的分类
     */
    @RequestMapping("/brands/list")
    public R list(@RequestParam(value = "catId", required = true) String catId){
        List<PmsBrandEntity> brandEntities = pmsCategoryBrandRelationService.queryBrands(catId);
        List<PmsBrandVo> brandVos = brandEntities.stream().map(item -> {
            PmsBrandVo pmsBrandVo = new PmsBrandVo();
            pmsBrandVo.setBrandId(item.getBrandId());
            pmsBrandVo.setBrandName(item.getName());
            return pmsBrandVo;
        }).collect(Collectors.toList());
        return R.ok().put("data", brandVos);
    }



    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:pmscategorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
		PmsCategoryBrandRelationEntity pmsCategoryBrandRelation = pmsCategoryBrandRelationService.getById(id);

        return R.ok().put("pmsCategoryBrandRelation", pmsCategoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:pmscategorybrandrelation:save")
    public R save(@RequestBody PmsCategoryBrandRelationEntity pmsCategoryBrandRelation){
		pmsCategoryBrandRelationService.saveDetail(pmsCategoryBrandRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:pmscategorybrandrelation:update")
    public R update(@RequestBody PmsCategoryBrandRelationEntity pmsCategoryBrandRelation){
		pmsCategoryBrandRelationService.updateById(pmsCategoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:pmscategorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids){
		pmsCategoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
