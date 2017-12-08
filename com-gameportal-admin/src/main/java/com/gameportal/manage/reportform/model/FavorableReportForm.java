package com.gameportal.manage.reportform.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 优惠金额报表
 * 洗码
 * @author Administrator
 *
 */
public class FavorableReportForm extends BaseEntity{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8008608133748533382L;

	/**
	 * 时间
	 */
	private String times;
	
	/**
	 * 优惠金额
	 */
	private String favorabTotal;
	
	public String getTimes() {
		return times;
	}

	public void setTime(String times) {
		this.times = times;
	}

	public String getFavorabTotal() {
		return favorabTotal;
	}

	public void setFavorabTotal(String favorabTotal) {
		this.favorabTotal = favorabTotal;
	}

	@Override
	public String toString() {
		return "FavorableReportForm [times=" + times + ", favorabTotal="
				+ favorabTotal + "]";
	}

	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return null;
	}

}
