package com.example.xdlearnbackend.vo;

import lombok.Data;

@Data
public class VideoVO {
    private Integer id;
    private String title;
    private Integer duration;
    private Long playCount;
}
