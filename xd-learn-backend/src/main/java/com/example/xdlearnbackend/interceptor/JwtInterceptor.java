package com.example.xdlearnbackend.interceptor;

import com.example.xdlearnbackend.entity.User;
import com.example.xdlearnbackend.service.UserService;
import com.example.xdlearnbackend.util.JwtUtil;
import com.example.xdlearnbackend.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JWT拦截器
 * 用于验证HTTP请求中的JWT令牌
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    /**
     * JWT工具类，用于验证JWT令牌
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户服务，用于根据用户名获取用户信息
     */
    @Autowired
    private UserService userService;

    /**
     * 在处理请求之前进行拦截
     * 验证请求头中的Authorization字段是否包含有效的JWT令牌
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param handler 处理器
     * @return 是否继续处理请求
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 记录请求信息
        logger.debug("Processing request: {} {}", request.getMethod(), request.getRequestURI());

        // 检查是否是获取评论列表的GET请求，如果是则允许公开访问
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        if (requestURI.matches(".*/api/courses/\\d+/comments") && "GET".equals(method)) {
            logger.debug("Allowing public access to GET comments endpoint");
            return true; // 允许GET请求访问评论列表接口
        }

        // 获取请求头中的Authorization字段
        String authorizationHeader = request.getHeader("Authorization");
        logger.debug("Authorization header: {}", authorizationHeader);

        // 检查Authorization字段是否存在且以Bearer开头
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // 提取JWT令牌
            String token = authorizationHeader.substring(7);
            logger.debug("Extracted token: {}", token.substring(0, Math.min(token.length(), 20)) + (token.length() > 20 ? "..." : ""));

            try {
                // 验证JWT令牌
                String username = jwtUtil.extractUsername(token);
                logger.debug("Extracted username from token: {}", username);

                if (username != null && !jwtUtil.isTokenExpired(token)) {
                    // 令牌有效，获取用户信息并存储在请求上下文中
                    User user = userService.getUserByUsername(username);
                    if (user != null) {
                        logger.debug("Found user with ID: {}", user.getId());
                        // 将用户信息存储在请求属性中和UserHolder中
                        request.setAttribute("currentUser", user);
                        UserHolder.setUser(user);
                        logger.debug("Successfully authenticated user: {} (ID: {})", username, user.getId());
                        return true;
                    } else {
                        logger.warn("User not found for username: {}", username);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("用户不存在");
                        return false;
                    }
                } else {
                    logger.warn("Token expired or username is null");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("令牌已过期或无效");
                    return false;
                }
            } catch (Exception e) {
                logger.error("Error validating token: ", e);
                // 令牌无效或已过期
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("令牌验证失败: " + e.getMessage());
                return false;
            }
        }

        // 未提供令牌或令牌无效
        logger.warn("Missing or invalid Authorization header");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("缺少有效的认证令牌");
        return false;
    }

    /**
     * 在请求处理完成后执行
     * 清理UserHolder中的用户信息，防止ThreadLocal内存泄漏
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param handler 处理器
     * @param ex 异常
     * @throws Exception 异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.clear();
        logger.debug("Cleared UserHolder after request completion");
    }
}