package com.na.manager.remote;

import java.io.Serializable;

/**
 * Created by sunny on 2017/8/16 0016.
 */
public class FindBetOrderRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long liveUserId;
    private Long roundId;
    //格式为：yyyyMMddHHmmss
    private String startTime;
    //格式为：yyyyMMddHHmmss
    private String endTime;
    private int pageSize;
    private int currentPage;
    private Long parentId;
    private String path;

    public Long getLiveUserId() {
        return liveUserId;
    }

    public void setLiveUserId(Long liveUserId) {
        this.liveUserId = liveUserId;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

	public Long getRoundId() {
		return roundId;
	}

	public void setRoundId(Long roundId) {
		this.roundId = roundId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
