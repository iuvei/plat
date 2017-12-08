package com.gameportal.manage.fcrecord.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 
 * @author sum 首存回访记录。
 */
public class FcRecord extends BaseEntity {

	private static final long serialVersionUID = 1L;
	// 主键ID
	private Long rid;
	// 游戏账号
	private String account;
	// 真实姓名
	private String username;
	// 代理用户名
	private String pname;
	// 金额
	private String money;
	// 电话号码
	private String phone;
	// 充值时间
	private String paytime;
	// 回访人ID
	private Long visitorId;
	// 访问人
	private String visitor;
	// 回访时间
	private String vistortime;
	// 描述
	private String remark;

	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPaytime() {
		return paytime;
	}

	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}

	public Long getVisitorId() {
		return visitorId;
	}

	public void setVisitorId(Long visitorId) {
		this.visitorId = visitorId;
	}

	public String getVisitor() {
		return visitor;
	}

	public void setVisitor(String visitor) {
		this.visitor = visitor;
	}

	public String getVistortime() {
		return vistortime;
	}

	public void setVistortime(String vistortime) {
		this.vistortime = vistortime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public Serializable getID() {
		return getRid();
	}
}
