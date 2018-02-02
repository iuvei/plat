package com.na.manager.bean;

import java.util.List;
import java.util.Set;

/**
 * @author andy
 * @date 2017年6月24日 下午5:58:47
 * 
 */
public class IpBlackWhiteSearchRequest extends PageCondition {
	private String ip;
	private Long startNum;
	private Integer platType;
	private Integer ipType;
	private Set<String> ips;
	private List<Long> ids; 

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getStartNum() {
		return startNum;
	}

	public void setStartNum(Long startNum) {
		this.startNum = startNum;
	}

	public Integer getPlatType() {
		return platType;
	}

	public void setPlatType(Integer platType) {
		this.platType = platType;
	}

	public Integer getIpType() {
		return ipType;
	}

	public void setIpType(Integer ipType) {
		this.ipType = ipType;
	}

	public Set<String> getIps() {
		return ips;
	}

	public void setIps(Set<String> ips) {
		this.ips = ips;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
}
