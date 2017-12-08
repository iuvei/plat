package com.gameportal.manage.proxy.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 代理结算标示对象
 * @author Administrator
 *
 */
public class ProxyClearingEntity extends BaseEntity{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -7175533805951575946L;
	
	/**
	 * 标示ID
	 */
	private Long clearingflagid;
	
	/**
	 * 对应用户ID
	 */
	private Integer clearingflaguiid;
	
	/**
	 * 年份
	 */
	private String clearingflagyear;
	
	/**
	 * 结算月份
	 */
	private String clearingflagmonth;
	
	/**
	 * 结算类型
	 * 1：输值结算
	 * 2：洗码结算
	 */
	private int clearingflagtype;
	
	/**
	 * 结算时间
	 */
	private String clearingflagtime;

	
	public Long getClearingflagid() {
		return clearingflagid;
	}


	public void setClearingflagid(Long clearingflagid) {
		this.clearingflagid = clearingflagid;
	}


	public Integer getClearingflaguiid() {
		return clearingflaguiid;
	}


	public void setClearingflaguiid(Integer clearingflaguiid) {
		this.clearingflaguiid = clearingflaguiid;
	}


	public String getClearingflagyear() {
		return clearingflagyear;
	}


	public void setClearingflagyear(String clearingflagyear) {
		this.clearingflagyear = clearingflagyear;
	}


	public String getClearingflagmonth() {
		return clearingflagmonth;
	}


	public void setClearingflagmonth(String clearingflagmonth) {
		this.clearingflagmonth = clearingflagmonth;
	}


	public int getClearingflagtype() {
		return clearingflagtype;
	}


	public void setClearingflagtype(int clearingflagtype) {
		this.clearingflagtype = clearingflagtype;
	}


	public String getClearingflagtime() {
		return clearingflagtime;
	}


	public void setClearingflagtime(String clearingflagtime) {
		this.clearingflagtime = clearingflagtime;
	}

	

	@Override
	public String toString() {
		return "ProxyClearingEntity [clearingflagid=" + clearingflagid
				+ ", clearingflaguiid=" + clearingflaguiid
				+ ", clearingflagmonth=" + clearingflagmonth
				+ ", clearingflagtype=" + clearingflagtype
				+ ", clearingflagtime=" + clearingflagtime + "]";
	}


	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.getClearingflagid();
	}

}
