package com.na.gate.proto.bean;

import com.alibaba.fastjson.JSONArray;
import com.na.gate.proto.base.MyField;
import com.na.gate.proto.base.Request;
import com.na.gate.vo.SendAccountJson;

/**
 * Created by Sunny on 2017/8/1 0001.
 */
public class BetOrderRequest implements Request{
    @MyField(order = 1)
    private String data;

    public BetOrderRequest(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(SendAccountJson sendAccountJson) {
        data = JSONArray.toJSONString(sendAccountJson);
    }
}
