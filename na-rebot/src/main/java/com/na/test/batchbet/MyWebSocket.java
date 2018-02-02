package com.na.test.batchbet;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created by Administrator on 2017/5/12 0012.
 */
public class MyWebSocket {
    final static Logger logger = LoggerFactory.getLogger(MyWebSocket.class);
    public static void customerWebSocket(String uriStr)throws Exception{
        String SOCKET_VERSION_1 = "/socket.io/1/";
        URL url = new URL(uriStr+SOCKET_VERSION_1);
        URLConnection connection = url.openConnection();
        InputStream stream = connection.getInputStream();
        Scanner in = new Scanner(stream);
        String response = in.nextLine();
        String[] data = response.split(":");
        String sessionId = data[0];
        URI uri = URI.create(String.format("%s://%s:%d%s%s/%s","ws",url.getHost(),url.getPort(),SOCKET_VERSION_1,"websocket",sessionId));

        WebSocketClient webSocketClient = new MyWebSocket.MyWebsocketClient(uri);
        webSocketClient.connect();
        Thread.sleep(100);
        webSocketClient.send("test");
        System.out.println("res:"+response);
    }

    public static void main(String[] args) throws Exception{
        customerWebSocket("http://192.168.0.28:18080");
    }


    public static class MyWebsocketClient extends WebSocketClient{

        public MyWebsocketClient(URI serverURI) {
            super(serverURI);
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            logger.info("onOpen");
        }

        @Override
        public void onMessage(String message) {
            logger.info("onMessage:"+message);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            logger.info("onClose");
        }

        @Override
        public void onError(Exception ex) {
            logger.info("onError");
        }
    }
}
