package com.example.xdlearnbackend.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 木又
 * @date 2026/5/21 19:57
 */
@Data
public class Favorite {
    private Long id;
    private Long userId;
    private Long courseId;
    private LocalDateTime createdTime;
}
