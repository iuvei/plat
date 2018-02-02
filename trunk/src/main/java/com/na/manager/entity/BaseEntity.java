package com.na.manager.entity;

import java.util.Date;

/**
 * @author andy
 * @date 2017年6月22日 上午11:45:46
 * 
 */
public class BaseEntity {

	protected Date createDateTime;

	protected Date updateDateTime;

	protected String createBy;

	protected String updateBy;

	public Date getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

	public Date getUpdateDateTime() {
		return updateDateTime;
	}

	public void setUpdateDateTime(Date updateDateTime) {
		this.updateDateTime = updateDateTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
}
