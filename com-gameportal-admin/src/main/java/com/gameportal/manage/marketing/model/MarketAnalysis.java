package com.gameportal.manage.marketing.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 市场分析
 * @author Administrator
 *
 */
public class MarketAnalysis extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8320380305791776507L;
	
	/**
	 * 推广渠道
	 */
	private String channel;
	
	/**
	 * 注册人数
	 */
	private String regcount = "0";
	
	/**
	 * 充值人数
	 */
	private String paycount = "0";
	
	/**
	 * 充值金额
	 */
	private String payamont = "0";
	
	

	public String getChannel() {
		return channel;
	}



	public void setChannel(String channel) {
		this.channel = channel;
	}



	public String getRegcount() {
		return regcount;
	}



	public void setRegcount(String regcount) {
		this.regcount = regcount;
	}



	public String getPaycount() {
		return paycount;
	}



	public void setPaycount(String paycount) {
		this.paycount = paycount;
	}



	public String getPayamont() {
		return payamont;
	}



	public void setPayamont(String payamont) {
		this.payamont = payamont;
	}



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return null;
	}

}
