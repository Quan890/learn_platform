package com.example.xdlearnbackend.controller;

import com.example.xdlearnbackend.common.Result;
import com.example.xdlearnbackend.service.CourseResourceService;
import com.example.xdlearnbackend.vo.VideoDetailVO;
import com.example.xdlearnbackend.vo.VideoListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VideoController {

    @Autowired
    private CourseResourceService courseResourceService;

    @GetMapping("/videos/{videoId}/url")
    public Result<VideoDetailVO> getVideoUrl(@PathVariable Long videoId) {
        VideoDetailVO videoUrl = courseResourceService.getVideoUrl(videoId);
        return Result.success("获取视频播放地址成功", videoUrl);
    }

    @GetMapping("/courses/{courseId}/videos")
    public Result<List<VideoListVO>> getCourseVideos(@PathVariable Long courseId) {
        List<VideoListVO> videos = courseResourceService.getCourseVideos(courseId);
        return Result.success("获取视频列表成功", videos);
    }
}