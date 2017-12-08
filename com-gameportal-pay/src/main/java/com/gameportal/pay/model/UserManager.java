package com.gameportal.pay.model;

import java.io.Serializable;

/**
 * 电销用户管理
 * @author Administrator
 *
 */
public class UserManager extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6773278800662024277L;
	
	/**
	 * 用户ID
	 */
	private Long uiid;
	
	/**
	 * 对应电销ID
	 */
	private Long belongid;
	
	/**
	 * 玩家类型默认：slot电子游戏
	 * 
	 */
	private String payertype;
	
	/**
	 * 电销备注
	 */
	private String remark;
	
	/**
	 * 客户类型
	 * 1：维护会员
	 * 2：开发会员
	 */
	private int clienttype = 2;

	public Long getUiid() {
		return uiid;
	}

	public void setUiid(Long uiid) {
		this.uiid = uiid;
	}

	public Long getBelongid() {
		return belongid;
	}

	public void setBelongid(Long belongid) {
		this.belongid = belongid;
	}

	public String getPayertype() {
		return payertype;
	}

	public void setPayertype(String payertype) {
		this.payertype = payertype;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getClienttype() {
		return clienttype;
	}

	public void setClienttype(int clienttype) {
		this.clienttype = clienttype;
	}

	@Override
	public String toString() {
		return "UserManager [uiid=" + uiid + ", belongid=" + belongid
				+ ", payertype=" + payertype + ", remark=" + remark + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((belongid == null) ? 0 : belongid.hashCode());
		result = prime * result
				+ ((payertype == null) ? 0 : payertype.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((uiid == null) ? 0 : uiid.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserManager other = (UserManager) obj;
		if (belongid == null) {
			if (other.belongid != null)
				return false;
		} else if (!belongid.equals(other.belongid))
			return false;
		if (payertype == null) {
			if (other.payertype != null)
				return false;
		} else if (!payertype.equals(other.payertype))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (uiid == null) {
			if (other.uiid != null)
				return false;
		} else if (!uiid.equals(other.uiid))
			return false;
		return true;
	}


	@Override
	public Serializable getID() {
		return this.getUiid();
	}

}
