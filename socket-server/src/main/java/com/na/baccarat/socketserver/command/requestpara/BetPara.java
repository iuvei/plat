package com.na.baccarat.socketserver.command.requestpara;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.baccarat.socketserver.common.enums.BetOrderBetTypeEnum;
import com.na.baccarat.socketserver.common.enums.BetOrderSourceEnum;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;

/**
 * 下注请求参数。
 * Created by sunny on 2017/5/1 0001.
 */
public class BetPara extends CommandReqestPara{
    /**
     * 投注来源：1 座位 、2 旁注、3 多台、4 好路
     * @see BetOrderSourceEnum
     */
    @JSONField(name = "source")
    private Integer source;

    /**
     *游戏桌ID
     */
    @JSONField(name = "tid")
    private Integer tableId;

    /**
     * 限红ID。
     */
    @JSONField(name = "lid")
    private Integer chipsCid;

//    /**
//     * 虚拟游戏桌ID
//     */
//    @JSONField(name = "vtid")
//    private Integer virtualGameTableId;

    /**
     * 投注类型。
     * @see com.na.baccarat.socketserver.common.enums.BetOrderBetTypeEnum
     */
    @JSONField(name = "betType")
    private Integer betType;

    /**
     * 投注项。
     */
    private List<Bet> bets;


    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

//    public Integer getVirtualGameTableId() {
//        return virtualGameTableId;
//    }
//
//    public void setVirtualGameTableId(Integer virtualGameTableId) {
//        this.virtualGameTableId = virtualGameTableId;
//    }

    public List<Bet> getBets() {
        return bets;
    }

    public void setBets(List<Bet> bets) {
        this.bets = bets;
    }

    public static class Bet{
        /**
         * 投注额。
         */
        public BigDecimal amount;
        /**
         * 交易项ID
         */
        public Integer tradeId;
    }

    public Integer getChipsCid() {
        return chipsCid;
    }

    public void setChipsCid(Integer chipsCid) {
        this.chipsCid = chipsCid;
    }

    @JSONField(serialize = false,deserialize = false)
    public BetOrderSourceEnum getSourceEnum(){
    	if(source == null) {
    		return null;
    	}
        return BetOrderSourceEnum.get(this.source);
    }

    public Integer getBetType() {
        return betType;
    }
    
    @JSONField(serialize = false,deserialize = false)
    public BetOrderBetTypeEnum getBetTypeEnum() {
    	if(betType == null) {
    		return null;
    	}
        return BetOrderBetTypeEnum.get(betType);
    }

    public void setBetType(Integer betType) {
        this.betType = betType;
    }
}
