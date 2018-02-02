package com.na.test.batchbet.util;

import com.alibaba.fastjson.JSONObject;
import com.na.test.batchbet.common.BetLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2017/5/13 0013.
 */
public class JLogUtil {
    final static Logger betLogger = LoggerFactory.getLogger(JLogUtil.class);

    public static void bet(long userId, long balance,long time){
        BetLog betLog = new BetLog(userId,balance,time);
        betLogger.info(JSONObject.toJSONString(betLog));
    }
}
