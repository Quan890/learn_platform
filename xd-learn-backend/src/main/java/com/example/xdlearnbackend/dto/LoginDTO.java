package com.example.xdlearnbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author 木又
 * @date 2026/5/19 23:10
 */
@Data
public class LoginDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
