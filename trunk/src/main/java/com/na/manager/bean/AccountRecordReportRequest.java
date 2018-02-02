package com.na.manager.bean;

import java.util.Date;

/**
 * 交易流水
 * 
 * @author andy
 * @date 2017年6月23日 上午9:49:53
 * 
 */
public class AccountRecordReportRequest extends PageCondition {
	private Date startTime;
	private Date endTime;
	private String userName;
	private String parentPath;
	private Long userId;
	private String flag;
	private Integer type;
	private Long roundId;
	private String proxyPath;
	private String merchantPath;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getRoundId() {
		return roundId;
	}

	public void setRoundId(Long roundId) {
		this.roundId = roundId;
	}

	public String getProxyPath() {
		return proxyPath;
	}

	public void setProxyPath(String proxyPath) {
		this.proxyPath = proxyPath;
	}

	public String getMerchantPath() {
		return merchantPath;
	}

	public void setMerchantPath(String merchantPath) {
		this.merchantPath = merchantPath;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}
