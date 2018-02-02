package com.na.gate.proto.bean;

import com.na.gate.proto.base.MyField;
import com.na.gate.proto.base.Request;
import com.na.gate.proto.base.STypeEnum;

/**
* @author Andy
* @version 创建时间：2017年9月30日 下午2:45:29
*/
public class ExceptionLoginoutRequest implements Request {
    @MyField(order = 0,sourceType = STypeEnum.UINT32)
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
