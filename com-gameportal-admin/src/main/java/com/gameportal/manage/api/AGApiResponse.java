package com.gameportal.manage.api;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * AG接口返回报文数据接收器。
 * @author Administrator
 *
 */
@XStreamAlias("result")
public class AGApiResponse {
	/**
	 * 代码数值
	 */
	@XStreamAsAttribute
	private String info;

	/**
	 * 数值描述
	 */
	@XStreamAsAttribute
	private String msg;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
