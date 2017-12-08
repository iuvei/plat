package com.gameportal.web.user.model;

import java.io.Serializable;

/**
 * 代理URL
 * @author Administrator
 *
 */
public class ProxyUrl extends BaseEntity{

	private int urlid;
	private String proxyurl;
	private String proxyuid;
	private String status;
	
	
	public int getUrlid() {
		return urlid;
	}


	public void setUrlid(int urlid) {
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


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.urlid;
	}
}
