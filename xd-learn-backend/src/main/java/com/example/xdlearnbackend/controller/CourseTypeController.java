package com.example.xdlearnbackend.controller;

import com.example.xdlearnbackend.common.Result;
import com.example.xdlearnbackend.entity.CourseType;
import com.example.xdlearnbackend.service.CourseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 木又
 * @date 2026/5/15 16:29
 */

@RestController
@RequestMapping("/api/course-types")
public class CourseTypeController {

    @Autowired
    private CourseTypeService courseTypeService;

    /**
     * 获取所有课程类型
     * @return
     */
    @GetMapping
    public Result<List<CourseType>> getAllCourseType(){
      List<CourseType> courseTypes = courseTypeService.getList(null);
      return Result.success("success",courseTypes);
    }
}
