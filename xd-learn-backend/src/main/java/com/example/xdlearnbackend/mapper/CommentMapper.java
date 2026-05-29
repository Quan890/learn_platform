package com.example.xdlearnbackend.mapper;

import com.example.xdlearnbackend.entity.Comment;
import com.example.xdlearnbackend.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 木又
 * @date 2026/5/23 14:49
 */
@Mapper
public interface CommentMapper {

    int countByCourseId(@Param("courseId") Long courseId);

    List<CommentVO> listByCourseId(@Param("courseId") Long courseId,
                                   @Param("offset") int offset,
                                   @Param("pageSize") int pageSize);

    int insert(Comment comment);
}
