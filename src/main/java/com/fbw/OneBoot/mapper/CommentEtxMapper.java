package com.fbw.OneBoot.mapper;

import com.fbw.OneBoot.model.Comment;
import com.fbw.OneBoot.model.CommentExample;
import com.fbw.OneBoot.model.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentEtxMapper {
    int commentCount(Comment comment);

    void likeCountComment(Comment comment);
}