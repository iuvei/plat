package com.gameportal.manage.proxydomain.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

public class ProxyDomian extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4868497478750605534L;
	
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


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
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


	@Override
	public String toString() {
		return "ProxyDomian [urlid=" + urlid + ", proxyurl=" + proxyurl
				+ ", proxyuid=" + proxyuid + ", status=" + status + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((proxyuid == null) ? 0 : proxyuid.hashCode());
		result = prime * result
				+ ((proxyurl == null) ? 0 : proxyurl.hashCode());
		result = prime * result + status;
		result = prime * result + urlid;
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
		ProxyDomian other = (ProxyDomian) obj;
		if (proxyuid == null) {
			if (other.proxyuid != null)
				return false;
		} else if (!proxyuid.equals(other.proxyuid))
			return false;
		if (proxyurl == null) {
			if (other.proxyurl != null)
				return false;
		} else if (!proxyurl.equals(other.proxyurl))
			return false;
		if (status != other.status)
			return false;
		if (urlid != other.urlid)
			return false;
		return true;
	}


	@Override
	public Serializable getID() {
		
		return this.getUrlid();
	}
	
	
}
