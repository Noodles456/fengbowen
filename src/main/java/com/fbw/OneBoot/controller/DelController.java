package com.fbw.OneBoot.controller;

import com.fbw.OneBoot.dto.PagDTO;
import com.fbw.OneBoot.model.User;
import com.fbw.OneBoot.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DelController {
  @Autowired
  private QuestionService questionService;
    @GetMapping("/del/{id}")
    public String del(@PathVariable(name = "id") long id,
                      Model model) {

        boolean b = questionService.delByQuestionId(id);
        if (b) {
            model.addAttribute("del", "删除成功");
            return "redirect:/profile/questions";
        }
        return "redirect:/profile/questions";
    }
}
