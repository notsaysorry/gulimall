package com.atguigu.gulimall.product.app;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.gulimall.product.entity.PmsCommentReplayEntity;
import com.atguigu.gulimall.product.service.PmsCommentReplayService;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.R;



/**
 * 商品评价回复关系
 *
 * @author liudong
 * @email 1062659083@qq.com
 * @date 2023-05-28 20:12:53
 */
@RestController
@RequestMapping("product/pmscommentreplay")
public class PmsCommentReplayController {
    @Autowired
    private PmsCommentReplayService pmsCommentReplayService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:pmscommentreplay:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pmsCommentReplayService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:pmscommentreplay:info")
    public R info(@PathVariable("id") Long id){
		PmsCommentReplayEntity pmsCommentReplay = pmsCommentReplayService.getById(id);

        return R.ok().put("pmsCommentReplay", pmsCommentReplay);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:pmscommentreplay:save")
    public R save(@RequestBody PmsCommentReplayEntity pmsCommentReplay){
		pmsCommentReplayService.save(pmsCommentReplay);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:pmscommentreplay:update")
    public R update(@RequestBody PmsCommentReplayEntity pmsCommentReplay){
		pmsCommentReplayService.updateById(pmsCommentReplay);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:pmscommentreplay:delete")
    public R delete(@RequestBody Long[] ids){
		pmsCommentReplayService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
