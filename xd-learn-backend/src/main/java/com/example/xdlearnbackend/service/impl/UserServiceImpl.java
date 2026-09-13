package com.example.xdlearnbackend.service.impl;

import com.example.xdlearnbackend.dto.RegisterDTO;
import com.example.xdlearnbackend.dto.UpdateProfileDTO;
import com.example.xdlearnbackend.entity.User;
import com.example.xdlearnbackend.exception.BusinessException;
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

    @Override
    public void register(RegisterDTO registerDTO) {
        String username = registerDTO.getUsername();
        String email = registerDTO.getEmail();

        if (userMapper.findBYUsername(username) != null) {
            throw new BusinessException(400, "用户名已被占用");
        }
        if (userMapper.findByEmail(email) != null) {
            throw new BusinessException(400, "邮箱已被注册");
        }

        User user = new User();
        user.setUsername(username);
        user.setNickname(username);
        // 密码加密后入库
        user.setPassword(Md5Util.md5(registerDTO.getPassword()));
        user.setEmail(email);
        userMapper.insert(user);
    }

    @Override
    public void updateProfile(Long userId, UpdateProfileDTO updateProfileDTO) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException(401, "用户不存在");
        }

        // 用户名与JWT中的身份绑定，不允许修改
        if (updateProfileDTO.getUsername() != null && !updateProfileDTO.getUsername().equals(user.getUsername())) {
            throw new BusinessException(400, "用户名不允许修改");
        }

        String newEmail = updateProfileDTO.getEmail();
        if (newEmail != null && !newEmail.equals(user.getEmail())) {
            User emailOwner = userMapper.findByEmail(newEmail);
            if (emailOwner != null) {
                throw new BusinessException(400, "邮箱已被注册");
            }
            user.setEmail(newEmail);
        }

        if (updateProfileDTO.getNickname() != null && !updateProfileDTO.getNickname().isBlank()) {
            user.setNickname(updateProfileDTO.getNickname());
        }

        String newPassword = updateProfileDTO.getNewPassword();
        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(Md5Util.md5(newPassword));
        }

        userMapper.updateProfile(user);
    }
}
