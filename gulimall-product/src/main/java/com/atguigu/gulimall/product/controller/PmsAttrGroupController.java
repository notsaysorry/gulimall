package com.atguigu.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.atguigu.gulimall.product.entity.PmsAttrEntity;
import com.atguigu.gulimall.product.service.PmsAttrAttrgroupRelationService;
import com.atguigu.gulimall.product.service.PmsAttrService;
import com.atguigu.gulimall.product.service.PmsCategoryService;
import com.atguigu.gulimall.product.vo.PmsAttrGroupRelationVo;
import com.atguigu.gulimall.product.vo.PmsAttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.product.entity.PmsAttrGroupEntity;
import com.atguigu.gulimall.product.service.PmsAttrGroupService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.R;



/**
 * 属性分组
 *
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 20:12:53
 */
@RestController
@RequestMapping("product/pmsattrgroup")
public class PmsAttrGroupController {
    @Autowired
    private PmsAttrGroupService pmsAttrGroupService;
    @Autowired
    private PmsCategoryService pmsCategoryService;
    @Autowired
    private PmsAttrService pmsAttrService;
    @Autowired
    private PmsAttrAttrgroupRelationService pmsAttrAttrgroupRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list/{catalogId}")
    //@RequiresPermissions("product:pmsattrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catalogId") String catalogId){
//        PageUtils page = pmsAttrGroupService.queryPage(params);
        PageUtils page = pmsAttrGroupService.queryPage(params, catalogId);
        return R.ok().put("page", page);
    }


    @RequestMapping("/attr/relation")
    public R attrRelation(@RequestBody List<PmsAttrGroupRelationVo> pmsAttrGroupRelationVos){
        pmsAttrAttrgroupRelationService.addRelation(pmsAttrGroupRelationVos);
        return R.ok();
    }


    @RequestMapping("/{catId}/withattr")
    public R attrGroupWithAttrs(@PathVariable("catId") String catId){
        List<PmsAttrGroupWithAttrsVo> attrGroupWithAttrsVoList = pmsAttrGroupService.attrGroupWithAttrs(catId);
        return R.ok().put("data", attrGroupWithAttrsVoList);
    }

    /**
     * 获取分组所有的属性关联关系
     * @param groupId
     * @return
     */
    @RequestMapping("/{groupId}/attr/relation")
    public R attrRelation(@PathVariable("groupId") String groupId){
        List<PmsAttrEntity> pmsAttrEntities = pmsAttrService.attrRelation(groupId);
        return R.ok().put("data", pmsAttrEntities);
    }


    /**
     * 获取不属于当前分组所有的属性关联关系
     * @param groupId
     * @return
     */
    @RequestMapping("/{groupId}/noattr/relation")
    public R attrNoRelation(@RequestParam Map<String, Object> params, @PathVariable("groupId") String groupId){
        PageUtils pageUtils = pmsAttrService.attrNoRelation(params, groupId);
        return R.ok().put("page", pageUtils);
    }

    @PostMapping("/attr/relation/delete")
    public R deleteAttrRelation(@RequestBody PmsAttrGroupRelationVo[] pmsAttrGroupRelationVos){
        pmsAttrGroupService.deleteAttrRelation(pmsAttrGroupRelationVos);
        return R.ok();
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:pmsattrgroup:info")
    public R info(@PathVariable("attrGroupId") String attrGroupId){
		PmsAttrGroupEntity pmsAttrGroup = pmsAttrGroupService.getById(attrGroupId);
        String[] catelogs = pmsCategoryService.queryCatelogs(pmsAttrGroup.getCatelogId());
        pmsAttrGroup.setCatelogIds(catelogs);
        return R.ok().put("pmsAttrGroup", pmsAttrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:pmsattrgroup:save")
    public R save(@RequestBody PmsAttrGroupEntity pmsAttrGroup){
		pmsAttrGroupService.save(pmsAttrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:pmsattrgroup:update")
    public R update(@RequestBody PmsAttrGroupEntity pmsAttrGroup){
		pmsAttrGroupService.updateById(pmsAttrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:pmsattrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		pmsAttrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
