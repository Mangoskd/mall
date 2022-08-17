package com.mango.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.mango.mall.product.entity.AttrEntity;
import com.mango.mall.product.service.AttrAttrgroupRelationService;
import com.mango.mall.product.service.AttrService;
import com.mango.mall.product.service.CategoryService;
import com.mango.mall.product.vo.AttrGroupRelationVo;
import com.mango.mall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mango.mall.product.entity.AttrGroupEntity;
import com.mango.mall.product.service.AttrGroupService;
import com.mango.common.utils.PageUtils;
import com.mango.common.utils.R;

import javax.annotation.Resource;


/**
 * 属性分组
 *
 * @author Mango
 * @email mango@mango.com
 * @date 2022-03-03 17:28:47
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;


    @Resource
    private CategoryService categoryService;

    @Resource
    AttrService attrService;
    @Resource
    AttrAttrgroupRelationService attrAttrgroupRelationService;

    //product/attrgroup/{catelogId}/withattr
    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId") Long catelogId){
      List<AttrGroupWithAttrsVo> list = attrGroupService.getAttrGroupWithAttrsBycatelogId(catelogId);
        return R.ok().put("data",list);
    }

    //product/attrgroup/attr/relation
    @PostMapping("/attr/relation")
    public R attrRelation(@RequestBody List<AttrGroupRelationVo> relationVos){
        attrAttrgroupRelationService.saveBatch(relationVos);

        return R.ok();
    }

    //product/attrgroup/{attrgroupId}/attr/relation
    @GetMapping("/{attrgroupId}/attr/relation")
    public  R attrRelation(@PathVariable("attrgroupId") Long attrgroupId){
        List<AttrEntity> entityList = attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data",entityList);
    }
    ///product/attrgroup/{attrgroupId}/noattr/relation
    @GetMapping("/{attrgroupId}/noattr/relation")
    public  R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId,
                             @RequestParam Map<String,Object> params){
        PageUtils page = attrService.getNoRelationAttr(params,attrgroupId);
        return R.ok().put("page",page);
    }


    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId) {
        PageUtils page = attrGroupService.queryPage(params,catelogId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        Long catelogId = attrGroup.getCatelogId();
       Long[] path = categoryService.findCatelogPath(catelogId);


        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }
    ///product/attrgroup/attr/relation/delete
    //[{"attrId":1,"attrGroupId":2}]
    @PostMapping("/attr/relation/delete")
    public R attrRelationDelete(@RequestBody AttrGroupRelationVo[] vos){
        attrService.deleteRelation(vos);
        return R.ok();
    }

}
