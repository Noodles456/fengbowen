package com.fbw.OneBoot.mapper;

import com.fbw.OneBoot.dto.SearchDTO;
import com.fbw.OneBoot.model.Question;
import com.fbw.OneBoot.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
    int inCView(Question record);
    int commentCount(Question record);
   List<Question> selectRelated(Question question);

    Integer countBySearch(SearchDTO searchDTO);

    List<Question> selectBySearch(SearchDTO searchDTO);


    Integer countByHot(@Param("hot") String hot);
}