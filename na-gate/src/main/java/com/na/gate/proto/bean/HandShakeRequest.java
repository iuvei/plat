package com.na.gate.proto.bean;

import com.na.gate.proto.base.MyField;
import com.na.gate.proto.base.Request;

/**
 * 游戏服务器验证登录协议.
 * Created by sunny on 2017/7/22 0022.
 */
public class HandShakeRequest implements Request {
    @MyField(order = 0)
    private int serverId;
    @MyField(order = 1)
    private int gameId;
    @MyField(order = 2)
    private String serverName;
    @MyField(order = 3)
    private String authCode;
    @MyField(order = 4)
    private String gameNameEn;
    @MyField(order = 5)
    private String ip;
    @MyField(order = 6)
    private int port;
    @MyField(order = 7)
    private String gameNameCn;



    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getGameNameEn() {
        return gameNameEn;
    }

    public void setGameNameEn(String gameNameEn) {
        this.gameNameEn = gameNameEn;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getGameNameCn() {
        return gameNameCn;
    }

    public void setGameNameCn(String gameNameCn) {
        this.gameNameCn = gameNameCn;
    }
}
