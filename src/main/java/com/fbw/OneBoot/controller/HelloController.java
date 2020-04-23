package com.fbw.OneBoot.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fbw.OneBoot.cache.HotQuestionCache;
import com.fbw.OneBoot.dto.PagDTO;
import com.fbw.OneBoot.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class HelloController {

  @Autowired
  private QuestionService questionService;
  @Autowired
  private HotQuestionCache questionCache;
  @GetMapping("/")
    public String index(
            Model model,
            @RequestParam(name = "page",defaultValue = "1") Integer page,
            @RequestParam(name = "size",defaultValue = "5") Integer size,
            @RequestParam(name = "search",required = false) String search,
            @RequestParam(name="zero",required = false) String zero,
            @RequestParam(name = "hot",required = false) String hot) {


          PagDTO pagList = questionService.queryQuestion(search,zero,hot,page, size);
      Integer countHot = questionService.countByHot(hot);
      List<String> hots = questionCache.getHots();
      model.addAttribute("pagList", pagList);
            model.addAttribute("search", search);
            model.addAttribute("zero",zero);
            model.addAttribute("hots",hots);
            model.addAttribute("countHot",countHot);
            return "index";

    }

}
