package com.na.gate.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 大厅分配的服务器配置数据。
 * Created by sunny on 2017/8/15 0015.
 */
@Component
public class GateConfig {
    /**
     * 平台管理系统加密key。
     */
    @Value("${spring.na.platform.game.key}")
    private String platformGameKey;
    
    /**
     * 平台管理员ID。
     */
    @Value("${spring.na.platform.admin.root}")
    private Long platformAdminUserRoot;

    /**
     * 真人用户平台代理的根ID。
     */
    @Value("${spring.na.platform.proxy.root}")
    private Long platformProxyUserRoot;
    
    /**
     * 真人用户平台商户的根ID。
     */
    @Value("${spring.na.platform.merchant.root}")
    private Long platformMerchantRoot;
    
    /**
     * 真人用户平台代理的Path。
     */
    @Value("${spring.na.platform.proxy.path}")
    private String platformProxyUserPath;
    
    /**
     * 真人用户平台商户的Path。
     */
    @Value("${spring.na.platform.merchant.path}")
    private String platformMerchantPath;

    /**
     * 真人管理系统后台地址。
     */
    @Value("${spring.na.webbackoffice.url}")
    private String webbackofficeUrl;
    /**
     * 平台管理员ID。
     */
    @Value("${spring.na.platform.admin.id}")
    private String platformAdminId;

    /**
     * 与平台的心跳时间。
     */
    @Value("${spring.na.heartbeat.time}")
    private Long heartbeatTime;
    
    /**
     * 账单推送URL
     */
    @Value("${spring.na.settlement.url}")
    private String settlementUrl;
    
    /**
     * 游戏记录推送URL
     */
    @Value("${spring.na.record.url}")
    private String recordUrl;
    
    /**
     * 包房报表根节点
     */
    @Value("${spring.na.platform.room.root}")
    private Long roomRoot;
    
    /**
     * 大厅包房专线名称
     */
    @Value("${spring.na.platform.room.agent.name}")
    private String roomAgentName;
    
    /**
     * 是否可以修改洗码、占成比配置
     */
    @Value("${spring.na.platform.update.flag}")
    private Integer updateFlag;

    public String getPlatformGameKey() {
        return platformGameKey;
    }

    public void setPlatformGameKey(String platformGameKey) {
        this.platformGameKey = platformGameKey;
    }

    public Long getPlatformProxyUserRoot() {
		return platformProxyUserRoot;
	}

	public void setPlatformProxyUserRoot(Long platformProxyUserRoot) {
		this.platformProxyUserRoot = platformProxyUserRoot;
	}

	public Long getPlatformMerchantRoot() {
		return platformMerchantRoot;
	}

	public void setPlatformMerchantRoot(Long platformMerchantRoot) {
		this.platformMerchantRoot = platformMerchantRoot;
	}

	public String getWebbackofficeUrl() {
        return webbackofficeUrl;
    }

    public void setWebbackofficeUrl(String webbackofficeUrl) {
        this.webbackofficeUrl = webbackofficeUrl;
    }

    public String getPlatformAdminId() {
        return platformAdminId;
    }

    public void setPlatformAdminId(String platformAdminId) {
        this.platformAdminId = platformAdminId;
    }

    public Long getHeartbeatTime() {
        return heartbeatTime;
    }

    public void setHeartbeatTime(Long heartbeatTime) {
        this.heartbeatTime = heartbeatTime;
    }

	public Long getPlatformAdminUserRoot() {
		return platformAdminUserRoot;
	}

	public void setPlatformAdminUserRoot(Long platformAdminUserRoot) {
		this.platformAdminUserRoot = platformAdminUserRoot;
	}

	public String getSettlementUrl() {
		return settlementUrl;
	}

	public void setSettlementUrl(String settlementUrl) {
		this.settlementUrl = settlementUrl;
	}

	public String getRecordUrl() {
		return recordUrl;
	}

	public void setRecordUrl(String recordUrl) {
		this.recordUrl = recordUrl;
	}

	public Long getRoomRoot() {
		return roomRoot;
	}

	public void setRoomRoot(Long roomRoot) {
		this.roomRoot = roomRoot;
	}

	public String getRoomAgentName() {
		return roomAgentName;
	}

	public void setRoomAgentName(String roomAgentName) {
		this.roomAgentName = roomAgentName;
	}

	public String getPlatformProxyUserPath() {
		return platformProxyUserPath;
	}

	public void setPlatformProxyUserPath(String platformProxyUserPath) {
		this.platformProxyUserPath = platformProxyUserPath;
	}

	public String getPlatformMerchantPath() {
		return platformMerchantPath;
	}

	public void setPlatformMerchantPath(String platformMerchantPath) {
		this.platformMerchantPath = platformMerchantPath;
	}

	public Integer getUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(Integer updateFlag) {
		this.updateFlag = updateFlag;
	}
}
