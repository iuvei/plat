package com.na.test.batchbet.common;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Administrator on 2017/4/28 0028.
 */
public class CommandRequest {
    @JSONField(ordinal = 1)
    private String cmd;
    @JSONField(ordinal = 2)
    private Object body;
    @JSONField(ordinal = 3)
    private String signMsg;

    private String msg;
    private String test;

    @JSONField(serialize = false)
    public RequestCommandEnum getCmdEnum(){
        return RequestCommandEnum.get(cmd);
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getSignMsg() {
        return signMsg;
    }

    public void setSignMsg(String signMsg) {
        this.signMsg = signMsg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
