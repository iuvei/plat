package com.gameportal.domain;

/**
 * 代理推广地址
 * @author leron
 *
 */
public class ProxyDomain extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9095948067018696797L;

	
	private Integer urlid;				
	private String proxyurl;		//URL domain
	private String proxyuid;		//代理用户UserID
	private String proxyaccount;	//代理账号
	private String proxyname;		//代理姓名
	private int status;				//0无效，1正常	
	public Integer getUrlid() {
		return urlid;
	}
	public void setUrlid(Integer urlid) {
		this.urlid = urlid;
	}
	public String getProxyurl() {
		return proxyurl;
	}
	public void setProxyurl(String proxyurl) {
		this.proxyurl = proxyurl;
	}
	public String getProxyuid() {
		return proxyuid;
	}
	public void setProxyuid(String proxyuid) {
		this.proxyuid = proxyuid;
	}
	public String getProxyaccount() {
		return proxyaccount;
	}
	public void setProxyaccount(String proxyaccount) {
		this.proxyaccount = proxyaccount;
	}
	public String getProxyname() {
		return proxyname;
	}
	public void setProxyname(String proxyname) {
		this.proxyname = proxyname;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
