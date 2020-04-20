package com.fbw.OneBoot.dto;

import com.fbw.OneBoot.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private  long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
