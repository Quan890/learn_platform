package com.example.xdlearnbackend.service.impl;

import com.example.xdlearnbackend.entity.Favorite;
import com.example.xdlearnbackend.exception.BusinessException;
import com.example.xdlearnbackend.mapper.FavoriteMapper;
import com.example.xdlearnbackend.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 木又
 * @date 2026/5/21 20:01
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public void favoriteCourse(Long userId, Long courseId) {
        // 检查是否已收藏
        Favorite favorite = favoriteMapper.selectByUserAndCourse(userId, courseId);
        if (favorite != null) {
            throw new BusinessException("您已收藏过该课程");
        }
        // 新增收藏
        Favorite newFavorite = new Favorite();
        newFavorite.setUserId(userId);
        newFavorite.setCourseId(courseId);
        favoriteMapper.insert(newFavorite);
    }

    @Override
    public void unfavoriteCourse(Long userId, Long courseId) {
        Favorite favorite = favoriteMapper.selectByUserAndCourse(userId, courseId);
        if (favorite == null) {
            throw new BusinessException("您尚未收藏该课程");
        }
        favoriteMapper.delete(userId, courseId);
    }

    @Override
    public boolean checkFavoriteStatus(Long userId, Long courseId) {
        Favorite favorite = favoriteMapper.selectByUserAndCourse(userId, courseId);
        return favorite != null;
    }
}