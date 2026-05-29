package com.example.xdlearnbackend.controller;

import com.example.xdlearnbackend.common.Result;
import com.example.xdlearnbackend.entity.User;
import com.example.xdlearnbackend.exception.BusinessException;
import com.example.xdlearnbackend.service.UserService;
import com.example.xdlearnbackend.vo.UserInfoVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户相关接口
 * @author 木又
 * @date 2026/5/20
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo(HttpServletRequest request) {
        // 从请求属性中获取当前用户（由 JWT 拦截器设置）
        User currentUser = (User) request.getAttribute("currentUser");

        if (currentUser == null) {
            throw new BusinessException(401, "用户未登录");
        }

        // 构造用户信息响应
        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setId(currentUser.getId());
        userInfo.setUsername(currentUser.getUsername());
        userInfo.setNickname(currentUser.getNickname() != null ? currentUser.getNickname() : currentUser.getUsername());
        userInfo.setEmail(currentUser.getEmail());
        userInfo.setAvatar(currentUser.getAvatar() != null ? currentUser.getAvatar() : "https://example.com/avatar.jpg");
        userInfo.setRegisterTime(currentUser.getCreatedTime());
        userInfo.setRegisterTime(currentUser.getCreatedTime());
        userInfo.setLastLoginTime(currentUser.getUpdatedTime());

        return Result.success("获取用户信息成功", userInfo);
    }
}
