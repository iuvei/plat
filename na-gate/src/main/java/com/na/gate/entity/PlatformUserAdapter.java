package com.na.gate.entity;

import com.na.gate.enums.PlatformUserAdapterType;

import java.util.Date;

/**
 * Created by sunny on 2017/7/24 0024.
 */
public class PlatformUserAdapter {
    private Long id;
    private Integer type;
    private String platformUserId;
    private Long liveUserId;
    private Date lastUpdateTime;
    private String platformUserName;
    private String platformParentId;
    
    @Override
    public String toString() {
    	return "liveUserId："+liveUserId+" platformUserId："+platformUserId+" platformUserName："+platformUserName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPlatformUserId() {
        return platformUserId;
    }

    public void setPlatformUserId(String platformUserId) {
        this.platformUserId = platformUserId;
    }

    public Long getLiveUserId() {
        return liveUserId;
    }

    public void setLiveUserId(Long liveUserId) {
        this.liveUserId = liveUserId;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getPlatformUserName() {
        return platformUserName;
    }

    public void setPlatformUserName(String platformUserName) {
        this.platformUserName = platformUserName;
    }

    public PlatformUserAdapterType getTypeEnum(){
        return PlatformUserAdapterType.get(this.type);
    }

    public String getPlatformParentId() {
        return platformParentId;
    }

    public void setPlatformParentId(String platformParentId) {
        this.platformParentId = platformParentId;
    }
}
