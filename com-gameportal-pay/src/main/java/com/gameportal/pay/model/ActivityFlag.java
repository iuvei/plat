package com.gameportal.pay.model;

import java.io.Serializable;


/**
 * 活动标示。
 * 
 * @author leron
 *
 */
public class ActivityFlag extends BaseEntity {
	private static final long serialVersionUID = 1L;
	// 主键标识
	private Long flagid;
	// 类型
	private Integer type;
	// 人数
	private Integer numbers;
	// 时间
	private String flagtime;
	// 时分秒
	private String hms;
	//会员ID
	private Long uiid;
	//活动ID
	private Integer acid;
	//活动分组
	private String acgroup;

	public Long getFlagid() {
		return flagid;
	}

	public void setFlagid(Long flagid) {
		this.flagid = flagid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getNumbers() {
		return numbers;
	}

	public void setNumbers(Integer numbers) {
		this.numbers = numbers;
	}

	public String getFlagtime() {
		return flagtime;
	}

	public void setFlagtime(String flagtime) {
		this.flagtime = flagtime;
	}

	public String getHms() {
		return hms;
	}

	public void setHms(String hms) {
		this.hms = hms;
	}
	
	public Long getUiid() {
		return uiid;
	}

	public void setUiid(Long uiid) {
		this.uiid = uiid;
	}

	public Integer getAcid() {
		return acid;
	}

	public void setAcid(Integer acid) {
		this.acid = acid;
	}

	public String getAcgroup() {
		return acgroup;
	}

	public void setAcgroup(String acgroup) {
		this.acgroup = acgroup;
	}

	@Override
	public Serializable getID() {
		return getFlagid();
	}
}
