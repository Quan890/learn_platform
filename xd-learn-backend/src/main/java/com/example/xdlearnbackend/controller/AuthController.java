package com.example.xdlearnbackend.controller;

import com.example.xdlearnbackend.common.Result;
import com.example.xdlearnbackend.dto.UserLoginDTO;
import com.example.xdlearnbackend.entity.User;
import com.example.xdlearnbackend.exception.BusinessException;
import com.example.xdlearnbackend.service.UserService;
import com.example.xdlearnbackend.util.JwtUtil;
import com.example.xdlearnbackend.vo.LoginResponseVO;
import com.example.xdlearnbackend.vo.UserInfoVO;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 木又
 * @date 2026/5/20 15:57
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/login")
    public Result<LoginResponseVO> login(@Validated @RequestBody UserLoginDTO userLoginDTO) throws BusinessException {
        User user = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        if (user != null) {
            String accessToken = jwtUtil.generateToken(user.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());
            // 构造用户信息
            UserInfoVO userInfo = getUserInfoVO(user);

            // 构造登录响应
            LoginResponseVO loginResponse = new LoginResponseVO();
            loginResponse.setAccessToken(accessToken);
            loginResponse.setRefreshToken(refreshToken);
            loginResponse.setUserInfo(userInfo);

            return Result.success("登录成功", loginResponse);
        } else {
            throw new BusinessException(401, "用户名或密码错误");
        }
    }
    private static @NonNull UserInfoVO getUserInfoVO(User user) {
        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setNickname(user.getNickname() != null ? user.getNickname() : user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setAvatar(user.getAvatar() != null ? user.getAvatar() : "https://example.com/avatar.jpg");
        userInfo.setRegisterTime(user.getCreatedTime());
        userInfo.setLastLoginTime(user.getUpdatedTime());
        return userInfo;
    }
}
