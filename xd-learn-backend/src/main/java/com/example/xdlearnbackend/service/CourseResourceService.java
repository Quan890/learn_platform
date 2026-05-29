package com.example.xdlearnbackend.service;

import com.example.xdlearnbackend.vo.CourseResourceVO;
import com.example.xdlearnbackend.vo.VideoDetailVO;
import com.example.xdlearnbackend.vo.VideoListVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 木又
 * @date 2026/5/23 16:56
 */
@Service
public interface CourseResourceService {

    List<CourseResourceVO> getCourseResources(Long courseId);

    List<VideoListVO> getCourseVideos(Long courseId);

    VideoDetailVO getVideoUrl(Long videoId);

}
