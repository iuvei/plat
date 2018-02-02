package com.na.gate.vo;

public class PlatformUserToken {
    private String platformUserId;
    private Long liveUserId;
    private String authcode;

    public PlatformUserToken(String platformUserId, Long liveUserId, String authcode) {
        this.platformUserId = platformUserId;
        this.liveUserId = liveUserId;
        this.authcode = authcode;
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

    public String getAuthcode() {
        return authcode;
    }

    public void setAuthcode(String authcode) {
        this.authcode = authcode;
    }
}
