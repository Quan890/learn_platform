package com.example.xdlearnbackend.service;

import com.example.xdlearnbackend.vo.FavoriteCourseVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 木又
 * @date 2026/5/21 20:00
 */
@Service
public interface FavoriteService {
    void favoriteCourse(Long userId, Long courseId);
    void unfavoriteCourse(Long userId, Long courseId);
    boolean checkFavoriteStatus(Long userId, Long courseId);

    /**
     * 查询用户收藏的课程列表
     * @param userId 用户ID
     * @return 收藏的课程列表
     */
    List<FavoriteCourseVO> listFavoriteCourses(Long userId);
}
