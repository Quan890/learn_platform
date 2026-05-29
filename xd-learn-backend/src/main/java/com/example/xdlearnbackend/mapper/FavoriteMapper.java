package com.example.xdlearnbackend.mapper;

import com.example.xdlearnbackend.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 木又
 * @date 2026/5/21 19:58
 */
@Mapper
public interface FavoriteMapper {

    /**
     * 查询用户是否已收藏该课程
     * @param userId
     * @param courseId
     * @return
     */
    Favorite selectByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 新增收藏
     * @param favorite
     * @return
     */
    int insert(Favorite favorite);

    /**
     * 删除收藏
     * @param userId
     * @param courseId
     * @return
     */
    int delete(@Param("userId") Long userId, @Param("courseId") Long courseId);
}
