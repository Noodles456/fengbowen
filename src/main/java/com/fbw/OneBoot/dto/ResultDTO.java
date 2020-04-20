package com.fbw.OneBoot.dto;

import com.fbw.OneBoot.exception.CustomizeException;
import com.fbw.OneBoot.exception.ErrorCodeImpl;
import lombok.Data;

@Data
public class ResultDTO<T> {
private Integer code;
private String messages;
private  T data;
public static ResultDTO errorOf(Integer code,String messages){
    ResultDTO resultDTO=new ResultDTO();
    resultDTO.setCode(code);
    resultDTO.setMessages(messages);
    return resultDTO;
}

    public static ResultDTO errorOf(ErrorCodeImpl errorCode) {
    return errorOf(errorCode.getCode(),errorCode.getMessage());
}
public static  ResultDTO okOf(){
    ResultDTO resultDTO=new ResultDTO();
    resultDTO.setCode(200);
    resultDTO.setMessages("请求成功");
    return resultDTO;
}
    public static <T> ResultDTO okOf(T t){
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessages("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeException ex) {
    return errorOf(ex.getCode(),ex.getMessage());
}
}
