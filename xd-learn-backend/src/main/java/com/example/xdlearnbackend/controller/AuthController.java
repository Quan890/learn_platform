package com.example.xdlearnbackend.controller;

import com.example.xdlearnbackend.common.Result;
import com.example.xdlearnbackend.dto.RefreshTokenDTO;
import com.example.xdlearnbackend.dto.RegisterDTO;
import com.example.xdlearnbackend.dto.UserLoginDTO;
import com.example.xdlearnbackend.entity.User;
import com.example.xdlearnbackend.exception.BusinessException;
import com.example.xdlearnbackend.service.UserService;
import com.example.xdlearnbackend.util.JwtUtil;
import com.example.xdlearnbackend.vo.LoginResponseVO;
import com.example.xdlearnbackend.vo.UserInfoVO;
import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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

    /**
     * 注册新用户
     */
    @PostMapping("/register")
    public Result<Void> register(@Validated @RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO);
        return Result.success("注册成功", null);
    }

    /**
     * 使用 refreshToken 换取新的令牌对
     */
    @PostMapping("/refresh")
    public Result<LoginResponseVO> refresh(@Validated @RequestBody RefreshTokenDTO refreshTokenDTO) {
        String refreshToken = refreshTokenDTO.getRefreshToken();
        String username;
        try {
            username = jwtUtil.extractUsername(refreshToken);
            if (username == null || jwtUtil.isTokenExpired(refreshToken)) {
                throw new BusinessException(401, "刷新令牌已失效");
            }
        } catch (ExpiredJwtException e) {
            throw new BusinessException(401, "刷新令牌已过期");
        } catch (JwtException | IllegalArgumentException e) {
            throw new BusinessException(401, "刷新令牌无效");
        }

        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new BusinessException(401, "用户不存在");
        }

        LoginResponseVO loginResponse = new LoginResponseVO();
        loginResponse.setAccessToken(jwtUtil.generateToken(username));
        loginResponse.setRefreshToken(jwtUtil.generateRefreshToken(username));
        loginResponse.setUserInfo(getUserInfoVO(user));
        return Result.success("刷新成功", loginResponse);
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
