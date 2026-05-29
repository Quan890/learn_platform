package com.example.xdlearnbackend.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 木又
 * @date 2026/5/17 15:49
 */
@Data
public class CourseListVO {

    private List<CourseVO> list;

    private Long total;

    private int page;

    private int pageSize;
}
