package com.na.gate.vo;

/**
 * Created by Administrator on 2017/8/17 0017.
 */
public class FindBetOrderRequestVO {
    /**
     * 平台用户ID
     */
    private String userId;
    //格式为：yyyyMMddHHmmss
    private String startTime;
    //格式为：yyyyMMddHHmmss
    private String endTime;
    private int pageSize;
    private int currentPage;
    private String sign;

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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
