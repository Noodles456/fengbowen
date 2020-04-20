package com.fbw.OneBoot.controller;

import com.fbw.OneBoot.dto.AccessTokenDTO;
import com.fbw.OneBoot.dto.GitHubUser;
import com.fbw.OneBoot.model.User;
import com.fbw.OneBoot.provider.GitHubProvider;
import com.fbw.OneBoot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {
    @Autowired
    private GitHubProvider gitHubProvider;

    @Autowired
    private UserService userService;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.redirect.uri}")
    private String redirectUri;
    @Value("${github.client.secret}")
    private String clientSecret;
    @GetMapping("/callback")
    public String callBack(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state ,
                           HttpServletResponse response){
       AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
       accessTokenDTO.setCode(code);
       accessTokenDTO.setClient_id(clientId);
       accessTokenDTO.setRedirect_uri(redirectUri);
       accessTokenDTO.setState(state);
       accessTokenDTO.setClient_secret(clientSecret);
        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
        GitHubUser gitHubUser = gitHubProvider.getUser(accessToken);
       if(gitHubUser!=null && gitHubUser.getId()!=null){
           User user=new User();
           String token=UUID.randomUUID().toString();
           user.setToken(token);
           user.setName(gitHubUser.getName());
           user.setAccountId(String.valueOf(gitHubUser.getId()));

           user.setAvatarUrl(gitHubUser.getAvatarUrl());
           userService.createUpdate(user);

           response.addCookie(new Cookie("token",token));
       return "redirect:/";
       }else{
           log.error("callback get gitHub error,{}",gitHubUser);
           return "redirect:/";
       }

    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        request.getSession().removeAttribute("user");
     Cookie cookie=new Cookie("token",null);
     cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
