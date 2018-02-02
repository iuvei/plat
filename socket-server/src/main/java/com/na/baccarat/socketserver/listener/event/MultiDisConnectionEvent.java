package com.na.baccarat.socketserver.listener.event;

import org.springframework.context.ApplicationEvent;

/**
 * 多台断线重连事件
 * 
 * @author alan
 * @date 2017年6月20日 下午3:43:26
 */
public class MultiDisConnectionEvent extends ApplicationEvent {
	
    public MultiDisConnectionEvent(Object userId) {
        super(userId);
    }

    public Object getSource() {
        return super.getSource();
    }
}
