package com.example.xdlearnbackend.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户收藏的课程列表项
 * @author 木又
 * @date 2026/9/13
 */
@Data
public class FavoriteCourseVO {

    /** 课程ID */
    private Long id;

    /** 课程标题 */
    private String title;

    /** 封面图片URL */
    private String coverImage;

    /** 教师姓名 */
    private String teacherName;

    /** 价格 */
    private BigDecimal price;

    /** 课程类型名称 */
    private String typeName;

    /** 收藏时间 */
    private LocalDateTime favoriteTime;
}
