package com.na.manager.bean;

/**
 * Created by sunny on 2017/6/19 0019.
 */
public class LiveBetOrder {
    private String agentName;
    private String loginName;
    private String tradeItemNames;
    private String amounts;
    private String betIps;
    private Long userId;
    private String tradeItemIds;
    private String betOrderIds;

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getTradeItemNames() {
        return tradeItemNames;
    }

    public void setTradeItemNames(String tradeItemNames) {
        this.tradeItemNames = tradeItemNames;
    }

    public String getAmounts() {
        return amounts;
    }

    public void setAmounts(String amounts) {
        this.amounts = amounts;
    }

    public String getBetIps() {
        return betIps;
    }

    public void setBetIps(String betIps) {
        this.betIps = betIps;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTradeItemIds() {
        return tradeItemIds;
    }

    public void setTradeItemIds(String tradeItemIds) {
        this.tradeItemIds = tradeItemIds;
    }

    public String getBetOrderIds() {
        return betOrderIds;
    }

    public void setBetOrderIds(String betOrderIds) {
        this.betOrderIds = betOrderIds;
    }
}
