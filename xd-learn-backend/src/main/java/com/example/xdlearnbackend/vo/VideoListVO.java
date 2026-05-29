package com.example.xdlearnbackend.vo;

import lombok.Data;

/**
 * @author 木又
 * @date 2026/5/23 17:55
 */
@Data
public class VideoListVO {
    private Long id;
    private String name;
    private Long chapterId;
    private String chapterName;
    private Integer duration;
    private Long playCount;
    private Integer order;
}
