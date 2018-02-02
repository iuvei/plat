package com.na.test.batchbet.util;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.Base64;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.na.betRobot.util.ZLibUtils;
import com.na.test.batchbet.common.CommandRequest;

/**
 * Created by sunny on 2017/4/27 0027.
 */
public class SocketUtil {
    private static Logger log = LoggerFactory.getLogger(SocketUtil.class);

    /**
     * 通讯解密和验签
     * @return
     */
    public static JSONObject connectDetrypt(String jsonStr, String key,String oldKey) {
        //解密内容
        String content = null;
        try {
            content = AESEncryptKit.detrypt(new String(ZLibUtils.decompress(Base64.base64ToByteArray(jsonStr))), key);
        } catch (BadPaddingException e) {
            if(oldKey==null){
                log.error("Detrypt fail：", e);
                return null;
            }
            try {
                    content = AESEncryptKit.detrypt(jsonStr, oldKey);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } catch (IllegalBlockSizeException e) {
            log.error("Detrypt fail：", e);
            e.printStackTrace();
//            throw SocketException.createError("服务器解密失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject params = JSONObject.parseObject(content,Feature.OrderedField);
        
        String signMsg = params.remove("signMsg") + "";
        String signSelf = MD5SignatoryKit.getMD5(JSONObject.toJSONString(params).getBytes());

        if(!signMsg.equals(signSelf)) {
            log.error("Validate signator fail：" + params);
//            throw SocketException.createError("服务器验签失败");
        } else {
//            log.debug("Validate signator success");
        }

        return params;
    }

//    /**
//     * 通讯加密和签名
//     * @return
//     */
//    public static String connectEncrypt(CommandResponse response,String key) {
//        if(key == null) {
//            throw new RuntimeException();
//        }
//
//        String json = JSONObject.toJSONString(response);
//        try {
//            String signMsg = MD5SignatoryKit.getMD5(json.getBytes());
//            response.setSignMsg(signMsg);
//            String signResponse = JSONObject.toJSONString(response);
//            log.debug("发送数据："+signResponse);
//            String encryMsg = AESEncryptKit.encrypt(signResponse, key);
//            return encryMsg.replace("\r\n", "");
//        } catch (Exception e) {
//            log.error(e.getMessage(),e);
//        }
//        return null;
//    }
    /**
     * 通讯加密和签名
     * @return
     */
    public static String connectEncrypt(CommandRequest request, String key) {
        if(key == null) {
            throw new RuntimeException();
        }

        String json = JSONObject.toJSONString(request);
        try {
            String signMsg = MD5SignatoryKit.getMD5(json.getBytes());
            request.setSignMsg(signMsg);
            String signResponse = JSONObject.toJSONString(request);
//            log.debug("发送数据："+signResponse);
            String encryMsg = AESEncryptKit.encrypt(signResponse, key);
            return encryMsg.replace("\r\n", "");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }
}
