package com.na.gate.util;

import com.na.gate.vo.PlatformManageLoginRequest;
import org.apache.tomcat.util.buf.HexUtils;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/7/29 0029.
 */
public class SignUtil {
    /**
     * 根据平台要求签名,签名的对象必须要有sign字段.
     * @param obj
     * @param key
     * @return
     */
    public static String sign(Object obj,String key){
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            Arrays.sort(fields, (f1, f2) -> {
                return f1.getName().compareToIgnoreCase(f2.getName());
            });
            StringBuilder sb = new StringBuilder(key);
            for (Field field : fields) {
                field.setAccessible(true);
                Object val = field.get(obj);
                if(val!=null && !field.getName().equals("sign")) {
                    sb.append(field.getName()).append(val);
                }
            }
            sb.append(key);
            String sign = URLEncoder.encode(sb.toString(),"utf-8");
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] buf = messageDigest.digest(sign.getBytes("utf-8"));
            return HexUtils.toHexString(buf);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public static boolean verifySign(Object obj,String key){
        String sign = sign(obj,key);
        try {
            Field field = obj.getClass().getDeclaredField("sign");
            field.setAccessible(true);
            String sourceSign = (String)field.get(obj);
            return sourceSign==null ? false : sign.equalsIgnoreCase(sourceSign);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void main(String[] args) {
        PlatformManageLoginRequest request = new PlatformManageLoginRequest();
        request.setId("admin陈");
        request.setTimestamp(1504567678L);
        request.setSign("5d6dfa58ee1fe6cf73a965b08afc28fdad46eb33ad2efca1540799c72f3c413e");
        String sb = SignUtil.sign(request,"abc");
        System.out.println(sb.toString());

        System.out.println(SignUtil.verifySign(request,"abc"));
    }
}
