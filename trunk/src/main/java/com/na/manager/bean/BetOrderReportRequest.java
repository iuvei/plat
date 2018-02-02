package com.na.manager.bean;

import java.util.Date;

/**
 * 投注记录
 * 
 * @author andy
 * @date 2017年6月23日 上午9:39:10
 * 
 */
public class BetOrderReportRequest extends PageCondition {
	private Date startTime;
	private Date endTime;
	private String agentName;
	private Integer tableId;
	private String betNo;
	private Integer multipleFlag;
	private Integer counterfeitFlag;
	private String path;
	private String paths;///2/5/11/和/2/6/10/联查
	private Long parentId;
	private Long userId;
	private Integer gameId;
	private Integer roundId;
	private Integer playId;
	//最后更新时间：开始
	private Date lastUpdateTimeStart;
	//最后更新时间：结束
	private Date lastUpdateTimeEnd;

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

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public Integer getMultipleFlag() {
		return multipleFlag;
	}

	public void setMultipleFlag(Integer multipleFlag) {
		this.multipleFlag = multipleFlag;
	}

	public Integer getCounterfeitFlag() {
		return counterfeitFlag;
	}

	public void setCounterfeitFlag(Integer counterfeitFlag) {
		this.counterfeitFlag = counterfeitFlag;
	}

	public String getBetNo() {
		return betNo;
	}

	public void setBetNo(String betNo) {
		this.betNo = betNo;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getLastUpdateTimeStart() {
		return lastUpdateTimeStart;
	}

	public void setLastUpdateTimeStart(Date lastUpdateTimeStart) {
		this.lastUpdateTimeStart = lastUpdateTimeStart;
	}

	public Date getLastUpdateTimeEnd() {
		return lastUpdateTimeEnd;
	}

	public void setLastUpdateTimeEnd(Date lastUpdateTimeEnd) {
		this.lastUpdateTimeEnd = lastUpdateTimeEnd;
	}

	public Integer getRoundId() {
		return roundId;
	}

	public void setRoundId(Integer roundId) {
		this.roundId = roundId;
	}

	public Integer getPlayId() {
		return playId;
	}

	public void setPlayId(Integer playId) {
		this.playId = playId;
	}

	public String getPaths() {
		return paths;
	}

	public void setPaths(String paths) {
		this.paths = paths;
	}
}
