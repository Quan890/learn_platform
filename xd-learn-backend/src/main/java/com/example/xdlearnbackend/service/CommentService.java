package com.example.xdlearnbackend.service;

import com.example.xdlearnbackend.common.PageResult;
import com.example.xdlearnbackend.dto.CommentDTO;
import com.example.xdlearnbackend.vo.CommentVO;
import org.springframework.stereotype.Service;

/**
 * @author 木又
 * @date 2026/5/23 14:50
 */
@Service
public interface CommentService {
    PageResult<CommentVO> getCommentsByCourseId(Long courseId, Integer page, Integer pageSize);

    CommentVO addComment(Long courseId, Long userId, String userName, String userAvatar, CommentDTO dto);
}
