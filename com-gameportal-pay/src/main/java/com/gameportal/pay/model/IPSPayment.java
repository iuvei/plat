package com.gameportal.pay.model;

import java.util.Date;

import com.gameportal.pay.util.DateUtil;


public class IPSPayment {
	// 商户编号
	private String merCode;
	// 商户订单号
	private String billNo;
	// 订单金额 两位小数
	private String amount;
	// 订单日期 YYYYMMDD
	private String date = DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT_DATE);
	// 币种 默人人民币
	private String currencyType = "RMB";
	// 支付卡种 01—人民币借记卡
	private String gatewayType = "01";
	// 语言 默人中文
	private String lang = "GB";
	// 支付结果成功返回的商户URL
	private String merchanturl;
	// 支付结果失败返回的商户URL
	private String failUrl;
	// 商户数据包
	private String attach;
	// 订单支付接口加密方式
	private String orderEncodeType = "5";
	// 交易返回接口加密方式 16：交易返回采用Md5WithRsa的签名认证方式 17：交易返回采用Md5的摘要认证方式
	private String retEncodeType = "17";
	// 返回方式 1 server to server 2 brower
	private String rettype = "1";
	// 回调地址
	private String serverUrl;
	// MD5摘要
	private String signMD5;
	// 直连选项
	private String doCredit="1"; //采用直连
	// 银行代码
	private String bankco;

	public IPSPayment() {
	}

	public String getSign(String key) {
		key = "billno" + getBillNo() + "currencytype" + getCurrencyType() + "amount" + getAmount() + "date" + getDate() + "orderencodetype"
				+ getOrderEncodeType() + key;
		cryptix.jce.provider.MD5 b = new cryptix.jce.provider.MD5();
		return b.toMD5(key).toLowerCase();
	}

	public String getMerCode() {
		return merCode;
	}

	public void setMerCode(String merCode) {
		this.merCode = merCode;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getGatewayType() {
		return gatewayType;
	}

	public void setGatewayType(String gatewayType) {
		this.gatewayType = gatewayType;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getMerchanturl() {
		return merchanturl;
	}

	public void setMerchanturl(String merchanturl) {
		this.merchanturl = merchanturl;
	}

	public String getFailUrl() {
		return failUrl;
	}

	public void setFailUrl(String failUrl) {
		this.failUrl = failUrl;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOrderEncodeType() {
		return orderEncodeType;
	}

	public void setOrderEncodeType(String orderEncodeType) {
		this.orderEncodeType = orderEncodeType;
	}

	public String getRetEncodeType() {
		return retEncodeType;
	}

	public void setRetEncodeType(String retEncodeType) {
		this.retEncodeType = retEncodeType;
	}

	public String getRettype() {
		return rettype;
	}

	public void setRettype(String rettype) {
		this.rettype = rettype;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getSignMD5() {
		return signMD5;
	}

	public void setSignMD5(String signMD5) {
		this.signMD5 = signMD5;
	}

	public String getDoCredit() {
		return doCredit;
	}

	public void setDoCredit(String doCredit) {
		this.doCredit = doCredit;
	}

	public String getBankco() {
		return bankco;
	}

	public void setBankco(String bankco) {
		this.bankco = bankco;
	}
}
