package com.example.xdlearnbackend.exception;

/**
 * @author: 木又
 * @date: 2026/05/15
 **/

public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer errorCode;

    /**
     * 构造方法
     * @param message
     */
    public BusinessException(String message) {
        super(message);
        this.errorCode = 500;
    }

    /**
     * 构造方法
     * @param errorCode
     * @param message
     */
    public BusinessException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
