package com.gameportal.manage.marketing.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

public class MarketingChannel extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2820393373197918132L;

	/**
	 * 渠道ID
	 */
	private Integer channelid;
	
	/**
	 * 渠道Key
	 */
	private String channelkey;
	
	/**
	 * 渠道名称
	 */
	private String channelname;
	
	/**
	 * 渠道值
	 */
	private String channelvalue;
	
	/**
	 * 渠道URL
	 */
	private String channelurl;
	
	/**
	 * 备注
	 */
	private String channelremark;
	
	/**
	 * 状态
	 * 0： 不正常
	 * 1：正常
	 */
	private int channelstatus;
	
	/**
	 * 日期
	 */
	private String channeltime;
	
	
	public Integer getChannelid() {
		return channelid;
	}


	public void setChannelid(Integer channelid) {
		this.channelid = channelid;
	}


	public String getChannelkey() {
		return channelkey;
	}


	public void setChannelkey(String channelkey) {
		this.channelkey = channelkey;
	}


	public String getChannelname() {
		return channelname;
	}


	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}


	public String getChannelvalue() {
		return channelvalue;
	}


	public void setChannelvalue(String channelvalue) {
		this.channelvalue = channelvalue;
	}


	public String getChannelurl() {
		return channelurl;
	}


	public void setChannelurl(String channelurl) {
		this.channelurl = channelurl;
	}


	public String getChannelremark() {
		return channelremark;
	}


	public void setChannelremark(String channelremark) {
		this.channelremark = channelremark;
	}


	public int getChannelstatus() {
		return channelstatus;
	}


	public void setChannelstatus(int channelstatus) {
		this.channelstatus = channelstatus;
	}


	public String getChanneltime() {
		return channeltime;
	}


	public void setChanneltime(String channeltime) {
		this.channeltime = channeltime;
	}
	
	@Override
	public String toString() {
		return "MarketingChannel [channelid=" + channelid + ", channelkey="
				+ channelkey + ", channelname=" + channelname
				+ ", channelvalue=" + channelvalue + ", channelurl="
				+ channelurl + ", channelremark=" + channelremark
				+ ", channelstatus=" + channelstatus + ", channeltime="
				+ channeltime + "]";
	}


	@Override
	public Serializable getID() {

		return this.getChannelid();
	}

}
