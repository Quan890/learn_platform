package com.example.xdlearnbackend.controller;

import com.example.xdlearnbackend.common.PageResult;
import com.example.xdlearnbackend.common.Result;
import com.example.xdlearnbackend.dto.CommentDTO;
import com.example.xdlearnbackend.service.CommentService;
import com.example.xdlearnbackend.util.UserHolder;
import com.example.xdlearnbackend.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 木又
 * @date 2026/5/23 14:47
 */
@RestController
@RequestMapping("/api/courses")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @GetMapping("/{courseId}/comments")
    public Result<PageResult<CommentVO>> getCommentsByCourseId(@PathVariable("courseId") Long courseId,
                                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        PageResult<CommentVO> pageResult =commentService.getCommentsByCourseId(courseId, page, pageSize) ;

        return Result.success(pageResult);
    }

    @PostMapping("/{courseId}/comments")
    public Result<CommentVO> addComment(
            @PathVariable Long courseId,
            @RequestBody CommentDTO dto) {

        // 从当前登录用户获取信息
        Long userId = UserHolder.getUser().getId();
        String userName = UserHolder.getUser().getUsername();
        String userAvatar = UserHolder.getUser().getAvatar();


        CommentVO vo = commentService.addComment(courseId, userId, userName, userAvatar, dto);
        return Result.success("评论成功", vo);
    }

}
