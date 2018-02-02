package com.na.user.socketserver.common.event;

import java.util.Map;

import org.springframework.context.ApplicationEvent;

/**
 * Created by sunny on 2017/5/13 0013.
 */
public class UserBalanceChangeEvent extends ApplicationEvent{
    public UserBalanceChangeEvent(Map user) {
        super(user);
    }

    public Map getSource() {
        return (Map) super.getSource();
    }
}
