package com.gameportal.pay.model;

import com.gameportal.pay.util.KeyValue;
import com.gameportal.pay.util.KeyValues;
import com.gameportal.pay.util.StringUtil;

/**
 * 国盛通。
 * 
 * @author jade
 *
 */
public class GstPayment {

	private String inputCharset = "UTF-8";

	private String informUrl = "";

	private String returnUrl = "";

	private String payType = "1"; // 1:网银支付 2:微信 3:支付宝

	private String bankCode = "";

	private String merchantCode = "";

	private String orderNo = "";

	private String orderAmount = "";

	private String orderTime = "";

	private String reqReferer = "";

	private String customerIp = "";

	private String returnParams = "";

	private String sign = "";
	
	public String buildSignature(String key){
		KeyValues kvs = new KeyValues();
        kvs.add(new KeyValue("input_charset", inputCharset));
        kvs.add(new KeyValue("inform_url", informUrl));
        kvs.add(new KeyValue("return_url", returnUrl));
        kvs.add(new KeyValue("pay_type", payType));
        kvs.add(new KeyValue("bank_code", bankCode));
        kvs.add(new KeyValue("merchant_code", merchantCode));
        kvs.add(new KeyValue("order_no", orderNo));
        kvs.add(new KeyValue("order_amount", orderAmount));
        kvs.add(new KeyValue("order_time", orderTime));
        if(StringUtil.isNotEmpty(reqReferer)){
        	kvs.add(new KeyValue("req_referer", reqReferer));
        }
        kvs.add(new KeyValue("customer_ip", customerIp));
        if(StringUtil.isNotEmpty(returnParams)){
        	kvs.add(new KeyValue("return_params", returnParams));
        }
        String sign = kvs.sign(key, inputCharset);
        
		return sign;
	}

	public String getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}

	public String getInformUrl() {
		return informUrl;
	}

	public void setInformUrl(String informUrl) {
		this.informUrl = informUrl;
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

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
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

	public String getReturnParams() {
		return returnParams;
	}

	public void setReturnParams(String returnParams) {
		this.returnParams = returnParams;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
