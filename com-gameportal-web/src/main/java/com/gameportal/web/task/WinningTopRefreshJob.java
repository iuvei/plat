package com.gameportal.web.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.user.service.IUserInfoService;

@Service
public class WinningTopRefreshJob {
    
    Logger logger = Logger.getLogger(WinningTopRefreshJob.class);
    
    @Resource(name = "redisServiceImpl")
    private IRedisService iRedisService = null;
    @Resource(name = "userInfoServiceImpl")
    private IUserInfoService userInfoService = null;
    
    private static final BigDecimal WIN_AMOUNT = new BigDecimal(2000);//中奖排行榜最低中奖金额
    private static final int LIMIT_NUM = 20;//中奖排行榜共显示条数
    
    //5分钟执行一次
    //@Scheduled(fixedDelay = 10*1000)
    public void winningTopRefresh(){
        /*****************加载中奖喜讯内容****************/
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("winAmount", WIN_AMOUNT);
        params.put("limitNum", LIMIT_NUM);
        ArrayList<String> winRecords = (ArrayList<String>) userInfoService.selWinningTopBetLog(params);
        iRedisService.addToRedis("winning_top_data", winRecords);
    }
}
