package com.example.xdlearnbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 课程数据传输对象
 * 用于封装课程相关的查询条件和分页信息
 *
 * @author 木又
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CourseDTO extends QueryResult {
    /**
     * 课程标题（模糊查询）
     */
    private String title;

    /**
     * 课程描述（模糊查询）
     */
    private String description;

    /**
     * 教师姓名（模糊查询）
     */
    private String teacherName;

    /**
     * 课程类型ID
     */
    private Long typeId;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 学习目标内容（模糊查询）
     */
    private String learningObjectiveContent;

    /**
     * 适合人群描述（模糊查询）
     */
    private String targetAudienceContent;

    /**
     * 评分（最小值）
     */
    private BigDecimal minRating;

    /**
     * 学生人数（最小值）
     */
    private Long minStudentCount;
}
