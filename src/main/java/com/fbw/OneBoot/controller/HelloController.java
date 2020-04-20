package com.fbw.OneBoot.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fbw.OneBoot.dto.PagDTO;
import com.fbw.OneBoot.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HelloController {

  @Autowired
  private QuestionService questionService;
    @GetMapping("/")

    public String index(
            Model model,
            @RequestParam(name = "page",defaultValue = "1") Integer page,
            @RequestParam(name = "size",defaultValue = "5") Integer size,
            @RequestParam(name = "search",required = false) String search){

        PagDTO pagList=questionService.queryQuestion(search,page,size);
       model.addAttribute("pagList",pagList);
       model.addAttribute("search",search);
       return "index";
    }
    @GetMapping("/{action}")
    public String zeroComment(
            Model model,
   @PathVariable(name = "action") String action,
            @RequestParam(name = "page",defaultValue = "1") Integer page,
            @RequestParam(name = "size",defaultValue = "5") Integer size){
if(action.equals("zero")) {
    PagDTO pagList = questionService.queryByComment(page, size);
    model.addAttribute("pagList", pagList);
    return "index";
}
return "index";
}
}
