package com.example.xdlearnbackend.controller;

import com.example.xdlearnbackend.common.Result;
import com.example.xdlearnbackend.entity.User;
import com.example.xdlearnbackend.exception.BusinessException;
import com.example.xdlearnbackend.service.FavoriteService;
import com.example.xdlearnbackend.util.UserHolder;
import com.example.xdlearnbackend.vo.FavoriteCourseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程收藏相关接口
 * @author 木又
 * @date 2026/5/21
 */
@RestController
@RequestMapping("/api")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    /**
     * 获取当前登录用户的用户ID，未登录时抛出业务异常
     */
    private Long currentUserId() {
        User user = UserHolder.getUser();
        if (user == null) {
            throw new BusinessException(401, "用户未登录");
        }
        return user.getId();
    }

    // 收藏课程
    @PostMapping("/courses/{id}/collect")
    public Result<Void> favoriteCourse(@PathVariable Long id) {
        favoriteService.favoriteCourse(currentUserId(), id);
        return Result.success("收藏成功", null);
    }

    // 取消收藏
    @DeleteMapping("/courses/{id}/collect")
    public Result<Void> unfavoriteCourse(@PathVariable Long id) {
        favoriteService.unfavoriteCourse(currentUserId(), id);
        return Result.success("取消收藏成功", null);
    }

    // 检查是否收藏
    @GetMapping("/courses/{id}/check-collect")
    public Result<Map<String, Boolean>> checkCollect(@PathVariable Long id) {
        boolean isCollected = favoriteService.checkFavoriteStatus(currentUserId(), id);
        Map<String, Boolean> data = new HashMap<>();
        data.put("isCollected", isCollected);
        return Result.success("success", data);
    }

    // 当前用户收藏的课程列表
    @GetMapping("/favorites")
    public Result<List<FavoriteCourseVO>> listFavorites() {
        return Result.success("获取收藏列表成功", favoriteService.listFavoriteCourses(currentUserId()));
    }

    // 取消收藏（收藏列表页使用）
    @DeleteMapping("/favorites/{courseId}")
    public Result<Void> removeFavorite(@PathVariable Long courseId) {
        favoriteService.unfavoriteCourse(currentUserId(), courseId);
        return Result.success("取消收藏成功", null);
    }
}
