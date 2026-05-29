package com.example.xdlearnbackend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 木又
 * @date 2026/5/20 16:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseVO {

    private String accessToken;

    private String refreshToken;

    private UserInfoVO userInfo;
}
