package com.gameportal.pay.model.shb;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("response")
public class ZFResponse {
	@XStreamAlias("interface_version")
	private String interfaceVersion;
	
	@XStreamAlias("merchant_code")
	private String merchantCode;
	
	@XStreamAlias("order_amount")
	private String orderAmount;

	@XStreamAlias("order_no")
	private String orderNo;
	
	@XStreamAlias("order_time")
	private String orderTime;
	
	@XStreamAlias("qrcode")
	private String qrCode;
	
	@XStreamAlias("resp_code")
	private String respCode;
	
	@XStreamAlias("resp_desc")
	private String respDesc;
	
	@XStreamAlias("result_code")
	private String resultCode;
	
	@XStreamAlias("sign")
	private String sign;
	
	@XStreamAlias("sign_type")
	private String signType;
	
	@XStreamAlias("trade_no")
	private String tradeNo;
	
	@XStreamAlias("trade_time")
	private String tradeTime;

	public String getInterfaceVersion() {
		return interfaceVersion;
	}

	public void setInterfaceVersion(String interfaceVersion) {
		this.interfaceVersion = interfaceVersion;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespDesc() {
		return respDesc;
	}

	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}
}
