package com.gameportal.manage.smsplatform.model;

import java.io.Serializable;
import com.gameportal.manage.system.model.BaseEntity;

public class SmsPlatAccount extends BaseEntity {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1199339014662943692L;

	private Long spaid;
	
	/**
	 * 短信服务器地址
	 */
	private String servername;
	
	/**
	 * 服务器端口
	 */
	private String serverport;
	
	/**
	 * 账号
	 */
	private String account;
	
	/**
	 * 密码
	 */
	private String pwd;
	
	/**
	 * 状态 0：停用 1：启用
	 */
	private int status;
	
	/**
	 * 创建时间
	 */
	private String createtime;
	
	/**
	 * 创建人
	 */
	private String createname;
	
	/**
	 * 修改时间
	 */
	private String updatetime;
	
	/**
	 * 修改人
	 */
	private String updatename;

	public SmsPlatAccount() {
	}
	
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getCreatename() {
		return createname;
	}

	public void setCreatename(String createname) {
		this.createname = createname;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getUpdatename() {
		return updatename;
	}

	public void setUpdatename(String updatename) {
		this.updatename = updatename;
	}

	@Override
	public String toString() {
		return "SmsPlatAccount [spaid=" + spaid + ", servername=" + servername
				+ ", serverport=" + serverport + ", account=" + account
				+ ", pwd=" + pwd + ", status=" + status + ", createtime="
				+ createtime + ", createname=" + createname + ", updatetime="
				+ updatetime + ", updatename=" + updatename + "]";
	}

	@Override
	public Serializable getID() {

		return this.spaid;
	}
}
