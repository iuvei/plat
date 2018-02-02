package com.na.gate.proto.bean;

import com.alibaba.fastjson.JSONArray;
import com.na.gate.proto.base.MyField;
import com.na.gate.proto.base.Request;
import com.na.gate.proto.base.STypeEnum;
import com.na.gate.vo.SendAccountJson;

/**
 * Created by Administrator on 2017/8/1 0001.
 */
public class AccountRequest implements Request{
    @MyField(order = 0,sourceType = STypeEnum.UINT32)
    private long userId;
    @MyField(order = 1)
    private String data;

    public String getData() {
        return data;
    }

    public void setData(SendAccountJson sendAccountJson) {
        data = JSONArray.toJSONString(sendAccountJson);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
