package com.na.gate.proto.bean;

import com.na.gate.proto.base.MyField;
import com.na.gate.proto.base.Request;
import com.na.gate.proto.base.STypeEnum;

/**
 * 玩家进入游戏服务器协议.
 * Created by sunny on 2017/7/24 0024.
 */
public class PlayerLoginRequest implements Request {
    @MyField(order = 0,sourceType = STypeEnum.UINT32)
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
