package com.gameportal.pay.model;

import org.apache.commons.lang3.StringUtils;

import com.gameportal.pay.util.DigestUtils;
import com.gameportal.pay.util.RandomUtil;

/**
 * 畅汇移动支付
 * 
 * @author sunny
 *
 */
public class CHPayment {
	private String p0cmd = "Buy";

	private String p1MerId="";

	private String p2Order="";

	private String p3Cur = "CNY";

	/**
	 * 单位:元，精确到分.
	 */
	private double p4Amt = 0;

	/**
	 * 异步回调地址
	 */
	private String p8Url="";

	/**
	 * 同步回调地址
	 */
	private String piUrl="";

	/**
	 * 商户扩展信息
	 */
	private String p9MP=new RandomUtil().getRandomCode(8);

	/**
	 *  支付通道编码
	 */
	private String paFrpId = "OnlinePay";

	/**
	 * 银行编码
	 */
	private String pgBankCode="";

	/**
	 * 签名数据
	 */
	private String hmac="";

	public String getSignStr(String key) {
		StringBuffer sValue = new StringBuffer();
		sValue.append(p0cmd);
		sValue.append(p1MerId);
		sValue.append(p2Order);
		sValue.append(p3Cur);
		sValue.append((int)p4Amt);
		sValue.append(p8Url);
		sValue.append(p9MP);
		sValue.append(paFrpId);
		if(StringUtils.isNotEmpty(pgBankCode)){
			sValue.append(pgBankCode);
		}
		sValue.append(piUrl);
		System.out.println("加密前："+sValue.toString());
		String sign = DigestUtils.hmacSign(sValue.toString(), key);
		return sign;
	}

	public String getP0cmd() {
		return p0cmd;
	}

	public void setP0cmd(String p0cmd) {
		this.p0cmd = p0cmd;
	}

	public String getP1MerId() {
		return p1MerId;
	}

	public void setP1MerId(String p1MerId) {
		this.p1MerId = p1MerId;
	}

	public String getP2Order() {
		return p2Order;
	}

	public void setP2Order(String p2Order) {
		this.p2Order = p2Order;
	}

	public String getP3Cur() {
		return p3Cur;
	}

	public void setP3Cur(String p3Cur) {
		this.p3Cur = p3Cur;
	}

	public double getP4Amt() {
		return p4Amt;
	}

	public void setP4Amt(double p4Amt) {
		this.p4Amt = p4Amt;
	}

	public String getP8Url() {
		return p8Url;
	}

	public void setP8Url(String p8Url) {
		this.p8Url = p8Url;
	}

	public String getP9MP() {
		return p9MP;
	}

	public void setP9MP(String p9mp) {
		p9MP = p9mp;
	}

	public String getPaFrpId() {
		return paFrpId;
	}

	public void setPaFrpId(String paFrpId) {
		this.paFrpId = paFrpId;
	}

	public String getHmac() {
		return hmac;
	}

	public void setHmac(String hmac) {
		this.hmac = hmac;
	}

	public String getPiUrl() {
		return piUrl;
	}

	public void setPiUrl(String piUrl) {
		this.piUrl = piUrl;
	}

	public String getPgBankCode() {
		return pgBankCode;
	}

	public void setPgBankCode(String pgBankCode) {
		this.pgBankCode = pgBankCode;
	}
	
	
}
