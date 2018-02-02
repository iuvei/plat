package com.na.manager.util;

import java.util.UUID;

/**
 * Created by sunny on 2017/4/12 0012.
 */
public class TokenUtil {
    private static String key = "lA0u511p4ysDxKt4";

    public static String createToken(long userId){
        String token = UUID.randomUUID().toString().replace("-", "");
        try {
            return EncryptUtil.encrypt(key, userId + "_" + token);
        }catch (Exception e){
            return token;
        }
    }

    public static Long getUserId(String token){
        try {
            String decToken = EncryptUtil.decrypt(key, token);
            String[] decTokens = decToken.split("_");
            return Long.valueOf(decTokens[0]);
        }catch (Exception e){
            return null;
        }
    }

}
