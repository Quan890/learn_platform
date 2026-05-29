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
}
