package com.gameportal.pay.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;

import com.gameportal.pay.util.DateUtil;
import com.gameportal.pay.util.DigestUtils;

/**
 * 国付宝支付。
 * 
 * @author Administrator
 *
 */
public class GoPayment {
	/**
	 * 版本号
	 */
	private String version = "2.2";

	/**
	 * 字符集 1：GBK 2：UTF-8
	 */
	private String charset = "1";

	/**
	 * 语言 1：中文 2：英文
	 */
	private String language = "1";
	/**
	 * 签名类型 1：MD5 2：SHA
	 */
	private String signType = "1";

	/**
	 * 交易代码
	 */
	private String tranCode = "8888";

	/**
	 * 商户ID
	 */
	private String merchantID;

	/**
	 * 订单号
	 */
	private String merOrderNum;

	/**
	 * 交易金额
	 */
	private String tranAmt;
	/**
	 * 手续费
	 */
	private String feeAmt = "";

	/**
	 * 币种
	 */
	private String currencyType = "156";

	/**
	 * 前台通知地址
	 */
	private String frontMerUrl;

	/**
	 * 后台通知地址
	 */
	private String backgroundMerUrl;

	/**
	 * 交易时间
	 */
	private String tranDateTime = DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT_MEDIA);

	/**
	 * 转入账户
	 */														
	private String virCardNoIn="0000000002000007426"; // 正式
	//private String virCardNoIn="0000000002000000257"; // 测试
	/**
	 * 用户IP
	 */
	private String tranIP;

	/**
	 * 重复提交标志 0:不重复  1：重复
	 */
	private String isRepeatSubmit="0";

	/**
	 * 密文串
	 */
	private String signValue;

	/**
	 * 服务器时间
	 */
	private String gopayServerTime="";
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getTranCode() {
		return tranCode;
	}

	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}

	public String getMerchantID() {
		return merchantID;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public String getMerOrderNum() {
		return merOrderNum;
	}

	public void setMerOrderNum(String merOrderNum) {
		this.merOrderNum = merOrderNum;
	}

	public String getTranAmt() {
		return tranAmt;
	}

	public void setTranAmt(String tranAmt) {
		this.tranAmt = tranAmt;
	}

	public String getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(String feeAmt) {
		this.feeAmt = feeAmt;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getFrontMerUrl() {
		return frontMerUrl;
	}

	public void setFrontMerUrl(String frontMerUrl) {
		this.frontMerUrl = frontMerUrl;
	}

	public String getBackgroundMerUrl() {
		return backgroundMerUrl;
	}

	public void setBackgroundMerUrl(String backgroundMerUrl) {
		this.backgroundMerUrl = backgroundMerUrl;
	}

	public String getTranDateTime() {
		return tranDateTime;
	}

	public void setTranDateTime(String tranDateTime) {
		this.tranDateTime = tranDateTime;
	}

	public String getVirCardNoIn() {
		return virCardNoIn;
	}

	public void setVirCardNoIn(String virCardNoIn) {
		this.virCardNoIn = virCardNoIn;
	}

	public String getTranIP() {
		return tranIP;
	}

	public void setTranIP(String tranIP) {
		this.tranIP = tranIP;
	}

	public String getIsRepeatSubmit() {
		return isRepeatSubmit;
	}

	public void setIsRepeatSubmit(String isRepeatSubmit) {
		this.isRepeatSubmit = isRepeatSubmit;
	}

	public String getSignValue() {
		return signValue;
	}

	public void setSignValue(String signValue) {
		this.signValue = signValue;
	}

	
	public void setGopayServerTime(String gopayServerTime) {
		this.gopayServerTime = gopayServerTime;
	}
	/**
	 * 获取国付宝服务器时间 用于时间戳
	 * @return 格式YYYYMMDDHHMMSS
	 */
	public String getGopayServerTime() {
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
		httpClient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 10000); 
		GetMethod getMethod = new GetMethod("https://gateway.gopay.com.cn/time.do");
		getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"GBK");  
		// 执行getMethod
		int statusCode = 0;
		try {
			statusCode = httpClient.executeMethod(getMethod);			
			if (statusCode == HttpStatus.SC_OK){
				String respString = StringUtils.trim((new String(getMethod.getResponseBody(),"GBK")));
				return respString;
			}			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		return null;
		//return DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT_MEDIA);
	}
	
	/**
	 * MD5生成请求签名
	 * @param key
	 * @return
	 */
	public String getSignStr(String key) {
		StringBuilder origSign = new StringBuilder("version=[").append(version).append("]");
		origSign.append("tranCode=[").append(tranCode).append("]");
		origSign.append("merchantID=[").append(merchantID).append("]");
		origSign.append("merOrderNum=[").append(merOrderNum).append("]");
		origSign.append("tranAmt=[").append(tranAmt).append("]");
		origSign.append("feeAmt=[]");
		origSign.append("tranDateTime=[").append(tranDateTime).append("]");
		origSign.append("frontMerUrl=[").append(frontMerUrl).append("]");
		origSign.append("backgroundMerUrl=[").append(backgroundMerUrl).append("]");
		origSign.append("orderId=[]");
		origSign.append("gopayOutOrderId=[]");
		origSign.append("tranIP=[").append(tranIP).append("]");
		origSign.append("respCode=[]");
		origSign.append("gopayServerTime=[]");
		origSign.append("VerficationCode=[").append(key).append("]");
		
		String origSignStr =origSign.toString();
		System.out.println("签名加密前："+origSignStr);
		try {
			return DigestUtils.md5DigestAsHex(origSignStr.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
