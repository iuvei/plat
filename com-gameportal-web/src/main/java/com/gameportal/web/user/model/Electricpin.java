package com.gameportal.web.user.model;

/**
 * 指定域名分配
 * @author Administrator
 *
 */
public class Electricpin{

	/**
	 * 域名ID
	 */
	private String domain;
	
	/**
	 * 域名ID
	 */
	private String eid;
	
	/**
	 * 电销人员名称
	 */
	private String truename;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	@Override
	public String toString() {
		return "Electricpin [domain=" + domain + ", eid=" + eid + ", truename="
				+ truename + "]";
	}
	
	
}
