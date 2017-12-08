package com.gameportal.pay.model;

import com.gameportal.pay.util.MD5Util;
import com.gameportal.pay.util.SFMD5Util;
/**
 * 新天诚支付
 * @author jade
 *
 */
public class XTCPayment {
	/**
	 * 商户 ID
	 */
	private String parter = "";

	/**
	 * 银行类型
	 */
	private String banktype = "ALIPAY";

	/**
	 * 金额：元
	 */
	private String paymoney = "";

	/**
	 * 商户订单Id
	 */
	private String ordernumber = "";

	/**
	 * 下行异步通知地址
	 */
	private String callbackurl = "";

	/**
	 * 下行同步通知地址
	 */
	private String hrefbackurl = "";

	/**
	 * 备注
	 */
	private String attach = "";

	/**
	 * 签名
	 */
	private String sign = "";
	
	/**
	 * 点卡类型
	 */
	private String cardtype;
	
	/**
	 * 点卡卡号
	 */
	private String cardno;
	
	/**
	 * 点卡密码
	 */
	private String cardpwd;

	public String getParter() {
		return parter;
	}

	public void setParter(String parter) {
		this.parter = parter;
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
	
	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getCardpwd() {
		return cardpwd;
	}

	public void setCardpwd(String cardpwd) {
		this.cardpwd = cardpwd;
	}

	/**
	 * 构造签名字串
	 * 
	 * @param key
	 * @return
	 */
	public String buildSignature(String key) {
		String signature = String.format("partner=%s&banktype=%s&paymoney=%s&ordernumber=%s&callbackurl=%s", parter,
				banktype, paymoney, ordernumber, callbackurl + key);
		System.out.println("新天誠加密字符串："+signature);
		return MD5Util.getMD5Encode(signature);
	}
	
	/**
	 * 构造点卡签名字串
	 * 
	 * @param key
	 * @return
	 */
	public String buildDKSignature(String key) {
		String signature = String.format("partner=%s&cardtype=%s&cardno=%s&cardpwd=%s&paymoney=%s&ordernumber=%s&callbackurl=%s", parter,
				cardtype, cardno,cardpwd,paymoney,ordernumber, callbackurl + key);
		System.out.println("新天誠点卡加密字符串："+signature);
		return MD5Util.getMD5Encode(signature);
	}

	public static void main(String[] args) {
		// String st2 =
		// "parter=99&type=963&value=100.00&orderid=1234567890&callbackurl=http://www.baidu.com/backAction1234567890abcdef";
		try {
//			System.out.println(MD5Util.getMD5Encode("1234567890abcdefghijklmnopqrstuvwxyz"));
//			String signature = String.format("parter=%s&banktype=%s&paymoney=%s&ordernumber=%s&callbackurl=%s", "17678",
//					"ALIPAY", "100.00", "2017032416313401", "http://app.11dxy.com/pay/xtczfb/callback.htmlecd51430a779ef075b1efb44890c5169");
			String s="partner=10000&banktype=ICBC&paymoney=100.00&ordernumber=1234567890&callbackurl=http://xtcpay.cn/backAction4272fafab8869dbd292d959b7542530c";
			System.out.println("新天誠加密字符串："+s);
			 System.out.println(SFMD5Util.encryption(s));
			 System.out.println(SFMD5Util.encryption("1234567890abcdefghijklmnopqrstuvwxyz"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
