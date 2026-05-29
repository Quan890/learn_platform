package com.example.xdlearnbackend.controller;

import com.example.xdlearnbackend.common.Result;
import com.example.xdlearnbackend.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/courses")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    // 收藏课程
    @PostMapping("/{id}/collect")
    public Result<Void> favoriteCourse(
            @PathVariable Long id,
            @RequestParam Long userId // 实际项目中这里从Token获取，先简化
    ) {
        favoriteService.favoriteCourse(userId, id);
        return Result.success("收藏成功", null);
    }

    // 取消收藏
    @DeleteMapping("/{id}/collect")
    public Result<Void> unfavoriteCourse(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        favoriteService.unfavoriteCourse(userId, id);
        return Result.success("取消收藏成功", null);
    }

    // 检查是否收藏
    @GetMapping("/{id}/check-collect")
    public Result<Map<String, Boolean>> checkCollect(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        boolean isCollected = favoriteService.checkFavoriteStatus(userId, id);
        Map<String, Boolean> data = new HashMap<>();
        data.put("isCollected", isCollected);
        return Result.success("success", data);
    }
}