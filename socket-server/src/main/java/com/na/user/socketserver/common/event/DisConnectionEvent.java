package com.na.user.socketserver.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * 断线重连事件
 * 
 * @author alan
 * @date 2017年6月20日 下午3:43:26
 */
public class DisConnectionEvent extends ApplicationEvent {
	
    public DisConnectionEvent(Object tableId) {
        super(tableId);
    }

    public Object getSource() {
        return super.getSource();
    }
}
