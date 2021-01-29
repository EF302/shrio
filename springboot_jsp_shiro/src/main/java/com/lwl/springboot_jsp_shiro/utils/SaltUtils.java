package com.lwl.springboot_jsp_shiro.utils;

import java.util.Random;

/**
 * 生成随随机盐
 */
public class SaltUtils {

    /**
     * 生成salt的静态方法
     *
     * @param n
     * @return
     */
    public static String getSalt(int n) {
        //注 转大小写快捷键ctrl+shift+U
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWSYZabcdefghijklmnopqrstuvwsyz0123456789~`!@#$%^&*()-_+=".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            //随机数范围为[0,chars.length)
            char aChar = chars[new Random().nextInt(chars.length)];
            sb.append(aChar);
        }
        return sb.toString();
    }
}
