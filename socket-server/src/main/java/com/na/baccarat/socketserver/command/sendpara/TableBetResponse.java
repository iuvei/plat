package com.na.baccarat.socketserver.command.sendpara;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.IResponse;

/**
 * Created by Administrator on 2017/5/3 0003.
 */
public class TableBetResponse implements IResponse {
	
	/**
	 * 交易项信息
	 */
	public class TradeInfo {
		/**
		 * 交易项ID
		 */
		public Integer item;
		/**
		 * 下注总额
		 */
		public BigDecimal number;
	}
	
    /**每个交易项下注总额*/
    private List<TradeInfo> tradeItemBetMoney;
    /**下注信息*/
    private Bet bet;

    public List<TradeInfo> getTradeItemBetMoney() {
		return tradeItemBetMoney;
	}

	public void setTradeItemBetMoney(List<TradeInfo> tradeItemBetMoney) {
		this.tradeItemBetMoney = tradeItemBetMoney;
	}

	public Bet getBet() {
        return bet;
    }

	public void setBet(Bet bet) {
        this.bet = bet;
    }

	public static class Bet{
        public Long userId;
        /**下注总额*/
        public BigDecimal betAmount;
        /**下注后用户余额*/
        public BigDecimal balance;
    }
}
