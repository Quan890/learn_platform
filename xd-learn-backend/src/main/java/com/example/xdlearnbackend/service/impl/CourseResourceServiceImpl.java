package com.example.xdlearnbackend.service.impl;

import com.example.xdlearnbackend.exception.BusinessException;
import com.example.xdlearnbackend.mapper.CourseResourceMapper;
import com.example.xdlearnbackend.service.CourseResourceService;
import com.example.xdlearnbackend.vo.CourseResourceVO;
import com.example.xdlearnbackend.vo.VideoDetailVO;
import com.example.xdlearnbackend.vo.VideoListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 木又
 * @date 2026/5/23 16:59
 */
@Service
public class CourseResourceServiceImpl implements CourseResourceService {
    @Autowired
    private CourseResourceMapper courseResourceMapper;

    @Override
    public List<CourseResourceVO> getCourseResources(Long courseId) {
        return courseResourceMapper.findByCourseId(courseId);
    }

    @Override
    public List<VideoListVO> getCourseVideos(Long courseId) {
        return courseResourceMapper.findVideosByCourseId(courseId);
    }

    @Override
    public VideoDetailVO getVideoUrl(Long videoId) {
        VideoDetailVO videoUrl = courseResourceMapper.findVideoUrlById(videoId);
        if (videoUrl == null) {
            throw new BusinessException("视频不存在");
        }
        return videoUrl;
    }
}
