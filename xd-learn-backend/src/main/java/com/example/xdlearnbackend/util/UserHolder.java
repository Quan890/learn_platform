package com.example.xdlearnbackend.util;

import com.example.xdlearnbackend.entity.User;

/**
 * 用户持有器
 * 使用ThreadLocal存储当前登录用户信息，便于在请求处理过程中随时获取
 */
public class UserHolder {

    /**
     * ThreadLocal用于存储当前线程的用户信息
     */
    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    /**
     * 设置当前登录用户
     * @param user 用户对象
     */
    public static void setUser(User user) {
        userThreadLocal.set(user);
    }

    /**
     * 获取当前登录用户
     * @return 当前登录用户对象，如果没有登录则返回null
     */
    public static User getUser() {
        return userThreadLocal.get();
    }

    /**
     * 清除当前线程的用户信息
     * 建议在请求处理完成后调用，防止内存泄漏
     */
    public static void clear() {
        userThreadLocal.remove();
    }

    /**
     * 获取当前登录用户ID
     * @return 用户ID，如果没有登录则返回null
     */
    public static Long getUserId() {
        User user = getUser();
        return user != null ? user.getId() : null;
    }

    /**
     * 获取当前登录用户名
     * @return 用户名，如果没有登录则返回null
     */
    public static String getUsername() {
        User user = getUser();
        return user != null ? user.getUsername() : null;
    }
}
