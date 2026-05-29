package com.example.xdlearnbackend.service;

import com.example.xdlearnbackend.dto.CourseTypeDTO;
import com.example.xdlearnbackend.entity.CourseType;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 木又
 * @date 2026/5/15 16:38
 */
@Service
public interface CourseTypeService {
    /**
     * 获取课程类型列表
     * @param query 查询参数
     * @return 课程类型列表
     */
    List<CourseType> getList(CourseTypeDTO courseTypeDTO);
}
