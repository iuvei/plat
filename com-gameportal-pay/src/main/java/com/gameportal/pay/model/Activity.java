package com.gameportal.pay.model;

import java.io.Serializable;

/**
 * 活动配置类
 * @author Administrator
 *
 */
public class Activity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7097567657572025276L;
	
	/**
	 * 主键ID
	 */
	private Integer aid;
	
	/**
	 * 活动类型
	 * 默认1
	 * 1：首存送活动
	 * 2：次次存活动
	 */
	private String hdtype = "1";
	
	/**
	 * 活动编号
	 */
	private String hdnumber;
	
	/**
	 * 活动标题
	 */
	private String hdtext;
	
	/**
	 * 活动赠送比例百分比单位
	 * 10表示10%
	 */
	private String hdscale;
	
	/**
	 * 流水倍数
	 */
	private String multiple;
	
	/**
	 * 此活动是否给洗码
	 * 1:可以洗码
	 * 0：不可以洗码
	 */
	private int isxima = 1;
	
	/**
	 * 简短的注意事项
	 * 例如什么游戏不能玩等等
	 */
	private String notecontext;
	
	/**
	 * 状态
	 * 0：未启用
	 * 1：正常
	 */
	private int status;
	
	/**
	 * 活动添加或更新时间
	 */
	private String uptime;
	
	/**
	 * 赠送最大金额
	 */
	private String maxmoney;
	
	/**
	 * 赠送最小金额
	 */
	private String minmoney;
	
	/**
	 * 活动规则
	 */
	private String hdrule;
	
	/**
	 * 活动分组 
	 */
	private String acgroup;
	
	/**
	 * 赠送金额
	 */
	private String rewmoney;
	
	

	public String getAcgroup() {
		return acgroup;
	}



	public void setAcgroup(String acgroup) {
		this.acgroup = acgroup;
	}



	public String getRewmoney() {
		return rewmoney;
	}



	public void setRewmoney(String rewmoney) {
		this.rewmoney = rewmoney;
	}



	public Integer getAid() {
		return aid;
	}



	public void setAid(Integer aid) {
		this.aid = aid;
	}



	public String getHdtype() {
		return hdtype;
	}



	public void setHdtype(String hdtype) {
		this.hdtype = hdtype;
	}



	public String getHdnumber() {
		return hdnumber;
	}



	public void setHdnumber(String hdnumber) {
		this.hdnumber = hdnumber;
	}



	public String getHdtext() {
		return hdtext;
	}



	public void setHdtext(String hdtext) {
		this.hdtext = hdtext;
	}



	public String getHdscale() {
		return hdscale;
	}



	public void setHdscale(String hdscale) {
		this.hdscale = hdscale;
	}



	public String getMultiple() {
		return multiple;
	}



	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}



	public int getIsxima() {
		return isxima;
	}



	public void setIsxima(int isxima) {
		this.isxima = isxima;
	}



	public String getNotecontext() {
		return notecontext;
	}



	public void setNotecontext(String notecontext) {
		this.notecontext = notecontext;
	}



	public int getStatus() {
		return status;
	}



	public void setStatus(int status) {
		this.status = status;
	}



	public String getUptime() {
		return uptime;
	}



	public void setUptime(String uptime) {
		this.uptime = uptime;
	}



	public String getMaxmoney() {
		return maxmoney;
	}



	public void setMaxmoney(String maxmoney) {
		this.maxmoney = maxmoney;
	}



	public String getMinmoney() {
		return minmoney;
	}



	public void setMinmoney(String minmoney) {
		this.minmoney = minmoney;
	}

	public String getHdrule() {
		return hdrule;
	}
	
	public void setHdrule(String hdrule) {
		this.hdrule = hdrule;
	}



	@Override
	public String toString() {
		return "Activity [aid=" + aid + ", hdtype=" + hdtype + ", hdnumber="
				+ hdnumber + ", hdtext=" + hdtext + ", hdscale=" + hdscale
				+ ", multiple=" + multiple + ", isxima=" + isxima
				+ ", notecontext=" + notecontext + ", status=" + status
				+ ", uptime=" + uptime + ", maxmoney=" + maxmoney
				+ ", minmoney=" + minmoney + "]";
	}



	@Override
	public Serializable getID() {
		
		return this.getAid();
	}

}
