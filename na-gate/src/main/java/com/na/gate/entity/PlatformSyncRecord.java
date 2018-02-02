package com.na.gate.entity;

/**
 * 同步记录。
 * Created by sunny on 2017/8/22 0022.
 */
public class PlatformSyncRecord {
    private long id;
    private String startTime;
    private String endTime;

    public PlatformSyncRecord() {
    }

    public PlatformSyncRecord(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
