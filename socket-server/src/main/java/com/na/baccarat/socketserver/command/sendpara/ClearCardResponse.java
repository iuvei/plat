package com.na.baccarat.socketserver.command.sendpara;

import com.na.user.socketserver.command.sendpara.IResponse;

/**
 * 清除以扫牌响应参数
 * 
 * @author alan
 * @date 2017年5月2日 上午10:22:29
 */
public class ClearCardResponse implements IResponse {
	private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
