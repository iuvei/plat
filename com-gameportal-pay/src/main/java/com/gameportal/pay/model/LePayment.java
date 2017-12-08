package com.gameportal.pay.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.gameportal.pay.util.DateUtil;
import com.lechinepay.channel.lepay.client.apppay.AppPay;

/**
 * 乐付
 * 
 * @author jade
 *
 */
public class LePayment implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 版本号
	 */
	private String version = "1.0.0";

	/**
	 * 编码方式
	 */
	private String encoding = "UTF-8";

	/**
	 * 签名
	 */
	private String signature = "";

	/**
	 * 请求方保留信息
	 */
	private String reqReserved = "1";

	/**
	 * 商户号
	 */
	private String mchId = "61002";

	/**
	 * 应用ID
	 */
	private String cmpAppId = "52503";

	/**
	 * 支付宝-扫码支付:PT0007 微信支付-扫码支付:PT0009 支付宝-扫码支付(民生银行):PT0007_MS
	 * 微信支付-扫码支付(民生银行):PT0009_MS 微信支付-扫码支付(进钱大掌柜):wxpay.qrpay.gc.qdzg
	 */
	private String payTypeCode = "wxpay.qrpay.gc.qdzg";

	/**
	 * 订单号
	 */
	private String outTradeNo;

	/**
	 * 交易发送时间
	 */
	private String tradeTime = DateUtil.getStrByDate(new Date(), "yyyyMMddHHmmss");

	/**
	 * 交易金额
	 */
	private String amount;

	/**
	 * 摘要
	 */
	private String summary = getSubjectList().get(new Random().nextInt(32)).trim();

	/**
	 * 终端设备IP地址
	 */
	private String deviceIp = "";
	
	/**
	 * 同步地址
	 */
	private String returnUrl="";

	public static List<String> getSubjectList() {
		List<String> list = new ArrayList<String>();
		list.add("苹果6s中国风手机套");
		list.add("6plus硅胶防摔壳");
		list.add("手机木质壳木防摔创意定制");
		list.add("iphone6s手机壳挂绳");
		list.add("商务大气板扣平滑皮带");
		list.add("皮带防过敏中青年腰带");
		list.add("腰带青年牛皮裤带");
		list.add("针扣皮带 ");
		list.add("帆布腰带");
		list.add("光面皮带");
		list.add("针扣皮带");
		list.add("拼接t桖短袖");
		list.add("t桖男短袖纯棉");
		list.add("t桖男短袖宽松");
		list.add("t桖男短袖修身");
		list.add("连衣裙");
		list.add("半身裙");
		list.add("大码女装");
		list.add("打底裤");
		list.add("职业套装");
		list.add("中老年服装");
		list.add("水果罐头");
		list.add("肉类罐头");
		list.add("水产罐头");
		list.add("粥羹罐头");
		list.add("蔬菜罐头");
		list.add("牛奶");
		list.add("雪橙子");
		list.add("冰糖雪梨24瓶");
		list.add("非赣南脐橙夏橙子包邮");
		list.add("红肉脐橙");
		list.add("秭归夏橙");
		list.add("信丰脐橙");
		list.add("脐橙");
		return list;
	}
	
	public static void init() {
		String keyStorePassword = "MTIzNDU2";// qianbao777的base64编码后的值
		String keyStorePath = LePayment.class.getResource("/").getPath()+"lpkey/qianbaoclient.pfx";// 商户私钥证书
		String certificatePath = LePayment.class.getResource("/").getPath()+"lpkey/pdtserver.cer";// +Lepay公钥证书

		String keyStoreType = "PKCS12";
		AppPay.init(keyStorePassword, keyStorePath, keyStoreType, certificatePath);
	}
	
	public static String getSignStr(){
		String orgiStr="amount=1&cmpAppId=35002&deviceId=L897554536354&deviceIp=127.0.0.1&encoding=UTF-8&mchId=32002&outTradeNo=20161008150248&payTypeCode=PT0007_MS&reqReserved=1&summary=unit 测试&tradeTime=20161008150248&version=1.0.0";
		return orgiStr;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getReqReserved() {
		return reqReserved;
	}

	public void setReqReserved(String reqReserved) {
		this.reqReserved = reqReserved;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getCmpAppId() {
		return cmpAppId;
	}

	public void setCmpAppId(String cmpAppId) {
		this.cmpAppId = cmpAppId;
	}

	public String getPayTypeCode() {
		return payTypeCode;
	}

	public void setPayTypeCode(String payTypeCode) {
		this.payTypeCode = payTypeCode;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
}
