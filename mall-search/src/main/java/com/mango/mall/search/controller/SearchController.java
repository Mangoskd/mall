package com.mango.mall.search.controller;

import com.mango.mall.search.service.MallSearchService;
import com.mango.mall.search.vo.SearchParm;
import com.mango.mall.search.vo.SearchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * @Author Mango
 * @Date 2022/4/2 23:18
 */
@Controller
public class SearchController {

    @Resource
    MallSearchService mallSearchService;
    @GetMapping("/list.html")
    public String listPage(SearchParm searchParm, Model model){
       SearchResult result = mallSearchService.search(searchParm);
       model.addAttribute("result",result);
        return "list";
    }
}
