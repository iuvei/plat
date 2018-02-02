package com.na.roulette.socketserver.command.requestpara;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.baccarat.socketserver.common.enums.BetOrderBetTypeEnum;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;

/**
 * 下注请求参数。
 * Created by sunny on 2017/5/1 0001.
 */
public class BetPara extends CommandReqestPara {
	
	 public static class Bet{
        /**
         * 投注额。
         */
        public BigDecimal amount;
        /**
         * 下注数字
         */
        public String number;
        /**
         * 下注类型
         */
        public String key;
        /**
         * 交易项ID
         */
        public Integer tradeId;
    }
    /**
     *游戏桌ID
     */
    @JSONField(name = "tid")
    private Integer tableId;

    /**
     * 投注类型。
     * @see BetOrderBetTypeEnum
     */
    @JSONField(name = "betType")
    private Integer betType;

    /**
     * 限红ID。
     */
    @JSONField(name = "lid")
    private Integer chipsCid;
    
    /**
     * 投注项。
     */
    private List<Bet> bets;
    
    public Integer getChipsCid() {
		return chipsCid;
	}

	public void setChipsCid(Integer chipsCid) {
		this.chipsCid = chipsCid;
	}

	public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public List<Bet> getBets() {
        return bets;
    }

    public void setBets(List<Bet> bets) {
        this.bets = bets;
    }

    public Integer getBetType() {
        return betType;
    }

    public void setBetType(Integer betType) {
        this.betType = betType;
    }
}
