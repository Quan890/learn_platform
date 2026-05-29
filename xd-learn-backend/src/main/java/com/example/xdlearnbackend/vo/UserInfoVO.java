package com.example.xdlearnbackend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 木又
 * @date 2026/5/19 23:11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO {

    private Long id;

    private String username;

    private String nickname;

    private String avatar;

    private String email;

    private LocalDateTime registerTime;

    private LocalDateTime lastLoginTime;

}
