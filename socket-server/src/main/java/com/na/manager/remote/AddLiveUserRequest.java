package com.na.manager.remote;

import java.io.Serializable;
import java.math.BigDecimal;

public class AddLiveUserRequest implements Serializable{
	private String loginName;
    private String nickName;
    private String headPic;
    private Long parentId;
    private boolean isPlayer;
    private Integer source;
    private BigDecimal washPercentage;
    private BigDecimal intoPercentage;    

    public AddLiveUserRequest(String loginName, String nickName, String headPic, Long parentId, boolean isPlayer,
    		  Integer source,BigDecimal washPercentage, BigDecimal intoPercentage) {
		this.loginName = loginName;
		this.nickName = nickName;
		this.headPic = headPic;
		this.parentId = parentId;
		this.isPlayer = isPlayer;
		this.source = source;
		this.washPercentage = washPercentage;
		this.intoPercentage = intoPercentage;
	}

	public AddLiveUserRequest() {
    }

    public String getLoginName() {
        return loginName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getHeadPic() {
        return headPic;
    }

    public Long getParentId() {
        return parentId;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setPlayer(boolean player) {
        isPlayer = player;
    }
    
	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public BigDecimal getWashPercentage() {
		return washPercentage;
	}

	public void setWashPercentage(BigDecimal washPercentage) {
		this.washPercentage = washPercentage;
	}

	public BigDecimal getIntoPercentage() {
		return intoPercentage;
	}

	public void setIntoPercentage(BigDecimal intoPercentage) {
		this.intoPercentage = intoPercentage;
	}
}
