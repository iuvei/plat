package com.gameportal.pay.model;

import java.util.Date;

import com.gameportal.pay.util.DateUtil;
import com.gameportal.pay.util.MD5Util;

/**
 * 宝付。
 * 
 * @author Administrator
 *
 */
public class BaoFooPayment {

	/**
	 * 商户ID
	 */
	private String memberID;

	/**
	 * 终端ID
	 */
	private String terminalID;

	/**
	 * 接口版本
	 */
	private String interfaceVersion = "4.0";

	/**
	 * 加密类型
	 */
	private String keyType = "1";

	/**
	 * 功能ID
	 */
	private String payID = "";

	/**
	 * 订单日期：14位长度 yyyyMMddHHmmss
	 */
	private String tradeDate = DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT_MEDIA);

	/**
	 * 订单号
	 */
	private String transID;

	/**
	 * 订单金额：分
	 */
	private String orderMoney;

	/**
	 * 通知类型
	 */
	private String noticeType = "1";

	/**
	 * 页面返回地址
	 */
	private String pageUrl;

	/**
	 * 交易通知地址
	 */
	private String returnUrl;

	/**
	 * 交易签名
	 */
	private String signature;

	public String getMemberID() {
		return memberID;
	}

	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}

	public String getTerminalID() {
		return terminalID;
	}

	public void setTerminalID(String terminalID) {
		this.terminalID = terminalID;
	}

	public String getInterfaceVersion() {
		return interfaceVersion;
	}

	public void setInterfaceVersion(String interfaceVersion) {
		this.interfaceVersion = interfaceVersion;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public String getPayID() {
		return payID;
	}

	public void setPayID(String payID) {
		this.payID = payID;
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getTransID() {
		return transID;
	}

	public void setTransID(String transID) {
		this.transID = transID;
	}

	public String getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(String orderMoney) {
		this.orderMoney = orderMoney;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	/**
	 * 构建请求签名
	 * 
	 * @return
	 */
	public String bulidSignature(String key) {
		String sign = memberID + "|" + payID + "|" + tradeDate + "|" + transID + "|" + orderMoney + "|" + pageUrl + "|"
				+ returnUrl + "|" + noticeType + "|" + key;
		return MD5Util.getMD5Encode(sign).toLowerCase();
	}
}
