package com.gameportal.pay.model;

import com.gameportal.pay.util.SFMD5Util;

public class YBPayment {
	// 商户ID
	private String partner = "20792";

	// 银行类型
	private String banktype = "WEIXIN";

	// 金额
	private String paymoney;

	// 商户订单号
	private String ordernumber;

	// 下行异步通知地址
	private String callbackurl;

	// 下行同步通知地址
	private String hrefbackurl;

	// 备注信息
	private String attach;

	// MD5签名
	private String sign;
	
	/**
	 * 构建签名
	 * @return
	 */
	public String bulidSign(String key){
		String origStr ="partner="+getPartner()+"&banktype="+getBanktype()+"&paymoney="+getPaymoney()+"&ordernumber="+getOrdernumber()+"&callbackurl="+getCallbackurl()+key;
		return SFMD5Util.encryption(origStr);
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getBanktype() {
		return banktype;
	}

	public void setBanktype(String banktype) {
		this.banktype = banktype;
	}

	public String getPaymoney() {
		return paymoney;
	}

	public void setPaymoney(String paymoney) {
		this.paymoney = paymoney;
	}

	public String getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}

	public String getCallbackurl() {
		return callbackurl;
	}

	public void setCallbackurl(String callbackurl) {
		this.callbackurl = callbackurl;
	}

	public String getHrefbackurl() {
		return hrefbackurl;
	}

	public void setHrefbackurl(String hrefbackurl) {
		this.hrefbackurl = hrefbackurl;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
