package com.fbw.OneBoot.exception;

public enum ErrorCodeImpl implements  ErrorCode {

QUESTION_NOT_FOUND(2001, "页面不见了"),
TARGET_PARAM_NOT_FOUND(2002,"未选择任何问题"),
NO_LOG(2003,"未登录,您需要先登录"),
    SERVER(2004,"服务器异常"),
TYPE_NOT(2005,"评论类型错误或者不存在"),
    COMMENT_NOT_FOUND(2006,"你操作的评论不见了"),
    COMMENT_IS_EPTMY(2007,"回复内容不能为空"),
   READ_NOTIFY(2008,"读取错误"),
    NOTIFICATION_NOT_FOUND(2009, "通知不见了"),
FILE_UPLOAD_FAIL(2010,"图片上传失败"),
    LIKE_FALSE(2011,"自己不能给自己点赞");
    private Integer code;
    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    ErrorCodeImpl(Integer code,String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }


}
