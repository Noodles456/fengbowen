package com.fbw.OneBoot.controller;

import com.fbw.OneBoot.dto.CommentDTO;
import com.fbw.OneBoot.dto.QuestionDTO;
import com.fbw.OneBoot.enums.TypeEnum;
import com.fbw.OneBoot.model.Question;
import com.fbw.OneBoot.service.CommentService;
import com.fbw.OneBoot.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String Question(@PathVariable(name = "id") Integer id,
                           Model model) {
        QuestionDTO questionDTO = questionService.queryById(id);
        List<CommentDTO> commentDTOList = commentService.queryById(id, TypeEnum.QUESTION);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        questionService.inView(id);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", commentDTOList);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }

}
