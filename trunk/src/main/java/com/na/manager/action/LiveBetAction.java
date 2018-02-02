package com.na.manager.action;

import com.na.manager.bean.NaResponse;
import com.na.manager.common.annotation.Auth;
import com.na.manager.service.IBetOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 现场投注
 * Created by sunny on 2017/6/27 0027.
 */
@RestController
@Auth("SearchSiteBet")
@RequestMapping("/livebet")
public class LiveBetAction {
    @Autowired
    private IBetOrderService betOrderService;

    @GetMapping("/search")
    public NaResponse searchLiveBetInfo(){
        return NaResponse.createSuccess(betOrderService.queryLiveBet());
    }

    @GetMapping("/getTopBet/{roundId}")
    public NaResponse getTopBet(@PathVariable("roundId") Long roundId){
        return NaResponse.createSuccess(betOrderService.queryTopBet(roundId));
    }
}
