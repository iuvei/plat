package com.gameportal.web.user.model;

import java.io.Serializable;

/**
 * 会员洗码标示
 * @author Administrator
 *
 */
public class MemberClearingFlag extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 896818768979856046L;

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

	/**
	 * 平台
	 */
	private String platname;
	
	public Long getFlagid() {
		return flagid;
	}

	public String getPlatname() {
		return platname;
	}

	public void setPlatname(String platname) {
		this.platname = platname;
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
