package com.mango.mall.product.web;

import com.mango.mall.product.entity.CategoryEntity;
import com.mango.mall.product.service.CategoryService;
import com.mango.mall.product.vo.Catelog2Vo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author Mango
 * @Date 2022/3/23 22:36
 */
@Controller
public class IndexController {
    @Resource
    CategoryService categoryService;

    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){
        List<CategoryEntity> categoryEntityList = categoryService.getLevelCategorys();

        model.addAttribute("categorys",categoryEntityList);
        return "index";
    }

    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String,List<Catelog2Vo>> getCatalogJson(){
      Map<String,List<Catelog2Vo>> map = categoryService.getCatalogJson();
        return map;
    }
    
}
