package com.na.user.socketserver.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.na.user.socketserver.exception.AppException;

/**
 * Created by Administrator on 2017/4/27 0027.
 */
public class Md5Util {
    private static final Logger log = LoggerFactory.getLogger(Md5Util.class);

    public static String digest(String algorithm, byte[] bytes, byte[] salt, int iterations){
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);

            if (salt != null) {
                md.update(salt);
            }

            byte[] hashBytes = md.digest(bytes);

            for (int i = 1; i < iterations; i++) {
                md.reset();
                hashBytes = md.digest(hashBytes);
            }

            return fixedHexString(hashBytes);
        }
        catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(),e);
            throw AppException.createError(e.getMessage());
        }
    }

    public static String fixedHexString(byte[] hashBytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hashBytes.length; i++) {
            sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
