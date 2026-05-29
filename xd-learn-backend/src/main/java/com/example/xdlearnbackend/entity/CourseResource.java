package com.example.xdlearnbackend.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseResource {
    private Long id;
    private String name;
    private String type;
    private Long size;
    private String url;
    private Integer downloadCount;
    private Long courseId;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}