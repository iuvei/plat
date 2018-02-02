package com.na.user.socketserver.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by sunny on 2017/5/13 0013.
 */
public class AbnormalGameResultEvent extends ApplicationEvent {
	
    public AbnormalGameResultEvent(Integer roundId) {
        super(roundId);
    }

    public Integer getSource() {
        return (Integer) super.getSource();
    }
}
