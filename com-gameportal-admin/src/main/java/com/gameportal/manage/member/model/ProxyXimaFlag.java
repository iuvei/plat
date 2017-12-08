package com.gameportal.manage.member.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 代理每日洗码标示
 * @author Administrator
 *
 */
public class ProxyXimaFlag extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2695543744270245440L;

	/**
	 * 
	 */
	private Long flagid; 
	
	/**
	 * 对应用户ID
	 */
	private Integer flaguiid;
	
	/**
	 * 洗码日期
	 */
	private String flagtime;

	
	public Long getFlagid() {
		return flagid;
	}


	public void setFlagid(Long flagid) {
		this.flagid = flagid;
	}


	public Integer getFlaguiid() {
		return flaguiid;
	}


	public void setFlaguiid(Integer flaguiid) {
		this.flaguiid = flaguiid;
	}


	public String getFlagtime() {
		return flagtime;
	}


	public void setFlagtime(String flagtime) {
		this.flagtime = flagtime;
	}


	@Override
	public String toString() {
		return "MemberClearingFlag [flagid=" + flagid + ", flaguiid="
				+ flaguiid + ", flagtime=" + flagtime + "]";
	}


	@Override
	public Serializable getID() {
		return this.getFlagid();
	}

}
