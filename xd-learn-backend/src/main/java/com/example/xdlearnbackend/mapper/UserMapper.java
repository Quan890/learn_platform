package com.example.xdlearnbackend.mapper;

import com.example.xdlearnbackend.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 木又
 * @date 2026/5/19 23:12
 */
@Mapper
public interface UserMapper {
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    User findBYUsername(String username);

    /**
     * 根据用户ID查询用户
     * @param id 用户ID
     * @return 用户
     */
    User findById(Long id);

    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户
     */
    User findByEmail(String email);

    /**
     * 新增用户
     * @param user 用户信息（含已加密密码）
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 更新用户资料（昵称、邮箱、密码）
     * @param user 待更新的用户信息
     * @return 影响行数
     */
    int updateProfile(User user);
}
