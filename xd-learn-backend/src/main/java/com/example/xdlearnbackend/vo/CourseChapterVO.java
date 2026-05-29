package com.example.xdlearnbackend.vo;

import lombok.Data;

@Data
public class CourseChapterVO {
    private Integer id;
    private String title;
    private java.util.List<VideoVO> videos;
}
