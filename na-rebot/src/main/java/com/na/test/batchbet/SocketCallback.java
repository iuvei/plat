package com.na.test.batchbet;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIOException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/5/11 0011.
 */
public class SocketCallback implements IOCallback{
    @Override
    public void onDisconnect() {

    }

    @Override
    public void onConnect() {

    }

    @Override
    public void onMessage(String data, IOAcknowledge ack) {

    }

    @Override
    public void onMessage(JSONObject json, IOAcknowledge ack) {

    }

    @Override
    public void on(String event, IOAcknowledge ack, Object... args) {

    }

    @Override
    public void onError(SocketIOException socketIOException) {

    }
}
