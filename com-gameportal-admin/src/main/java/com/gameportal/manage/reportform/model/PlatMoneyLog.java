package com.gameportal.manage.reportform.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 平台余额记录
 * @author Administrator
 *
 */
public class PlatMoneyLog extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -304096517212138568L;

	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 主键自动增长
	 */
	private java.lang.Long pmid;
	
	/**
	 * 平台余额
	 */
	private java.lang.String totalmoney; 
	
	/**
	 * 创建时间
	 */
	private java.util.Date create_date;

	public java.lang.Long getPmid() {
		return pmid;
	}
	public void setPmid(java.lang.Long pmid) {
		this.pmid = pmid;
	}
	public java.lang.String getTotalmoney() {
		return totalmoney;
	}
	public void setTotalmoney(java.lang.String totalmoney) {
		this.totalmoney = totalmoney;
	}
	public java.util.Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(java.util.Date create_date) {
		this.create_date = create_date;
	}

}
