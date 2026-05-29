package com.example.xdlearnbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 木又
 * @date 2026/5/15 16:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseType {

    private Integer id;

    private String name;

    private String description;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

}
