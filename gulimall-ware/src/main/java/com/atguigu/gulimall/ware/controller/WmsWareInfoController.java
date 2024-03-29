package com.atguigu.gulimall.ware.controller;

import java.util.Arrays;
import java.util.Map;

import com.atguigu.gulimall.ware.vo.FareVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.ware.entity.WmsWareInfoEntity;
import com.atguigu.gulimall.ware.service.WmsWareInfoService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.R;



/**
 * 仓库信息
 *
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 20:14:51
 */
@RestController
@RequestMapping("ware/wmswareinfo")
public class WmsWareInfoController {
    @Autowired
    private WmsWareInfoService wmsWareInfoService;

    /**
     * 获取运费信息
     * @return
     */
    @GetMapping(value = "/fare")
    public R getFare(@RequestParam("addrId") String addrId) {

        FareVo fare = wmsWareInfoService.getFare(addrId);
        R ok = R.ok();
        ok.setData(fare);
        return ok;
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:wmswareinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wmsWareInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:wmswareinfo:info")
    public R info(@PathVariable("id") Long id){
		WmsWareInfoEntity wmsWareInfo = wmsWareInfoService.getById(id);

        return R.ok().put("wmsWareInfo", wmsWareInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:wmswareinfo:save")
    public R save(@RequestBody WmsWareInfoEntity wmsWareInfo){
		wmsWareInfoService.save(wmsWareInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:wmswareinfo:update")
    public R update(@RequestBody WmsWareInfoEntity wmsWareInfo){
		wmsWareInfoService.updateById(wmsWareInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:wmswareinfo:delete")
    public R delete(@RequestBody Long[] ids){
		wmsWareInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
