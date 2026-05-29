package com.example.xdlearnbackend.service.impl;

import com.example.xdlearnbackend.entity.User;
import com.example.xdlearnbackend.mapper.UserMapper;
import com.example.xdlearnbackend.service.UserService;
import com.example.xdlearnbackend.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 木又
 * @date 2026/5/20 16:11
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User login(String username, String password) {
        User user = userMapper.findBYUsername(username);

        if (user != null && Md5Util.verify(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.findBYUsername( username);
    }
}
