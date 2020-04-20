package com.fbw.OneBoot.controller;

import com.fbw.OneBoot.cache.TagCache;
import com.fbw.OneBoot.dto.QuestionDTO;
import com.fbw.OneBoot.dto.TagDTO;
import com.fbw.OneBoot.model.Question;
import com.fbw.OneBoot.model.User;
import com.fbw.OneBoot.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "id", required = false) Long id,
            HttpServletRequest request,
            Model model) {
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", TagCache.get());
        if (title == null || title == "") {
            model.addAttribute("error", "标题为空");
            return "publish";
        }
        if (description == null || description == "") {
            model.addAttribute("error", "问题补充为空");
            return "publish";
        }
        if (tag == null || tag == "") {
            model.addAttribute("error", "标签为空");

            return "publish";
        }
        String inv = TagCache.filterValid(tag);
        if (StringUtils.isNotBlank(inv)) {
            model.addAttribute("error", "输入了非法标签" + inv);
            return "publish";
        }
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);

        questionService.createUpdate(question);

        return "redirect:/";
    }

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") long id,
                       Model model) {
        QuestionDTO question = questionService.queryById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }
}
