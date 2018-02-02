package com.na.gate.proto.bean;

import com.na.gate.proto.base.MyField;
import com.na.gate.proto.base.Request;
import com.na.gate.proto.base.STypeEnum;

/**
 * 玩家数据.
 * Created by sunny on 2017/7/26 0026.
 */
public class PlayerData implements Request{
    @MyField(order = 1,sourceType = STypeEnum.INT32) //verifyCode
    private int authCode;

    @MyField(order = 2,sourceType = STypeEnum.UINT32) //gameUserID
    private long userId;

    @MyField(order = 3)
    private String headPic; //headerUrl
    
    @MyField(order = 4)
    private String nickName; //nickname

    @MyField(order = 5)
    private String userName; //username

    @MyField(order = 6)
    private String parentId; //parentId

    @MyField(order = 7)
    private String balance; //balance
    
    @MyField(order = 8,sourceType = STypeEnum.INT32)
    private int line; //line
    
    @MyField(order = 9)
    private String liveMix; //vedioMix
    
    @MyField(order = 10,sourceType = STypeEnum.INT32) //清算标识
    private int flag;

    public int getAuthCode() {
        return authCode;
    }

    public void setAuthCode(int authCode) {
        this.authCode = authCode;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public String getLiveMix() {
		return liveMix;
	}

	public void setLiveMix(String liveMix) {
		this.liveMix = liveMix;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
