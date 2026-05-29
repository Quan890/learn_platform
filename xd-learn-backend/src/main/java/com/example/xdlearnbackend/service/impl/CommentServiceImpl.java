package com.example.xdlearnbackend.service.impl;

import com.example.xdlearnbackend.common.PageResult;
import com.example.xdlearnbackend.dto.CommentDTO;
import com.example.xdlearnbackend.entity.Comment;
import com.example.xdlearnbackend.mapper.CommentMapper;
import com.example.xdlearnbackend.service.CommentService;
import com.example.xdlearnbackend.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论服务实现类
 * @author 木又
 * @date 2026/5/23 14:50
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public PageResult<CommentVO> getCommentsByCourseId(Long courseId, Integer page, Integer pageSize) {
        // 参数校验
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }

        // 计算偏移量
        int offset = (page - 1) * pageSize;

        // 查询总数
        int total = commentMapper.countByCourseId(courseId);

        // 查询评论列表（包含用户信息）
        List<CommentVO> comments = commentMapper.listByCourseId(courseId, offset, pageSize);

        // 封装分页结果
        return new PageResult<>(comments, total, page, pageSize);
    }

    @Override
    public CommentVO addComment(Long courseId, Long userId, String userName, String userAvatar, CommentDTO dto) {
        // 1. 构建评论实体
        Comment comment = new Comment();
        comment.setCourseId(courseId);
        comment.setUserId(userId);
        comment.setRating(dto.getRating());
        comment.setContent(dto.getContent());
        comment.setCreatedTime(LocalDateTime.now());

        // 2. 插入数据库
        commentMapper.insert(comment);

        // 3. 封装返回VO
        CommentVO vo = new CommentVO();
        vo.setId(comment.getId());
        vo.setUserId(userId);
        vo.setUserName(userName);
        vo.setUserAvatar(userAvatar);
        vo.setRating(dto.getRating());
        vo.setContent(dto.getContent());
        vo.setCreateTime(comment.getCreatedTime());

        return vo;
    }
}
