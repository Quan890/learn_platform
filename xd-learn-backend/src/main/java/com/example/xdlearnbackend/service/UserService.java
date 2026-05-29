package com.example.xdlearnbackend.service;

import com.example.xdlearnbackend.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Service;

/**
 * @author 木又
 * @date 2026/5/20 16:11
 */
@Service
public interface UserService {
    /**
     * 登录方法
     * @param username 用户名
     * @param password 密码
     * @return 用户信息
     */
    User login(String username, String password);

    /**
     * 根据用户名查询用户信息方法
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);
}
