package com.fbw.OneBoot.service;

import com.fbw.OneBoot.dto.PagDTO;
import com.fbw.OneBoot.dto.QuestionDTO;
import com.fbw.OneBoot.dto.SearchDTO;
import com.fbw.OneBoot.exception.CustomizeException;
import com.fbw.OneBoot.exception.ErrorCode;
import com.fbw.OneBoot.exception.ErrorCodeImpl;
import com.fbw.OneBoot.mapper.QuestionExtMapper;
import com.fbw.OneBoot.mapper.QuestionMapper;
import com.fbw.OneBoot.mapper.UserMapper;
import com.fbw.OneBoot.model.Question;
import com.fbw.OneBoot.model.QuestionExample;
import com.fbw.OneBoot.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
@Autowired
private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
@Autowired
private UserMapper userMapper;


    public PagDTO queryQuestion(String search,Integer page, Integer size) {
        if(StringUtils.isNotBlank(search)){
            String [] tags=StringUtils.split(search,",");
            search= Arrays.stream(tags).collect(Collectors.joining("|"));
        }

        PagDTO<QuestionDTO> pagDTO = new PagDTO<>();
       SearchDTO searchDTO =new SearchDTO();
       searchDTO.setSearch(search);
           Integer totalCount = questionExtMapper.countBySearch(searchDTO);
        pagDTO.setPagInation(totalCount,page,size);
        if(page<1){
            page=1;
        }if(page>pagDTO.getTotalPage()){
            page=pagDTO.getTotalPage();
        }
        if(size<1){
            size=1;
        }
        if(size>totalCount){
            size=totalCount;
        }

        Integer offsize = page < 1 ? 0 : size * (page - 1);
searchDTO.setPage(offsize);
searchDTO.setSize(size);
        List<Question> questionLists = questionExtMapper.selectBySearch(searchDTO);
        List<QuestionDTO> questionDTOList=new ArrayList<>();

        for(Question  questionList: questionLists){
        User user=userMapper.selectByPrimaryKey(questionList.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(questionList,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);

        }
        pagDTO.setData(questionDTOList);


        return pagDTO;
    }
    public PagDTO list(long id,Integer page, Integer size) {

        PagDTO<QuestionDTO> pagDTO = new PagDTO<>();

        QuestionExample questionExample=new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(id);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);
        pagDTO.setPagInation(totalCount,page,size);

        if(page<1){
            page=1;
        }if(page>pagDTO.getTotalPage()){
            page=pagDTO.getTotalPage();
        }
        if(size<1){
            size=1;
        }
        if(size>totalCount){
            size=totalCount;
        }
        Integer offsize=size*(page-1);

        QuestionExample example=new QuestionExample();
        example.createCriteria().andCreatorEqualTo(id);
        List<Question> questionLists = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offsize, size));

        List<QuestionDTO> questionDTOList=new ArrayList<>();

        for(Question  questionList: questionLists){
            User user=userMapper.selectByPrimaryKey(questionList.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(questionList,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);

        }
        pagDTO.setData(questionDTOList);
        return pagDTO;
    }

    public QuestionDTO queryById(long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question==null){
            throw new CustomizeException(ErrorCodeImpl.QUESTION_NOT_FOUND);
        }
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        QuestionDTO questionDTO=new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createUpdate(Question question) {
    if(question.getId()==null){
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        question.setViewCount(0);
        question.setCommentCount(0);
        question.setLikeCount(0);
        questionMapper.insert(question);
    }
    else{
       Question updateQuestion=new Question();
       updateQuestion.setGmtModified(System.currentTimeMillis());
       updateQuestion.setTitle(question.getTitle());
       updateQuestion.setDescription(question.getDescription());
       updateQuestion.setTag(question.getTag());
       updateQuestion.setViewCount(question.getViewCount());
       QuestionExample questionExample=new QuestionExample();
       questionExample.createCriteria().andIdEqualTo(question.getId());
        int update = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
if(update!=1){
    throw new CustomizeException(ErrorCodeImpl.QUESTION_NOT_FOUND);
}
    }
    }

    public void inView(long id) {
 Question question=new Question();
 question.setId(id);
 question.setViewCount(1);
 questionExtMapper.inCView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        if(StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }
        String [] tags=StringUtils.split(questionDTO.getTag(),",");
        String regexpTag= Arrays.stream(tags).collect(Collectors.joining("|"));
    Question question=new Question();
    question.setId(questionDTO.getId());
    question.setTag(regexpTag);
        List<Question> questionList = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOList = questionList.stream().map((q)->{
            QuestionDTO questionDTO1= new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO1);
            return questionDTO1;
        }).collect(Collectors.toList());

        return questionDTOList;
    }

    public boolean delByQuestionId(long id) {

        int i = questionMapper.deleteByPrimaryKey(id);
    if(i>0){
        return true;
    }
    else{
        throw new CustomizeException(ErrorCodeImpl.QUESTION_NOT_FOUND);
    }
    }

    public PagDTO queryByComment(Integer page, Integer size) {
   List<Question> questionLists= questionExtMapper.queryQuestionComZero(page,size);
        PagDTO<QuestionDTO> pagDTO = new PagDTO<>();
        List<QuestionDTO> questionDTOList=new ArrayList<>();

        for(Question  questionList: questionLists){
            User user=userMapper.selectByPrimaryKey(questionList.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(questionList,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);

        }
        pagDTO.setData(questionDTOList);


        return pagDTO;

    }
}
