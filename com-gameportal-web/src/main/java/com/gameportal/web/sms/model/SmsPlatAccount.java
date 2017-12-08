package com.gameportal.web.sms.model;

import java.io.Serializable;
import java.util.Date;

import com.gameportal.web.user.model.BaseEntity;

/**
 * 短信平台信息。
 * 
 * @author sum
 *
 */
@SuppressWarnings("all")
public class SmsPlatAccount extends BaseEntity {
	private final static String TABLE_ALIAS = "SmsPlatAccount";
	private final static String ALIAS_SPAID = "短信平台ID";
	private final static String ALIAS_servername = "域名";
	private final static String ALIAS_serverport = "端口";
	private final static String ALIAS_account = "用户";
	private final static String ALIAS_pwd = "密码";
	private final static String ALIAS_STATUS = "状态";
	private final static String alias_createtime = "创建时间";
	private final static String alias_createname = "创建人";
	private final static String alias_updatetime = "修改时间";
	private final static String alias_updatename = "修改人";

	private Long spaid;
	private String servername;
	private String serverport;
	private String account;
	private String pwd;
	private Integer status;
	private Date createTime = new Date();
	private String createName;
	private Date updateTime = new Date();
	private String updateName;

	public Long getSpaid() {
		return spaid;
	}

	public void setSpaid(Long spaid) {
		this.spaid = spaid;
	}

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public String getServerport() {
		return serverport;
	}

	public void setServerport(String serverport) {
		this.serverport = serverport;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	@Override
	public Serializable getID() {
		return getSpaid();
	}
}
