package com.example.xdlearnbackend.service;

import org.springframework.stereotype.Service;

/**
 * @author 木又
 * @date 2026/5/21 20:00
 */
@Service
public interface FavoriteService {
    void favoriteCourse(Long userId, Long courseId);
    void unfavoriteCourse(Long userId, Long courseId);
    boolean checkFavoriteStatus(Long userId, Long courseId);
}
