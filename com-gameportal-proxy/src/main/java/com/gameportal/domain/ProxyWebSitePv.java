package com.gameportal.domain;

import java.util.Date;

/**
 * 代理日访问量统计。
 * 
 * @author sum
 *
 */
public class ProxyWebSitePv extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private Long staid;
	private Long proxyid;
	private String proxydomain;
	private Date createDate;
	private Integer number;

	public ProxyWebSitePv() {
	}

	public Long getStaid() {
		return staid;
	}

	public void setStaid(Long staid) {
		this.staid = staid;
	}

	public Long getProxyid() {
		return proxyid;
	}

	public void setProxyid(Long proxyid) {
		this.proxyid = proxyid;
	}

	public String getProxydomain() {
		return proxydomain;
	}

	public void setProxydomain(String proxydomain) {
		this.proxydomain = proxydomain;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
}
