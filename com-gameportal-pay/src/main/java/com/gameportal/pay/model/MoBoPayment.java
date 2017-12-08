package com.gameportal.pay.model;

import java.util.Date;

import com.gameportal.pay.util.DateUtil;
import com.gameportal.pay.util.MD5Util;

/**
 * 摩宝支付-微信扫码
 * 
 * @author Administrator
 *
 */
public class MoBoPayment {

	/**
	 * 接口名字
	 */
	private String apiName = "WEB_PAY_B2C";

	/**
	 * 接口版本
	 */
	private String apiVersion = "1.0.0.0";
	/**
	 * 商户(合作伙伴)ID
	 */
	private String platformID = "MerchTest";

	/**
	 * 商户账号
	 */
	private String merchNo;
	/**
	 * 商户参数
	 */
	private String merchParam="a";
	/**
	 * 商户订单号
	 */
	private String orderNo;

	/**
	 * 交易日期
	 */
	private String tradeDate = DateUtil.getStrByDate(new Date(), "yyyyMMdd");

	/**
	 * 订单金额:元
	 */
	private String amt;

	/**
	 * 支付结果通知地址
	 */
	private String merchUrl;

	/**
	 * 交易摘要
	 */
	private String tradeSummary;
	/**
	 * 签名
	 */
	private String signMsg;
	
	/**
	 * 客户单ip
	 */
	private String customerIP;
	
	/**
	 * 1.网银 2.一键支付 3.非银行支付 4. 支付宝扫描 5.微信扫码
	 */
	private String choosePayType;

	public String getSignStr(String key) throws Exception {
		// 输入数据组织成字符串
		String paramsStr = String.format(
				"apiName=%s&apiVersion=%s&platformID=%s&merchNo=%s&orderNo=%s&tradeDate=%s&amt=%s&merchUrl=%s&merchParam=%s&tradeSummary=%s",
				apiName, apiVersion, platformID, merchNo, orderNo, tradeDate, amt, merchUrl, merchParam, tradeSummary);
		return MD5Util.signByMD5(paramsStr,key).toUpperCase();
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getPlatformID() {
		return platformID;
	}

	public void setPlatformID(String platformID) {
		this.platformID = platformID;
	}

	public String getMerchNo() {
		return merchNo;
	}

	public void setMerchNo(String merchNo) {
		this.merchNo = merchNo;
	}

	public String getMerchParam() {
		return merchParam;
	}

	public void setMerchParam(String merchParam) {
		this.merchParam = merchParam;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}

	public String getMerchUrl() {
		return merchUrl;
	}

	public void setMerchUrl(String merchUrl) {
		this.merchUrl = merchUrl;
	}

	public String getTradeSummary() {
		return tradeSummary;
	}

	public void setTradeSummary(String tradeSummary) {
		this.tradeSummary = tradeSummary;
	}

	public String getSignMsg() {
		return signMsg;
	}

	public void setSignMsg(String signMsg) {
		this.signMsg = signMsg;
	}
	
	public String getChoosePayType() {
		return choosePayType;
	}

	public void setChoosePayType(String choosePayType) {
		this.choosePayType = choosePayType;
	}

	public String getCustomerIP() {
		return customerIP;
	}

	public void setCustomerIP(String customerIP) {
		this.customerIP = customerIP;
	}

	public static void main(String[] args) {
		try {
			String str="apiName=WEB_PAY_B2C&apiVersion=1.0.0.0&platformID=210000310009136&merchNo=210000310009136&orderNo=2016041516281601&tradeDate=20160415&amt=1.00&merchUrl=http://app.shanghaixy.com.cn/pay/mbo/callback.html&merchParam=null&tradeSummary=U2016041516281602";
			System.out.println(MD5Util.signByMD5(str,"f583536c52d81d6ce0b4f98812618b04").toUpperCase());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
