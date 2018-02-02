package com.na.gate.vo;

/**
 *
 * Created by sunny on 2017/7/28 0028.
 */
public class PlatformManageLoginRequest {
    private String id;
    private Long timestamp;
    private String sign;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "PlatformManageLoginRequest{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", sign='" + sign + '\'' +
                '}';
    }
}
