package com.na.gate.entity;

/**
 * 同步记录。 Created by sunny on 2017/8/22 0022.
 */
public class SyncBetOrderFailRecord {
	private long roundId;
	private String createTime;

	public long getRoundId() {
		return roundId;
	}

	public void setRoundId(long roundId) {
		this.roundId = roundId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
