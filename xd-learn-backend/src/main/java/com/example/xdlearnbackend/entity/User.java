package com.example.xdlearnbackend.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 木又
 * @date 2026/5/17 16:34
 */
@Data
public class User {
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String email;

    private String avatar;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;
}
