package com.na.test.batchbet.common;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Administrator on 2017/4/28 0028.
 */
public class CommandResponse {
    @JSONField(ordinal = 0)
    private String msg;
    @JSONField(ordinal = 1)
    private String type;
    @JSONField(ordinal = 2)
    private Object body;
    @JSONField(ordinal = 3)
    private String signMsg;

    @JSONField(serialize = false)
    public RequestCommandEnum getTypeEnum(){
        return RequestCommandEnum.get(type);
    }
    

  

	public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

   

    /**
     * 返回成功的响应。
     * @param commandEnum
     * @param body
     * @return
     */
    public static CommandResponse createSuccess(RequestCommandEnum commandEnum, Object body){
        CommandResponse res = new CommandResponse();
        res.setType(commandEnum.get());
        res.setMsg(ResponseCommandEnum.OK.get());
        res.setBody(body);
        return res;
    }

    @Override
    public String toString() {
        return "CommandResponse{" +
                "msg='" + msg + '\'' +
                ", type='" + type + '\'' +
                ", body=" + body +
                ", signMsg='" + signMsg + '\'' +
                '}';
    }
}
