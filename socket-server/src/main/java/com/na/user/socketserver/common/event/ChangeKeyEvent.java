package com.na.user.socketserver.common.event;

import org.springframework.context.ApplicationEvent;

import com.corundumstudio.socketio.SocketIOClient;

/**
 * Created by sunny on 2017/5/13 0013.
 */
public class ChangeKeyEvent extends ApplicationEvent {
	
    public ChangeKeyEvent(SocketIOClient client) {
        super(client);
    }

    public SocketIOClient getSource() {
        return (SocketIOClient) super.getSource();
    }
}
