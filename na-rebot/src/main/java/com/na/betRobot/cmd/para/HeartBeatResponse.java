package com.na.betRobot.cmd.para;

/**
 * 心跳命令
 * 
 * @author alan
 * @date 2017年5月4日 上午10:26:14
 */
public class HeartBeatResponse {
	
	private Long sn;
	
	private Long startTimeStamp;
	
	private Long endTimeStamp;
	
	private Object data;

	public Long getSn() {
		return sn;
	}
	public void setSn(Long sn) {
		this.sn = sn;
	}
	public Long getStartTimeStamp() {
		return startTimeStamp;
	}
	public void setStartTimeStamp(Long startTimeStamp) {
		this.startTimeStamp = startTimeStamp;
	}
	public Long getEndTimeStamp() {
		return endTimeStamp;
	}
	public void setEndTimeStamp(Long endTimeStamp) {
		this.endTimeStamp = endTimeStamp;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	
	
}
