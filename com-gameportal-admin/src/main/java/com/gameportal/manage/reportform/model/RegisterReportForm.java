package com.gameportal.manage.reportform.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 注册人数统计
 * @author Administrator
 *
 */
public class RegisterReportForm extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8161798790089308283L;
	
	/**
	 * 注册时间
	 */
	private String times;
	
	/**
	 * 总人数
	 */
	private Long counts;

	public String getTimes() {
		return times;
	}



	public void setTimes(String times) {
		this.times = times;
	}



	public Long getCounts() {
		return counts;
	}



	public void setCounts(Long counts) {
		this.counts = counts;
	}



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return null;
	}

}
