package com.example.xdlearnbackend.service;

import com.example.xdlearnbackend.common.PageResult;
import com.example.xdlearnbackend.dto.CourseDTO;
import com.example.xdlearnbackend.entity.Course;
import com.example.xdlearnbackend.vo.CourseVO;
import org.springframework.stereotype.Service;

@Service
public interface CourseService {

    /**
    * 获取课程列表（分页）
    * */
    PageResult<Course> getPage(CourseDTO query);

    /**
    * 获取课程详细
    * */
    CourseVO getCourseDetail(Long id);
}
