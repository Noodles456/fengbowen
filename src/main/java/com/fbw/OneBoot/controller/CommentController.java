package com.fbw.OneBoot.controller;

import com.fbw.OneBoot.dto.CommentCreateDTO;
import com.fbw.OneBoot.dto.CommentDTO;
import com.fbw.OneBoot.dto.LikeCountDTO;
import com.fbw.OneBoot.dto.ResultDTO;
import com.fbw.OneBoot.enums.TypeEnum;
import com.fbw.OneBoot.exception.ErrorCodeImpl;
import com.fbw.OneBoot.model.Comment;
import com.fbw.OneBoot.model.User;
import com.fbw.OneBoot.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @ResponseBody
@RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object commentPost(@RequestBody CommentCreateDTO commentCreateDTO,
                              HttpServletRequest request){
        User user=(User) request.getSession().getAttribute("user");
        if(user==null){
            return ResultDTO.errorOf(ErrorCodeImpl.NO_LOG);
        }
        if(commentCreateDTO==null|| StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(ErrorCodeImpl.COMMENT_IS_EPTMY);
        }
    Comment comment = new Comment();
    comment.setParentId(commentCreateDTO.getParentId());
    comment.setContent(commentCreateDTO.getContent());
    comment.setType(commentCreateDTO.getType());
    comment.setGmtModified(System.currentTimeMillis());
    comment.setGmtCreate(System.currentTimeMillis());
    comment.setCommentator(user.getId());
    comment.setLikeCount(0l);
    commentService.insert(comment,user);
        return ResultDTO.okOf();
}
    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)

public ResultDTO comments(@PathVariable(name = "id") Long id){
       List<CommentDTO>  commentDTOS= commentService.queryById(id, TypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
}

@ResponseBody
@RequestMapping(value = "/like",method = RequestMethod.POST)
    public Object likeCount(@RequestBody LikeCountDTO likeCountDTO,
                            HttpServletRequest request){

    User user=(User) request.getSession().getAttribute("user");
    if(user==null){
        return ResultDTO.errorOf(ErrorCodeImpl.NO_LOG);
    }
    Long id=likeCountDTO.getId();
    Long qid=likeCountDTO.getQid();
    Long uid=likeCountDTO.getUid();
    boolean bol = commentService.likeCount(id, qid, uid);
if(bol){
    return ResultDTO.okOf();
}
    return ResultDTO.errorOf(ErrorCodeImpl.LIKE_FALSE);
    }

}
