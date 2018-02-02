package com.na.user.socketserver.common.event;

import org.springframework.context.ApplicationEvent;

import com.na.user.socketserver.command.sendpara.SelectTableResponse;

/**
 * Created by sunny on 2017/5/13 0013.
 */
public class SelectTableEvent extends ApplicationEvent {
	
    public SelectTableEvent(SelectTableResponse response) {
        super(response);
    }

    public SelectTableResponse getSource() {
        return (SelectTableResponse) super.getSource();
    }
}
