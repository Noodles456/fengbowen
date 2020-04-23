package com.fbw.OneBoot.schedule;

import com.fbw.OneBoot.cache.HotQuestionCache;
import com.fbw.OneBoot.mapper.QuestionMapper;
import com.fbw.OneBoot.model.Question;
import com.fbw.OneBoot.model.QuestionExample;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component

public class HotQuestion {
@Autowired
private QuestionMapper questionMapper;
@Autowired
private HotQuestionCache hotQuestionCache;
@Scheduled(fixedRate = 1000 * 60 * 60 * 3)
public  void hotQuestionSchedule(){
    int offsize=0;
    int limit=20;
    List<Question> list=new ArrayList<>();
    Map<String,Integer> priorities=new HashMap<>();
while (offsize==0||list.size()==limit){
    list=questionMapper.selectByExampleWithRowbounds(new QuestionExample(),new RowBounds(offsize,limit));
   for(Question question:list){
       String[] tags= StringUtils.split(question.getTag(),",");
       for(String tag:tags){
           Integer priority=priorities.get(tag);
           if(priority!=null){
               priorities.put(tag,priority+5+question.getCommentCount()+question.getViewCount());
           }
           else{
               priorities.put(tag,5+question.getCommentCount()+question.getViewCount());
           }
       }

   }
    offsize+=limit;
}
    hotQuestionCache.updateTags(priorities);
}
}
