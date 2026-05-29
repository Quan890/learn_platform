package com.example.xdlearnbackend.mapper;

import com.example.xdlearnbackend.entity.CourseType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 木又
 * @date 2026/5/15 16:51
 */
@Mapper
public interface CourseTypeMapper {
    /**
     * 查询列表
     * @param courseType
     * @return
     */
    List<CourseType> findList(CourseType courseType);
}
