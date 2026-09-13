package com.example.xdlearnbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 刷新令牌请求参数
 * @author 木又
 * @date 2026/9/13
 */
@Data
public class RefreshTokenDTO {

    @NotBlank(message = "refreshToken不能为空")
    private String refreshToken;
}
