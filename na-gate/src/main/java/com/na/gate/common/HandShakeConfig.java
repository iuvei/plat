package com.na.gate.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 与大厅握手相关配置。
 * Created by sunny on 2017/8/15 0015.
 */
@Component
public class HandShakeConfig {
    @Value("${spring.na.handShake.authcode}")
    private String authCode;
    @Value("${spring.na.handShake.gameId}")
    private int gameId;
    @Value("${spring.na.handShake.gameName.cn}")
    private String gameNameCn;
    @Value("${spring.na.handShake.gameName.en}")
    private String gameNameEn;
    @Value("${spring.na.handShake.socketServer.ip}")
    private String socketServerIp;
    @Value("${spring.na.handShake.socketServer.port}")
    private int socketServerPort;
    @Value("${spring.na.handShake.socketServer.id}")
    private int serverId;
    @Value("${spring.application.name}")
    private String serverName;

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGameNameCn() {
        return gameNameCn;
    }

    public void setGameNameCn(String gameNameCn) {
        this.gameNameCn = gameNameCn;
    }

    public String getGameNameEn() {
        return gameNameEn;
    }

    public void setGameNameEn(String gameNameEn) {
        this.gameNameEn = gameNameEn;
    }

    public String getSocketServerIp() {
        return socketServerIp;
    }

    public void setSocketServerIp(String socketServerIp) {
        this.socketServerIp = socketServerIp;
    }

    public int getSocketServerPort() {
        return socketServerPort;
    }

    public void setSocketServerPort(int socketServerPort) {
        this.socketServerPort = socketServerPort;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
