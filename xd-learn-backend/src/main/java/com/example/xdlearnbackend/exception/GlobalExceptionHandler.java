package com.example.xdlearnbackend.exception;

import com.example.xdlearnbackend.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 木又
 * @date 2026/5/15 15:03
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义业务异常
     * @param ex       业务异常
     * @param request  HTTP请求
     * @return 错误信息
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Map<String, Object>> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        logger.error("业务异常: {}", ex.getMessage(),ex);
        Map<String, Object> errorInfo = buildErrorInfo(ex.getErrorCode(), ex.getMessage(), request);
        return Result.error(ex.getErrorCode(), "业务异常");
    }

    /**
     * 处理参数校验异常（通过@Valid注解触发）
     * @param ex 参数校验异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Map<String, Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        logger.error("参数校验异常: {}", ex.getMessage(), ex);

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        Map<String, Object> errorInfo = buildErrorInfo(400, errorMessage, request);
        return Result.error(400, "参数校验失败");
    }

    /**
     * 处理参数校验异常（通过@Validated注解触发）
     * @param ex 参数校验异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Map<String, Object>> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {
        logger.error("参数校验异常: {}", ex.getMessage(), ex);

        String errorMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));

        Map<String, Object> errorInfo = buildErrorInfo(400, errorMessage, request);
        return Result.error(400, "参数校验失败");
    }

    /**
     * 处理参数绑定异常
     * @param ex 参数绑定异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(BindException.class)
    public Result<Map<String, Object>> handleBindException(BindException ex, HttpServletRequest request) {
        logger.error("参数绑定异常: {}", ex.getMessage(), ex);

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        Map<String, Object> errorInfo = buildErrorInfo(400, errorMessage, request);
        return Result.error(400, "参数绑定失败");
    }

    /**
     * 处理空指针异常
     * @param ex 空指针异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<Map<String, Object>> handleNullPointerException(NullPointerException ex, HttpServletRequest request) {
        logger.error("空指针异常: {}", ex.getMessage(), ex);

        Map<String, Object> errorInfo = buildErrorInfo(500, "服务器内部错误", request);
        return Result.error(500, "服务器内部错误");
    }

    /**
     * 处理数据库访问异常
     * @param ex 数据库访问异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(DataAccessException.class)
    public Result<Map<String, Object>> handleDataAccessException(DataAccessException ex, HttpServletRequest request) {
        logger.error("数据库访问异常: {}", ex.getMessage(), ex);

        Map<String, Object> errorInfo = buildErrorInfo(500, "数据库操作失败", request);
        return Result.error(500, "数据库操作失败");
    }

    /**
     * 处理404 Not Found异常
     * @param ex 404异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<Map<String, Object>> handleNotFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        logger.warn("请求的资源未找到: {}", request.getRequestURI());

        Map<String, Object> errorInfo = buildErrorInfo(404, "请求的资源不存在", request);
        return Result.error(404, "请求的资源不存在");
    }

    /**
     * 处理其他未被捕获的异常
     * @param ex 异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(Exception.class)
    public Result<Map<String, Object>> handleException(Exception ex, HttpServletRequest request) {
        logger.error("未处理的异常: {}", ex.getMessage(), ex);

        Map<String, Object> errorInfo = buildErrorInfo(500, "服务器内部错误", request);
        return Result.error(500, "服务器内部错误");
    }


    /**
     * 构建错误信息
     *
     * @param errorCode    错误码
     * @param errorMessage 错误信息
     * @param request      HTTP请求
     * @return 错误信息Map
     */
    private Map<String, Object> buildErrorInfo(Integer errorCode, String errorMessage,HttpServletRequest request) {
        Map<String, Object> errorInfo = new HashMap<>();
        errorInfo.put("error_code", errorCode);
        errorInfo.put("error_message", errorMessage);
        errorInfo.put("timestamp", LocalDateTime.now().toString());
        errorInfo.put("path", request.getRequestURI());
        return errorInfo;

    }
}
