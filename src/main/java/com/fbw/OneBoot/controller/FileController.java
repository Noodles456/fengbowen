package com.fbw.OneBoot.controller;

import com.fbw.OneBoot.dto.FileDTO;
import com.fbw.OneBoot.provider.UcloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class FileController {
    @Autowired
    private UcloudProvider ucloudProvider;
@RequestMapping("/file/upload")
@ResponseBody
    public FileDTO upload(HttpServletRequest request){
    MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) request;
    MultipartFile file = multipartRequest.getFile("editormd-image-file");
    try {
        String fileName = ucloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl(fileName);
        return fileDTO;
    } catch (IOException e) {
        e.printStackTrace();
    }
    FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/images/wx.jpg");
        return fileDTO;
    }
}
