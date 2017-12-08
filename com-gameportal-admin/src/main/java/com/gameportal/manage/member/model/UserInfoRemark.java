package com.gameportal.manage.member.model;

import java.io.Serializable;
import java.util.Date;

import com.gameportal.manage.system.model.BaseEntity;

public class UserInfoRemark extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long rid;
	
	/**
	 * 用户ID
	 */
	private long uiid;

	/**
	 * 添加人
	 */
	private String operator;

	/**
	 * 描述
	 */
	private String remark;

	/**
	 * 创建时间
	 */
	private Date createdate;

	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	public long getUiid() {
		return uiid;
	}

	public void setUiid(long uiid) {
		this.uiid = uiid;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	@Override
	public Serializable getID() {
		return this.rid;
	}
}
