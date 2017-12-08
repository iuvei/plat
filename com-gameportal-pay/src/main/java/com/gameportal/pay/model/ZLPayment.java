package com.gameportal.pay.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.gameportal.pay.util.MyRSAUtils;
import com.gameportal.pay.util.RandomUtil;
import com.gameportal.pay.util.SignatureUtil;
import com.gameportal.pay.util.WebConst;
/**
 * 
 * 掌灵支付
 *
 */
public class ZLPayment implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static String PUBLICKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxzVuaVK+Xh4LmS+qVPppK7cbKBuQUbmWcbjSYKWxf/wkmP3umNQE6neu4Lswy1JBh0Joo/piYkeTialZc/VYWaXSUbFMAaoAHf0zWYXpBeU6H6VKxlJVCqyyhrM3j6hySnRp2BeaRKsZtE73MwnAOCbx3sWPkWTgfrXkM7ShLbVaBHGmmrHzzZuibaERajGisCBK3o1yMW6j8nu84KKISygF6ZBDCaSzIoA0W4PjwnpxXWbt4plu7YIu3tFikzuCQHd28FRqoEtS5ht+MiEjJhFwUZwDeCCjFiEaFgowjqbh5q+f9CfhFbVlble1qYMoKjysRcUhK0LpwLztXUooRQIDAQAB";
	public static String PRIVATEKEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKvscfEbsm0sjllc6T86XEbpuO2iADS8cPkX3Bz58zwt5Zg4zzAMVeUTsxba/WbKCLTJRr4iQOFz9F48ngsbJfHJo2kPsUCKLqTWuDzRL1ApKnkjwm8lR9IEvXI/fQ6r6Fof/5aw9s8qBKtUT0HgZMfv+RYAnlfnHICbyYEFxLyzAgMBAAECgYAvDzqlJ9KwRZj1ZxRFhWEK3CENkuGBje9Sj7Blkhl9aIbHIw27djTwznKgF/Ny9mxpqAqgMtoVnFrQjMEqAxVgZdoaMIEpfinnFvO4PaSSsV76hskXervlCw0jew10jSRuflRgXtTKI+Zo2cvqcV7XA8y7l3Qyy/yd+J0Re0m42QJBANKUKE7Hariqr4AP7ESQEbN0hXM4Ny5EUA3xykO1ViPYOG8RWhJY5eKA9kJCr5dLbh0NdmzpEdRQfMireBdRAK8CQQDRAc848DMvqsj1hTTNV4A/fLp0I1LsrSx/j+0bhNNzmWfR+AfbuKBN1BjY9LF9wOHMln5lLLqs/BQwkyb3SF09AkEAp9m+82STVdWABNUsYE5yPZggk4H4wiFjqmnT7vqJShMckLPFFyDTRSPEp3ZeXnuarv+/KPry4kix3oaHOBayMwJAcXd1A2lLsJYcMUZWyx+0zUKuirxemYTOQpZlV+o9TWW7+j6YLY3G4VnC/42gCjA7pXESP2UQFzdm0pQgtGDB1QJARts4cNVHYEOmEFz3WtZ64dHkAUK3ueHG8usBRqLWNKEwEaB6jrlgKN1LoxE3qxCeQA0BZY5Ajuy2KmfiGaRK/A==";
	/**
	 * 平台分配的机构号
	 */
	private String orgId = "000041";

	/**
	 * 订单付款方式
	 */
	private String source="1"; //1 支付宝  0 微信
	
	/**
	 * 清算金额
	 */
	private String settleAmt="";
	
	/**
	 * 账户
	 */
	private String account="13552642658";
	
	/**
	 * 金额 单位：分
	 */
	private String amount;

	/**
	 * 商户订单号
	 */
	private String orgOrderNo;
	
	/**
	 * 订单支付结果异步通知地
	 */
	private String notifyUrl;
	
	/**
	 * 交易类型
	 */
	private String tranTp="0";
	
	/**
	 * 签名
	 */
	private String signature;
	
	/**
	 * 附加数据
	 */
	private String extra=RandomUtil.getRandomCode(3);
	
	/**
	 * 商品名称
	 */
	private String subject=WebConst.getSubjectList().get(new Random().nextInt(32)).trim();
	
	/**
	 * 扫码支付
	 * @param order
	 */
	public Map<String,Object> fqrpay(String key){
		Map<String,Object> params = new HashMap<>();
		params.put("orgId", orgId);
		params.put("source", source);
		params.put("settleAmt", settleAmt);
		params.put("account", account);
		params.put("amount", amount);
		params.put("notifyUrl", notifyUrl);
		params.put("tranTp", tranTp);
		params.put("orgOrderNo", orgOrderNo);
		params.put("subject", subject.trim());
		params.put("extra", extra);
		params.put("description", "abc");
		params.put("body", "def");
		String bigStr = SignatureUtil.hex(params);
		System.out.println("加密字串："+bigStr);
		params.put("signature", MyRSAUtils.sign(PRIVATEKEY, bigStr, MyRSAUtils.MD5_SIGN_ALGORITHM));
		return params;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSettleAmt() {
		return settleAmt;
	}

	public void setSettleAmt(String settleAmt) {
		this.settleAmt = settleAmt;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getOrgOrderNo() {
		return orgOrderNo;
	}

	public void setOrgOrderNo(String orgOrderNo) {
		this.orgOrderNo = orgOrderNo;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getTranTp() {
		return tranTp;
	}

	public void setTranTp(String tranTp) {
		this.tranTp = tranTp;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}
