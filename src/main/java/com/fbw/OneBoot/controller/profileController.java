package com.fbw.OneBoot.controller;

import com.fbw.OneBoot.dto.PagDTO;
import com.fbw.OneBoot.mapper.QuestionMapper;
import com.fbw.OneBoot.model.User;
import com.fbw.OneBoot.service.NotificationService;
import com.fbw.OneBoot.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class profileController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name="action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "5") Integer size){

User user=(User) request.getSession().getAttribute("user");
if(user==null){
    return "redirect:/";
}

        if("questions".equals(action)) {
        model.addAttribute("section","questions" );
        model.addAttribute("sectionName","我关注的问题");
            PagDTO pageList = questionService.list(user.getId(), page, size);
            model.addAttribute("pageList",pageList);

        }
    if("replies".equals(action)){
        PagDTO pageList= notificationService.list(user.getId(),page,size);

        model.addAttribute("pageList",pageList);
        model.addAttribute("section","replies" );

        model.addAttribute("sectionName","最新回复");

    }

        return "profile";
    }


}
