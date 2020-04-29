package com.fbw.OneBoot.service;

import com.fbw.OneBoot.dto.CommentDTO;
import com.fbw.OneBoot.enums.NotificationEnum;
import com.fbw.OneBoot.enums.NotificationStatusEnum;
import com.fbw.OneBoot.enums.TypeEnum;
import com.fbw.OneBoot.exception.CustomizeException;
import com.fbw.OneBoot.exception.ErrorCodeImpl;
import com.fbw.OneBoot.mapper.*;
import com.fbw.OneBoot.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentEtxMapper commentEtxMapper;
   @Autowired
   private NotificationMapper notificationMapper;
    @Transactional
    public void insert(Comment comment, User commentor) {
    if(comment.getParentId()==null||comment.getParentId()==0){
        throw new CustomizeException(ErrorCodeImpl.TARGET_PARAM_NOT_FOUND);
    }
    if(comment.getType()==null|| !TypeEnum.isExist(comment.getType())){
        throw new CustomizeException(ErrorCodeImpl.TARGET_PARAM_NOT_FOUND);
    }
    if(comment.getType()==TypeEnum.COMMENT.getType()){
        Comment dbComment=commentMapper.selectByPrimaryKey(comment.getParentId());
        if(dbComment==null){
            throw new CustomizeException(ErrorCodeImpl.COMMENT_NOT_FOUND);
        }
        Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
        if(question==null){
            throw new CustomizeException(ErrorCodeImpl.QUESTION_NOT_FOUND);
        }
        commentMapper.insert(comment);
        Comment parComment=new Comment();
        parComment.setId(comment.getParentId());
        parComment.setCommentCount(1);
        commentEtxMapper.commentCount(parComment);
createNotify(comment,dbComment.getCommentator(),commentor.getName(), question.getTitle(), NotificationEnum.REPLY_COMMENT,question.getId());

    }else{
        Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
   if(question==null){
       throw new CustomizeException(ErrorCodeImpl.QUESTION_NOT_FOUND);
   }
   comment.setCommentCount(0);
   commentMapper.insert(comment);
   question.setCommentCount(1);
   questionExtMapper.commentCount(question);
createNotify(comment,question.getCreator(),commentor.getName(),question.getTitle(), NotificationEnum.REPLY_QUESTION,question.getId());
    }
    }
private  void  createNotify(Comment comment, Long receiver, String notifyName, String outerTitle, NotificationEnum notificationEnum,Long outId){
    if(receiver==comment.getCommentator()){
        return;
    }
        Notification notification=new Notification();
    notification.setGmtCreate(System.currentTimeMillis());
    notification.setType(notificationEnum.getType());

    notification.setOutid(outId);
    notification.setNotifier(comment.getCommentator());
    notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
    notification.setReceiver(receiver);
    notification.setNotifierName(notifyName);
    notification.setOuterTitle(outerTitle);
    notificationMapper.insert(notification);
    }
    public List<CommentDTO> queryById(long id, TypeEnum type) {
        CommentExample commentExampl=new CommentExample();
        commentExampl.createCriteria().andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExampl.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExampl);
  if(comments.size()==0){
      return new ArrayList<>();
  }
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
       List<Long> userId=new ArrayList<>();
       userId.addAll(commentators);
        UserExample userExample=new UserExample();
        userExample.createCriteria()
                .andIdIn(userId);
        List<User> users=userMapper.selectByExample(userExample);
        Map<Long,User> userMap =  users.stream().collect(Collectors.toMap(user->user.getId(), user->user));
        List<CommentDTO> commentDTOList = comments.stream().map((comment) ->{
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOList;
    }

    public boolean likeCount(Long id,Long qid,Long uid){
       Comment comment= commentMapper.selectByPrimaryKey(id);
    Question question=questionMapper.selectByPrimaryKey(qid);
        if(comment!=null&&comment.getCommentator()==uid){
         return false;
    } comment.setLikeCount(1L);
         commentEtxMapper.likeCountComment(comment);
        return true;
    }
}
