package com.na.manager.common.websocket;

import com.na.manager.entity.User;

import javax.websocket.Session;

/**
 * Created by Administrator on 2017/6/27 0027.
 */
public class Client {

    private Session session;
    private User user;

    public Client(Session session, User user) {
        this.session = session;
        this.user = user;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;

        Client client = (Client) o;

        if (getSession() != null ? !getSession().equals(client.getSession()) : client.getSession() != null)
            return false;
        return getUser() != null ? getUser().equals(client.getUser()) : client.getUser() == null;
    }

    @Override
    public int hashCode() {
        int result = getSession() != null ? getSession().hashCode() : 0;
        result = 31 * result + (getUser() != null ? getUser().hashCode() : 0);
        return result;
    }
}
