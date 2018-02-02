package com.na.user.socketserver.command.requestpara;

/**
 * 试玩请求参数
 * 
 * @author alan
 * @date 2017年6月5日 上午11:22:52
 */
public class HeartBeatPara extends CommandReqestPara {
	
	
    private Long startTimeStamp;
    


	public Long getStartTimeStamp() {
		return startTimeStamp;
	}

	public void setStartTimeStamp(Long startTimeStamp) {
		this.startTimeStamp = startTimeStamp;
	}
    
}
