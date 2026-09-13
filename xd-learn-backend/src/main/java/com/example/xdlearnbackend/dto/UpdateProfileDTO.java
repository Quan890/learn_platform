package com.example.xdlearnbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新用户资料请求参数
 * @author 木又
 * @date 2026/9/13
 */
@Data
public class UpdateProfileDTO {

    @Size(max = 20, message = "用户名长度不能超过20")
    private String username;

    @Size(max = 100, message = "昵称长度不能超过100")
    private String nickname;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Size(min = 6, max = 20, message = "密码长度必须在6到20之间")
    private String newPassword;
}
