package com.example.xdlearnbackend.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseResourceVO {
    private Long id;
    private String name;
    private String type;
    private Long size;
    private String uploadDate;
    private Integer downloadCount;
    private String url;
}