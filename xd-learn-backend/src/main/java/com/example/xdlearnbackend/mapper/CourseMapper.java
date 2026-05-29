package com.example.xdlearnbackend.mapper;

import com.example.xdlearnbackend.entity.Course;
import com.example.xdlearnbackend.vo.CourseChapterVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {
    /**
     * 查询课程数量
     * */
    int count(@Param("course") Course course, 
              @Param("minRating") Double minRating, 
              @Param("minStudentCount") Long minStudentCount);

    /**
     * 分页查询课程列表
     * */
    List<Course> findListByPage(@Param("course") Course course, 
                                @Param("offset") int offset, 
                                @Param("pageSize") int pageSize);

    /**
     * 根据ID查询课程
     * */
    Course findById(@Param("id") Long id);

    /**
     * 根据课程ID获取 学习目标
     * */
    List<String> findLearningObjectivesByCourseId(@Param("courseId") Long courseId);

    /**
     * 根据课程ID获取 适合人群
     * */
    List<String> findTargetAudienceByCourseId(@Param("courseId") Long courseId);

    /**
     * 根据课程ID获取 章节+视频列表
     * */
    List<CourseChapterVO> findChaptersByCourseId(@Param("courseId") Long courseId);
}
