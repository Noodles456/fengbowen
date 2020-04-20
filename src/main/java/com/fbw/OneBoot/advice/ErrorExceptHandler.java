package com.fbw.OneBoot.advice;

import com.alibaba.fastjson.JSON;
import com.fbw.OneBoot.dto.ResultDTO;
import com.fbw.OneBoot.exception.CustomizeException;
import com.fbw.OneBoot.exception.ErrorCodeImpl;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class ErrorExceptHandler {
    @ExceptionHandler(Exception.class)
    Object handler(Throwable ex, Model model,
                   HttpServletRequest request,
                   HttpServletResponse response){
        String contentType = request.getContentType();
        if("application/json".equals(contentType)){
            ResultDTO resultDTO;
            if(ex instanceof CustomizeException){
           resultDTO=ResultDTO.errorOf((CustomizeException)ex);
            }else{
                resultDTO=ResultDTO.errorOf(ErrorCodeImpl.SERVER);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer=response.getWriter();
            writer.write(JSON.toJSONString(resultDTO));
            writer.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
return null;
        }else{

            if(ex instanceof CustomizeException){
                model.addAttribute("messages",ex.getMessage());
                return new ModelAndView("error");
            }else{
                model.addAttribute("messages",ErrorCodeImpl.SERVER.getMessage());
            }
            return new ModelAndView("error");
        }
    }
}


