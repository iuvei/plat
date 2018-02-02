package com.na.manager.bean;

import com.na.manager.common.annotation.I18NField;

/**
 * Created by sunny on 2017/6/17 0017.
 */
public abstract class LiveBetBean {
    @I18NField
    protected String gameName;
    @I18NField
    protected String tableName;
    //靴ID bid
    protected String bootId;
    //局号
    protected String roundNum;
    protected String roundStatus;
    protected Integer gameId;
    protected Integer roundId;
    protected Integer gameTableId;
    //投注人数
    protected Integer userNum = 0;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getBootId() {
        return bootId;
    }

    public void setBootId(String bootId) {
        this.bootId = bootId;
    }

    public String getRoundNum() {
        return roundNum;
    }

    public void setRoundNum(String roundNum) {
        this.roundNum = roundNum;
    }

    public String getRoundStatus() {
        return roundStatus;
    }

    public void setRoundStatus(String roundStatus) {
        this.roundStatus = roundStatus;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getRoundId() {
        return roundId;
    }

    public void setRoundId(Integer roundId) {
        this.roundId = roundId;
    }

    public Integer getGameTableId() {
        return gameTableId;
    }

    public void setGameTableId(Integer gameTableId) {
        this.gameTableId = gameTableId;
    }

    public Integer getUserNum() {
        return userNum;
    }

    public void setUserNum(Integer userNum) {
        this.userNum = userNum;
    }
}
