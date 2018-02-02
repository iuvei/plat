package com.na.user.socketserver.command.requestpara;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 请求命令参数对象。
 * Created by Sunny on 2017/4/27 0027.
 */
public abstract class CommandReqestPara {
    /**
     * 客户端 语言包
     */
    @JSONField(name = "lan")
    protected String language;

    public String getLanguage() {
        String temp = language==null ? "zh_cn" : language.toUpperCase();
        if(temp.indexOf("_")<0){
                temp = "zh_"+temp;
        }
        return temp;
    }

	public void setLanguage(String language) {
		this.language = language;
	}
    
    
}
