package com.example.xdlearnbackend.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 木又
 * @date 2026/5/23 14:36
 */
@Data
public class Comment {

    private Long id;

    private String content;

    private Long userId;

    private Long courseId;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private Integer rating;
}
