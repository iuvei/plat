package com.na.gate.proto.bean;

import java.math.BigDecimal;

import com.na.gate.enums.PlatformUserAdapterType;
import com.na.gate.proto.base.Request;

/**
 * Created by sunny on 2017/7/29 0029.
 */
public class MerchantJson implements Request {
    private String id;
    private String username;
    private String nickname;
    private String parentId;
    //超级管理员 1 线路商 10 商户 100 代理1000 玩家 10000
    private String role;
    private String headPic;
    // 洗码比
    private BigDecimal liveMix;
    // 占成比
    private BigDecimal rate;
    
    private String sign;
    
    private String levelIndex;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public PlatformUserAdapterType getType(){
        switch (role){
            case "1":
                return PlatformUserAdapterType.ADMIN;
            case "10":
                return PlatformUserAdapterType.LINE;
            case "100":
                return PlatformUserAdapterType.MERCHANT;
            case "1000":
                return PlatformUserAdapterType.PROXY;
            case "10000":
                return PlatformUserAdapterType.PLAYER;
            default:
                return PlatformUserAdapterType.UNKNOWN;
        }
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
    
	public BigDecimal getLiveMix() {
		return liveMix;
	}

	public void setLiveMix(BigDecimal liveMix) {
		this.liveMix = liveMix;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public String getLevelIndex() {
		return levelIndex;
	}

	public void setLevelIndex(String levelIndex) {
		this.levelIndex = levelIndex;
	}
}
