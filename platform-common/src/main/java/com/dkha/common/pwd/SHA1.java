package com.dkha.common.pwd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * sha转换
 */
public class SHA1 {

    private static Logger logger = LoggerFactory.getLogger(SHA1.class);

    public static String convert(String password) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(password.getBytes("UTF-8"));
            password = SHA1.byte2hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return password;
    }

    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0xFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs;
    }

    public static void main(String[] args) {
        System.out.println(SHA1.convert("123123"));
    }
}
