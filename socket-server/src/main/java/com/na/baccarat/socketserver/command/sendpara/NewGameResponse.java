package com.na.baccarat.socketserver.command.sendpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.IResponse;

/**
 * 新一局响应。
 * Created by sunny on 2017/5/1 0001.
 */
public class NewGameResponse implements IResponse {
    /**靴ID*/
    @JSONField(name = "bid")
    private String bootId;

    /**靴数*/
    @JSONField(name = "bn")
    private Integer bootNumber;

    /**局数*/
    @JSONField(name = "rn")
    private Integer roundNumber;

    /**局ID*/
    @JSONField(name = "rid")
    private Long roundId;

    /**游戏ID*/
    @JSONField(name = "gid")
    private Integer gameId;

    /**局状态*/
    @JSONField(name = "stu")
    private Integer roundStatus;

    /**gametableId*/
    @JSONField(name = "tid")
    private Integer gameTableId;

    /**
     * 桌子展示状态
     */
    private String showTableStatus;

    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

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

	public String getBootId() {
		return bootId;
	}

	public void setBootId(String bootId) {
		this.bootId = bootId;
	}

	public Integer getBootNumber() {
		return bootNumber;
	}

	public void setBootNumber(Integer bootNumber) {
		this.bootNumber = bootNumber;
	}

	public Integer getRoundNumber() {
		return roundNumber;
	}

	public void setRoundNumber(Integer roundNumber) {
		this.roundNumber = roundNumber;
	}

	public String getShowTableStatus() {
		return showTableStatus;
	}

	public void setShowTableStatus(String showTableStatus) {
		this.showTableStatus = showTableStatus;
	}
}
