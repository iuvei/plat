package com.na.baccarat.socketserver.command.sendpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * 游戏桌状态状态
 * Created by Administrator on 2017/4/28 0028.
 */
public class TableStatus {
    /**
     * 局状态。0没有开始 1新一靴 3新一局 4开始投注 5截止投注 6结算完成 7CLOSE。可扩展
     */
    @JSONField(name = "stu")
    private Integer roundStatus;

    /**游戏桌ID*/
    @JSONField(name = "tid")
    private Integer gameTableId;

    /**游戏ID*/
    @JSONField(name = "gid")
    private Integer gameId;

    /**VIP用户能否进入：true 能 false 不能*/
    @JSONField(name = "thj")
    private Boolean vipUserAccess;

    /**语音用户能否进入：true 能 false 不能*/
    @JSONField(name = "spu")
    private Boolean speakerUserAccess;

    /***/
    @JSONField(name = "tms")
    private Long openMaxTime;

    /**剩余倒计时，单位：秒*/
    @JSONField(name = "ht")
    private Long countDownSeconds;


    public Integer getRoundStatus() {
        return roundStatus;
    }

    public void setRoundStatus(Integer roundStatus) {
        this.roundStatus = roundStatus;
    }

    public Integer getGameTableId() {
        return gameTableId;
    }

    public void setGameTableId(Integer gameTableId) {
        this.gameTableId = gameTableId;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Boolean getVipUserAccess() {
        return vipUserAccess;
    }

    public void setVipUserAccess(Boolean vipUserAccess) {
        this.vipUserAccess = vipUserAccess;
    }

    public Boolean getSpeakerUserAccess() {
        return speakerUserAccess;
    }

    public void setSpeakerUserAccess(Boolean speakerUserAccess) {
        this.speakerUserAccess = speakerUserAccess;
    }

    public Long getOpenMaxTime() {
        return openMaxTime;
    }

    public void setOpenMaxTime(Long openMaxTime) {
        this.openMaxTime = openMaxTime;
    }

    public Long getCountDownSeconds() {
        return countDownSeconds;
    }

    public void setCountDownSeconds(Long countDownSeconds) {
        this.countDownSeconds = countDownSeconds;
    }
}
