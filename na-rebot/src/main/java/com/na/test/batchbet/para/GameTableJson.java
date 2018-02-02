package com.na.test.batchbet.para;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by sunny on 2017/5/4 0004.
 */
public class GameTableJson {
	
	private Logger log = LoggerFactory.getLogger(GameTableJson.class);
	
    @JSONField(name = "tid")
    private Integer id;

    /**当前局开始时间*/
    private Date startTime;

    /**荷官昵称*/
    private String dealerNickName;

    /**荷官头像*/
    private String dealerHeadPic;
    
    @JSONField(name = "gid")
    private Integer gameId;

    @JSONField(name = "tableName")
    private String name;

    /**是否多台 0 否 1 是*/
    private Integer isMany;
    /**是否竞咪 0 否 1 是*/
    private Integer isBegingMi;
    /**
     * 游戏状态 0正常1关闭
     */
    @JSONField(name = "stu")
    private Integer status;

    /**
     * 剩余秒数。
     */
    @JSONField(name = "timeLeft")
    private Long timeLeft;

    /**
     * 桌子即时状态。
     */
    @JSONField(name = "instantState")
    private Integer instantState;
    /**
     * 下注时间
     */
    private Integer countDownSeconds;
    /**
	 * @see com.na.test.batchbet.common.GameTableTypeEnum
	 */
    @JSONField(name = "type")
    private Integer type;

    /**该桌的当前靴的所有局结果*/
    private List<String> resultRes;
    
    /**
     * 该实体桌当前人数
     */
    private Integer playerNumber;

    public GameTableJson() {
    }

    public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getGameId() {
		return gameId;
	}


	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getInstantState() {
        return instantState;
    }

    public void setInstantState(Integer instantState) {
        this.instantState = instantState;
    }

    public Integer getCountDownSeconds() {
        return countDownSeconds;
    }

    public void setCountDownSeconds(Integer countDownSeconds) {
        this.countDownSeconds = countDownSeconds;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsMany() {
        return isMany;
    }

    public void setIsMany(Integer isMany) {
        this.isMany = isMany;
    }

    public Integer getIsBegingMi() {
        return isBegingMi;
    }

    public void setIsBegingMi(Integer isBegingMi) {
        this.isBegingMi = isBegingMi;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Long getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Long timeLeft) {
        this.timeLeft = timeLeft;
    }

    public List<String> getResultRes() {
        return resultRes;
    }

    public void setResultRes(List<String> resultRes) {
        this.resultRes = resultRes;
    }

    public void addResultRes(String res){
        if(this.resultRes==null){
            this.resultRes = new ArrayList<>();
        }
        this.resultRes.add(res);
    }

    public String getDealerNickName() {
        return dealerNickName;
    }

    public void setDealerNickName(String dealerNickName) {
        this.dealerNickName = dealerNickName;
    }

    public String getDealerHeadPic() {
        return dealerHeadPic;
    }

    public void setDealerHeadPic(String dealerHeadPic) {
        this.dealerHeadPic = dealerHeadPic;
    }

	public Integer getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(Integer playerNumber) {
		this.playerNumber = playerNumber;
	}
    
}
