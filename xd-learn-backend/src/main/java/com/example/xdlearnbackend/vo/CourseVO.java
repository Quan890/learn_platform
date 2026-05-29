package com.example.xdlearnbackend.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 课程VO类
 * 用于向前端返回课程信息，符合API文档要求
 */
@Data
public class CourseVO {
    /**
     * 课程ID
     */
    private Long id;

    /**
     * 课程标题
     */
    private String title;

    /**
     * 课程描述
     */
    private String description;

    /**
     * 教师姓名
     */
    private String author;

    /**
     * 课程类型
     */
    private String type;

    /**
     * 评分
     */
    private Double rating;

    /**
     * 学生数量
     */
    private Integer students;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 封面图片URL
     */
    private String cover;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 目标
     */
    private List<String> goals;
    /**
     * 适合人群
     */
    private List<String> audience;

    /**
     * 章节列表
     */
    private List<CourseChapterVO> chapters;

}
