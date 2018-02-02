package com.na.roulette.socketserver.command.sendpara;

import java.util.ArrayList;
import java.util.List;

import com.na.user.socketserver.command.sendpara.CommandResponse;

/**
 * 投注成功响应。
 * Created by sunny on 2017/5/3 0003.
 */
public class RouletteBetResponse extends CommandResponse {
	
    /**投注用户ID*/
	private Long userId;
    /**投注交易项列表*/
    private List<Integer> tradeItemIds = new ArrayList<>();


	public void addTradeItemId(Integer tradeItemId){
        this.tradeItemIds.add(tradeItemId);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Integer> getTradeItemIds() {
        return tradeItemIds;
    }

    public void setTradeItemIds(List<Integer> tradeItemIds) {
        this.tradeItemIds = tradeItemIds;
    }
}
