package com.gameportal.web.user.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;


/**
 * 投注记录。
 * @author sum
 *
 */
public class BetLog extends BaseEntity{
    private static final long serialVersionUID = 1L;
    private Long pdid;
    private String account;
    private String platformcode;
    private String gamecode;
    private String gamename;
    private String gameCategory;
    private Date betdate;
    private String betno;
    private BigDecimal betamount;
    private BigDecimal profitamount;
    private String currency;
    private BigDecimal tormbrate;
    private BigDecimal finalamount;
    private String origin;
    private String gameResult;
    private String result;
    private BigDecimal validBetAmount;
    private Integer flag;
    private String playType;
    private String tableCode;
    private String inningsCode;
    private String loginIP;
    private BigDecimal beforeCerdit;
    private String round;
    private Date recalcuTime;
    private String roundNo;
    private String commission;

    public Long getPdid() {
        return pdid;
    }


    public void setPdid(Long pdid) {
        this.pdid = pdid;
    }


    public String getAccount() {
        return account;
    }


    public void setAccount(String account) {
        this.account = account;
    }


    public String getPlatformcode() {
        return platformcode;
    }


    public void setPlatformcode(String platformcode) {
        this.platformcode = platformcode;
    }


    public String getGamecode() {
        return gamecode;
    }


    public void setGamecode(String gamecode) {
        this.gamecode = gamecode;
    }


    public String getGamename() {
        return gamename;
    }


    public void setGamename(String gamename) {
        this.gamename = gamename;
    }


    public String getGameCategory() {
        return gameCategory;
    }


    public void setGameCategory(String gameCategory) {
        this.gameCategory = gameCategory;
    }


    public Date getBetdate() {
        return betdate;
    }


    public void setBetdate(Date betdate) {
        this.betdate = betdate;
    }


    public String getBetno() {
        return betno;
    }


    public void setBetno(String betno) {
        this.betno = betno;
    }


    public BigDecimal getBetamount() {
        return betamount;
    }


    public void setBetamount(BigDecimal betamount) {
        this.betamount = betamount;
    }


    public BigDecimal getProfitamount() {
        return profitamount;
    }


    public void setProfitamount(BigDecimal profitamount) {
        this.profitamount = profitamount;
    }


    public String getCurrency() {
        return currency;
    }


    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public BigDecimal getTormbrate() {
        return tormbrate;
    }


    public void setTormbrate(BigDecimal tormbrate) {
        this.tormbrate = tormbrate;
    }


    public BigDecimal getFinalamount() {
        return finalamount;
    }


    public void setFinalamount(BigDecimal finalamount) {
        this.finalamount = finalamount;
    }


    public String getOrigin() {
        return origin;
    }


    public void setOrigin(String origin) {
        this.origin = origin;
    }


    public String getGameResult() {
        return gameResult;
    }


    public void setGameResult(String gameResult) {
        this.gameResult = gameResult;
    }


    public String getResult() {
        return result;
    }


    public void setResult(String result) {
        this.result = result;
    }


    public BigDecimal getValidBetAmount() {
        return validBetAmount;
    }


    public void setValidBetAmount(BigDecimal validBetAmount) {
        this.validBetAmount = validBetAmount;
    }


    public Integer getFlag() {
        return flag;
    }


    public void setFlag(Integer flag) {
        this.flag = flag;
    }


    public String getPlayType() {
        return playType;
    }


    public void setPlayType(String playType) {
        this.playType = playType;
    }


    public String getTableCode() {
        return tableCode;
    }


    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }


    public String getInningsCode() {
        return inningsCode;
    }


    public void setInningsCode(String inningsCode) {
        this.inningsCode = inningsCode;
    }


    public String getLoginIP() {
        return loginIP;
    }


    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }


    public BigDecimal getBeforeCerdit() {
        return beforeCerdit;
    }


    public void setBeforeCerdit(BigDecimal beforeCerdit) {
        this.beforeCerdit = beforeCerdit;
    }


    public String getRound() {
        return round;
    }


    public void setRound(String round) {
        this.round = round;
    }


    public Date getRecalcuTime() {
        return recalcuTime;
    }


    public void setRecalcuTime(Date recalcuTime) {
        this.recalcuTime = recalcuTime;
    }


    public String getRoundNo() {
        return roundNo;
    }


    public void setRoundNo(String roundNo) {
        this.roundNo = roundNo;
    }


    public String getCommission() {
        return commission;
    }


    public void setCommission(String commission) {
        this.commission = commission;
    }


    @Override
    public Serializable getID() {
        return this.pdid;
    }

    //将用户名打码
    public String getEncodeAccount() {
        if (StringUtils.isEmpty(this.account)) {
            return "";
        }
        return this.account.substring(0, 2) + "***" + this.account.substring(this.account.length() - 1);
    }

}
