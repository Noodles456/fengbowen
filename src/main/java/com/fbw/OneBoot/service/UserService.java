package com.fbw.OneBoot.service;

import com.fbw.OneBoot.mapper.UserMapper;
import com.fbw.OneBoot.model.User;
import com.fbw.OneBoot.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    public void createUpdate(User user) {
        UserExample userExample=new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
    if(users.size()==0){
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModicied(user.getGmtCreate());
        userMapper.insert(user);
    }else{
        User dbuser=users.get(0);
        User updateUser=new User();
        updateUser.setGmtModicied(System.currentTimeMillis());
        updateUser.setAvatarUrl(user.getAvatarUrl());
        updateUser.setName(user.getName());
        updateUser.setToken(user.getToken());
    UserExample example=new UserExample();
    example.createCriteria().andIdEqualTo(dbuser.getId());
        userMapper.updateByExampleSelective(updateUser,example);
    }
    }
}
