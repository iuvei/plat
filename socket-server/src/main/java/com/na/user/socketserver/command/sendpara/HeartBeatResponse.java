package com.na.user.socketserver.command.sendpara;

/**
 * 心跳命令
 * 
 * @author alan
 * @date 2017年5月4日 上午10:26:14
 */
public class HeartBeatResponse implements IResponse{
	
	private Long startTimeStamp;
	
	private Long endTimeStamp;
	
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

	
	
}
