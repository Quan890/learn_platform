package com.example.xdlearnbackend.service;

import com.example.xdlearnbackend.dto.RegisterDTO;
import com.example.xdlearnbackend.dto.UpdateProfileDTO;
import com.example.xdlearnbackend.entity.User;
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

    /**
     * 注册新用户
     * @param registerDTO 注册信息
     */
    void register(RegisterDTO registerDTO);

    /**
     * 更新用户资料（昵称、邮箱、密码）
     * @param userId 用户ID
     * @param updateProfileDTO 资料信息
     */
    void updateProfile(Long userId, UpdateProfileDTO updateProfileDTO);
}
