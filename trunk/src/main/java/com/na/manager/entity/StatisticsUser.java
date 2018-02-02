package com.na.manager.entity;

import java.io.Serializable;

/**
 * 
 * 
 * @author alan
 * @date 2017年12月19日 下午5:02:00
 */
public class StatisticsUser implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Long agentId;
	
	private Long agentName;
	
	private String agentParentPath;
	
	private Long onlineNumberUser;
	
	private Long registerNumberUser;
	
	private Long betNumberUser;
	
	private Long betNumberTotal;
	
	private Long createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public String getAgentParentPath() {
		return agentParentPath;
	}

	public void setAgentParentPath(String agentParentPath) {
		this.agentParentPath = agentParentPath;
	}

	public Long getOnlineNumberUser() {
		return onlineNumberUser;
	}

	public void setOnlineNumberUser(Long onlineNumberUser) {
		this.onlineNumberUser = onlineNumberUser;
	}

	public Long getRegisterNumberUser() {
		return registerNumberUser;
	}

	public void setRegisterNumberUser(Long registerNumberUser) {
		this.registerNumberUser = registerNumberUser;
	}

	public Long getBetNumberUser() {
		return betNumberUser;
	}

	public void setBetNumberUser(Long betNumberUser) {
		this.betNumberUser = betNumberUser;
	}

	public Long getBetNumberTotal() {
		return betNumberTotal;
	}

	public void setBetNumberTotal(Long betNumberTotal) {
		this.betNumberTotal = betNumberTotal;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getAgentName() {
		return agentName;
	}

	public void setAgentName(Long agentName) {
		this.agentName = agentName;
	}
	
	
}
