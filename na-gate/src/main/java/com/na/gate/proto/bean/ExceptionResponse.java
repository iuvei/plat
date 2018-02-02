package com.na.gate.proto.bean;

import com.na.gate.proto.base.MyField;
import com.na.gate.proto.base.Response;
import com.na.gate.proto.base.STypeEnum;

/**
 * Created by Administrator on 2017/7/24 0024.
 */
public class ExceptionResponse implements Response {
    @MyField(order = 0,sourceType = STypeEnum.INT32)
    private int cmd;

    @MyField(order = 1,sourceType = STypeEnum.UINT32)
    private long errCode;

    @MyField(order = 2)
    private String msg;
    
    @MyField(order = 3,sourceType = STypeEnum.UINT32)
    private long userId;
    
    @MyField(order = 4)
    private String userName;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
		return "ExceptionResponse{" + "cmd=" + cmd + ", errCode=" + errCode + ", msg='" + msg + '\'' + 
				",userId="+userId+",userName="+userName+'}';
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public long getErrCode() {
        return errCode;
    }

    public void setErrCode(long errCode) {
        this.errCode = errCode;
    }

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
