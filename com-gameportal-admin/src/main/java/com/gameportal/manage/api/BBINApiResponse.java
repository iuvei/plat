package com.gameportal.manage.api;

/**
 * BBIN接口返回报文接收器。
 * 
 * @author sum
 *
 */
public class BBINApiResponse {
	// 返回结果
	private String result;
	// 错误编码
	private String code;
	// 描述信息
	private String info;
	// 登录地址
	private String url;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
