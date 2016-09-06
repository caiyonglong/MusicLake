package com.cyl.music_hnust.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 对密码加密
 * 作者：yonglong on 2016/8/21 12:49
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class EncryptUtils {

    public static String toMD5(String string) {

        byte[] hash;

        try {

            hash = MessageDigest.getInstance("EncryptUtils").digest(string.getBytes("UTF-8"));

        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException("Huh, EncryptUtils should be supported?", e);

        } catch (UnsupportedEncodingException e) {

            throw new RuntimeException("Huh, UTF-8 should be supported?", e);

        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {

            if ((b & 0xFF) < 0x10) hex.append("0");

            hex.append(Integer.toHexString(b & 0xFF));

        }
        return hex.toString();

    }
}
