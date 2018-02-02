package com.na.manager.entity;

import java.io.Serializable;

/**
 * 国际化表
 * @author andy
 * @date 2017年6月22日 下午12:25:27
 * 
 */
public class I18N implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String key;
	
	private String zh;
	
	private String tw;
	
	private String en;
	
	private String ko;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getZh() {
		return zh;
	}

	public void setZh(String zh) {
		this.zh = zh;
	}

	public String getTw() {
		return tw;
	}

	public void setTw(String tw) {
		this.tw = tw;
	}

	public String getEn() {
		return en;
	}

	public void setEn(String en) {
		this.en = en;
	}

	public String getKo() {
		return ko;
	}

	public void setKo(String ko) {
		this.ko = ko;
	}
}
