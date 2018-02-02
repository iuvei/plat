package com.na.manager.remote;

import java.io.Serializable;
import java.math.BigDecimal;

public class UpdateUserRequest implements Serializable{
    private String nickName;
    private String headPic;
    private Long userId;
    private BigDecimal washPercentage;
    private BigDecimal intoPercentage;


    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {

        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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
