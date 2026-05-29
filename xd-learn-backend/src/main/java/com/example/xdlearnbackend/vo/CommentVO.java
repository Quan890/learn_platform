package com.example.xdlearnbackend.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评论VO类
 * 包含评论信息和用户信息
 * @author 木又
 * @date 2026/5/23
 */
@Data
public class CommentVO {

    /**
     * 评论ID
     */
    private Long id;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 评分
     */
    private Integer rating;
}
