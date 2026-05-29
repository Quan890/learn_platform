package com.example.xdlearnbackend.util;

import com.example.xdlearnbackend.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 木又
 * @date 2026/5/19 17:23
 */
@Component
public class JwtUtil {

    @Autowired
    private JwtConfig jwtConfig;

    private static SecretKey secretKey;

    static {
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    //获取token
    /**
     * 生成JWT token方法
     * 根据用户名生成带有过期时间的JWT令牌
     * @param username 用户名
     * @return 生成的JWT token字符串
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>() ;
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()*1000))
                .signWith(secretKey)
                .compact();
    }
    /**
     * 生成刷新JWT token方法
     * 根据用户名生成带有较长过期时间的JWT令牌
     * @param username 用户名
     * @return 生成的JWT token字符串
     */
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        // 刷新令牌的过期时间设置为7天
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .signWith(secretKey)
                .compact();
    }
    /**
     * 验证JWT token方法
     * 验证token是否有效且未过期
     * @param token JWT token
     * @return 验证结果，有效返回true，否则返回false
     */
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从token中提取用户名方法
     * 解析JWT token并提取其中的用户名信息
     * @param token JWT token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return extractUsername(token);
    }

    /**
     * 从token中提取用户名方法
     * 解析JWT token并提取其中的用户名信息
     * @param token JWT token
     * @return 用户名
     */
    public String extractUsername(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * 从token中提取过期时间方法
     * 解析JWT token并提取其中的过期时间信息
     * @param token JWT token
     * @return 过期时间
     */
    public Date extractExpiration(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    /**
     * 从token中提取claims方法
     * 解析JWT token并提取其中的claims信息
     * @param token JWT token
     * @return Claims对象
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    /**
     * 检查token是否过期方法
     * 判断JWT token是否已经过期
     * @param token JWT token
     * @return 是否过期，过期返回true，否则返回false
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }


}
