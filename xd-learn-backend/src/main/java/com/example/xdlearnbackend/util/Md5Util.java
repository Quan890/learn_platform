package com.example.xdlearnbackend.util;


import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author 木又
 * @date 2026/5/20 16:23
 */
public class Md5Util {

    private static final String FIXED_SALT = "xdlearn2025";
    
    public static String md5(String input) {
        return DigestUtils.md5Hex(input + FIXED_SALT);
    }

    public static Boolean verify(String input, String md5password) {
        return md5(input).equals(md5password);
    }
}
