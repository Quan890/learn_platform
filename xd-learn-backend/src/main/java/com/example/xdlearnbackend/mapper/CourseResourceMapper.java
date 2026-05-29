package com.example.xdlearnbackend.mapper;

import com.example.xdlearnbackend.vo.CourseResourceVO;
import com.example.xdlearnbackend.vo.VideoDetailVO;
import com.example.xdlearnbackend.vo.VideoListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseResourceMapper {
    List<CourseResourceVO> findByCourseId(@Param("courseId") Long courseId);

    List<VideoListVO> findVideosByCourseId(@Param("courseId") Long courseId);

    VideoDetailVO findVideoUrlById(@Param("videoId") Long videoId);
}