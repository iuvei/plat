package com.gameportal.pay.model;

import java.io.UnsupportedEncodingException;

import com.gameportal.pay.util.DigestUtils;

/**
 * 锐付在线支付。
 * 
 * @author Administrator
 *
 */
public class RFPayment {
	/**
	 * 商戶编号
	 */
	private String partyId = "gateway_A01634";

	private String accountId = "gateway_A01634001";

	/**
	 * 支付类型
	 */
	private String appType = "";
	/**
	 * 订单号
	 */
	private String orderNo = "";

	/**
	 * 订单金额
	 */
	private String orderAmount = "10";

	/**
	 * 商户首码
	 */
	private String goods = "A19E";
	
	/**
	 * 银行编码
	 */
	private String bank="";

	/**
	 * 返回地址
	 */
	private String returnUrl;

	/**
	 * 支付卡种 01人民幣轉帳卡 02 信用卡
	 */
	private String cardType = "01";
	/**
	 * 簽名方式
	 */
	private String encodeType = "Md5";

	/**
	 * 子商戶參考編號
	 */
	private String refCode = "00000000";

	/**
	 * Md5 摘要
	 */
	private String signMD5;

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAppType() {
		return appType;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public void setAppType(String appType) {
		this.appType = appType;
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

	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getEncodeType() {
		return encodeType;
	}

	public void setEncodeType(String encodeType) {
		this.encodeType = encodeType;
	}

	public String getRefCode() {
		return refCode;
	}

	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}

	public String getSignMD5() {
		return signMD5;
	}

	public void setSignMD5(String signMD5) {
		this.signMD5 = signMD5;
	}

	/**
	 * MD5生成请求签名
	 * 
	 * @param key
	 * @return
	 */
	public String getSignStr(String key) {
		String source = "orderNo" + orderNo + "appType" + appType + "orderAmount" + orderAmount + "encodeType"
				+ encodeType + key;
		String origSignStr = source.toString();
		try {
			return DigestUtils.md5DigestAsHex(origSignStr.getBytes("UTF-8")).toLowerCase();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
