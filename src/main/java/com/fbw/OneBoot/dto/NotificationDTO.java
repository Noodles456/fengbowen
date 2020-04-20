package com.fbw.OneBoot.dto;


import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private Long outid;
    private String notifierName;
    private String outerTitle;
    private String typeName;
    private Integer type;
}
