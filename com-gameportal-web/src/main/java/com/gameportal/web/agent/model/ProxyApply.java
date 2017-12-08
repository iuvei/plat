package com.gameportal.web.agent.model;

import java.io.Serializable;
import java.util.Date;

import com.gameportal.web.user.model.BaseEntity;

/**
 * 前台代理申请。
 * 
 * @author sum
 *
 */
public class ProxyApply extends BaseEntity {
	private static final long serialVersionUID = -5933310635127687849L;
	public static final String TABLE_ALIAS = "WebProxyApply";
	public static final String ALIAS_AID = "注册申请编号";
	public static final String ALIAS_TRUENAME = "真实姓名";
	public static final String ALIAS_TELPHONE = "联系电话";
	public static final String ALIAS_QQSKYPE = "QQ/skype";
	public static final String ALIAS_EMAIL = "email邮箱";
	public static final String ALIAS_SPREADFS = "推广方式";
	public static final String ALIAS_APPLYSOURCE = "申请来源";
	public static final String ALIAS_APPLYTIME = "申请时间";
	public static final String ALIAS_STATUS = "状态 0：待审核 1:审核通过 2：审核不通过";
	public static final String ALIAS_DESC = "描述";

	private Long aid;
	private String trueName;
	private String telphone;
	private String qqskype;
	private String aemail;
	private String spreadfs;
	private String applySource;
	private Date applyTime = new Date();
	private Integer status;
	private String description;

	private String code; // 用户存放前台申请时验证码

	public ProxyApply() {
	}

	public Long getAid() {
		return aid;
	}

	public void setAid(Long aid) {
		this.aid = aid;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getQqskype() {
		return qqskype;
	}

	public void setQqskype(String qqskype) {
		this.qqskype = qqskype;
	}

	public String getAemail() {
		return aemail;
	}

	public void setAemail(String aemail) {
		this.aemail = aemail;
	}

	public String getSpreadfs() {
		return spreadfs;
	}

	public void setSpreadfs(String spreadfs) {
		this.spreadfs = spreadfs;
	}

	public String getApplySource() {
		return applySource;
	}

	public void setApplySource(String applySource) {
		this.applySource = applySource;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public Serializable getID() {
		return this.aid;
	}
}
