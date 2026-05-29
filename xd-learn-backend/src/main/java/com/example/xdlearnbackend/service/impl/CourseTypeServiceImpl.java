package com.example.xdlearnbackend.service.impl;

import com.example.xdlearnbackend.dto.CourseTypeDTO;
import com.example.xdlearnbackend.entity.CourseType;
import com.example.xdlearnbackend.mapper.CourseTypeMapper;
import com.example.xdlearnbackend.service.CourseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 木又
 * @date 2026/5/15 16:39
 */
@Service
public class CourseTypeServiceImpl implements CourseTypeService {

    @Autowired
    private CourseTypeMapper courseTypeMapper;
    @Override
    public List<CourseType> getList(CourseTypeDTO query) {


        CourseType courseType = new CourseType();

        if (query != null) {
            courseType.setName(query.getName());
            courseType.setDescription(query.getDescription());
        }

        return courseTypeMapper.findList(courseType);
    }
}
