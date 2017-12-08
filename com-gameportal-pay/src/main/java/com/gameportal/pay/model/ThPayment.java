package com.gameportal.pay.model;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.gameportal.pay.util.DateUtil;
import com.gameportal.pay.util.KeyValue;
import com.gameportal.pay.util.KeyValues;

/**
 * 通汇支付。
 * 
 * @author Administrator
 *
 */
public class ThPayment {

	/**
	 * 字符集
	 */
	private String inputCharset = "UTF-8";

	/**
	 * 服务器异步通知地址
	 */
	private String notifyUrl;

	/**
	 * 页面同步跳转通知地址
	 */
	private String returnUrl;

	/**
	 * 支付方式
	 */
	private String payType = "1";

	/**
	 * 商户号
	 */
	private String merchantCode;

	/**
	 * 商户订单号
	 */
	private String orderNo;

	/**
	 * 商户订单总金额:元
	 */
	private String orderAmount;

	/**
	 * 商户订单时间:yyyy-MM-dd HH:mm:ss
	 */
	private String orderTime = DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT);
	
	/**
	 * 银行编码
	 */
	private String bankCode="";

	/**
	 * 来路域名
	 */
	private String reqReferer;
	
	/**
	 * 回传参数
	 */
	private String returnParams;

	/**
	 * 消费者IP
	 */
	private String customerIp;

	/**
	 * 签名
	 */
	private String sign;

	public String bulidSignature(String key) {
		KeyValues kvs = new KeyValues();
		kvs.add(new KeyValue("input_charset", inputCharset));
		kvs.add(new KeyValue("notify_url", notifyUrl));
		kvs.add(new KeyValue("return_url", returnUrl));
		kvs.add(new KeyValue("pay_type", payType));
		kvs.add(new KeyValue("merchant_code", merchantCode));
		kvs.add(new KeyValue("order_no", orderNo));
		kvs.add(new KeyValue("order_amount", orderAmount));
		kvs.add(new KeyValue("order_time", orderTime));
		kvs.add(new KeyValue("req_referer", reqReferer));
		kvs.add(new KeyValue("customer_ip", customerIp));
		if(StringUtils.isNotEmpty(returnParams)){
			kvs.add(new KeyValue("return_params", returnParams));
		}
		if(StringUtils.isNotEmpty(bankCode)){
			kvs.add(new KeyValue("bank_code", bankCode));
		}
		String sign = kvs.sign(key, "UTF-8");
		return sign;
	}
	

	public String getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getReqReferer() {
		return reqReferer;
	}

	public void setReqReferer(String reqReferer) {
		this.reqReferer = reqReferer;
	}

	public String getCustomerIp() {
		return customerIp;
	}

	public void setCustomerIp(String customerIp) {
		this.customerIp = customerIp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getReturnParams() {
		return returnParams;
	}

	public void setReturnParams(String returnParams) {
		this.returnParams = returnParams;
	}
}
