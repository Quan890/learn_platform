package com.example.xdlearnbackend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 木又
 * @date 2026/5/19 17:20
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    //JWT密钥
    private String secret;


    //过期时间
    private Long expiration;


    /**
     * 获取过期时间
     * @return
     */
    public Long getExpiration() {
        return expiration;
    }

    /**
     * 设置过期时间
     * @param expiration
     */
    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    /**
     * 获取JWT密钥
     * @return
     */
    public String getSecret() {
        return secret;
    }

    /**
     * 设置JWT密钥
     * @param secret
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

}
