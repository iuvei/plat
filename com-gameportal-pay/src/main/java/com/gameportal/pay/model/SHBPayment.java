package com.gameportal.pay.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.gameportal.pay.util.DateUtil;
import com.gameportal.pay.util.RandomUtil;
import com.itrus.util.sign.RSAWithSoftware;

/**
 * 速汇宝
 * 
 * @author jade
 *
 */
public class SHBPayment implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnFXXyNt08Q38LgUquOIs+8ZbhMlfWzyrW3tuQodBdwSIln2pc3OGPv0D3llLbqSNPWjxOFwEJbhyhFUGWfEE8De5reSzKxsOyL0hLQZeRZw1Otl4Wr4CYXPeEgwTUizQe4f3i7d9zWJjDqOphH4+PtSiapP/p8n6onZdZzKN0UQIDAQAB";
	public static String PRIVATEKEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALRhqQRmAu0/40LUtjPVD8LFCpZp/Lk7NwWdMGAXR91tvqylIiqRFYULEKub3bqeHV+xJpOLgt9/Itc4cc+GPD/OF1M+kANNiIa4zH9ojwmhJZcsFnuUEplSTaYbRmKwlTmr2dQLqXVuEFxHNfejAEtQz+clqj4Qx2EdtvlpR6PbAgMBAAECgYEAnQ1u4JKFN9ZpY9XachSvLVDsH/xV+10QUgYDiZabjrKv0pfQwKkjyHvS3ufNzV8/igKu6MYOg9OpHSRbC2n/58OwApR6ZC6/9bxOx2jXJR5kQMD5mC2nrp/obixkB6w0XyKJTAOKMv2VsY2Z85FXPjoLlsbajywPyGf0Hsb5rXkCQQDXYZ1kJ1eBZFn/8eKJBD1EIB2RjYJTGKEi/Gp5SOr/GN4tDDvfPKWJklKCkstFE1IR+HdwjtTNkh3pccmW/ldvAkEA1mZKWgjNWcyyOBB0pk1GrvJEmZiIKdBLNmWK6ckVcINdGhS00Mw8amsHTugkc5drXYwnqG5w/Wdo4eNciYckVQJAT3E86Y0tVJecLrCceydqJa+m2GIWuOmzvcDW0QeZTiBxCrKhftTSxiyHbIwSl9bphaFdBNJaIb0MdWe4EI6d1wJAbdJjz46msZL6z2EOf4kh5cdPgGXziYeanziQWYSZKNMBQ7TLkhCr+c7tJWwgD93GL1r1jX9U/ViVDvBJnGDPKQJBAJk1jCjmPp6egXgDz4/HqPo97kpaslWm5IM8HoasHI1lFlyFlAXh7xdLeowdhBCVx3tVtC7gy3tlI+2YHlaE+Og=";
	/**
	 * 商家号  https://api.zfbill.net/gateway/api/scanpay
	 */
	private String merchantCode = "6000031505";

	/**
	 * 业务类型
	 */
	private String serviceType = "";

	/**
	 * 服务器异步通知地址
	 */
	private String notifyUrl = "";

	/**
	 * 接口版本
	 */
	private String interfaceVersion = "V3.1";

	/**
	 * 客户端IP
	 */
	private String clientIp = "";

	/**
	 * 签名方式
	 */
	private String signType = "RSA-S";

	/**
	 * 签名
	 */
	private String sign = "";

	/**
	 * 商户网站唯一订单号
	 */
	private String orderNo = "";

	/**
	 * 商户订单时间
	 */
	private String orderTime = DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT);

	/**
	 * 商户订单总金额
	 */
	private String orderAmount = "";

	/**
	 * 商品名称
	 */
	private String productName = RandomUtil.getRandomCode(6);
	
	/**
	 * 参数编码字符集
	 */
	private String inputCharset="UTF-8";
	
	/**
	 * 同步跳转地址
	 */
	private String returnUrl="";
	
	/**
	 * 银行编码
	 */
	private String bankCode="";
	
	/**
	 * 是否允许重复订单 1:不允许  0：允许
	 */
	private String redoFlag="1";
	
	private String extendParam="name^张三|sex^男";

	public String genSignStr() throws Exception {
		StringBuffer signSrc = new StringBuffer();
		signSrc.append("client_ip=").append(clientIp).append("&");
		signSrc.append("interface_version=").append(interfaceVersion).append("&");
		signSrc.append("merchant_code=").append(merchantCode).append("&");
		signSrc.append("notify_url=").append(notifyUrl).append("&");
		signSrc.append("order_amount=").append(orderAmount).append("&");
		signSrc.append("order_no=").append(orderNo).append("&");
		signSrc.append("order_time=").append(orderTime).append("&");
		signSrc.append("product_name=").append(productName).append("&");
		signSrc.append("service_type=").append(serviceType);
		
		String signInfo = signSrc.toString();
		System.out.println("智付签名加密："+signSrc);
		return RSAWithSoftware.signByPrivateKey(signInfo,PRIVATEKEY);
	}
	
	public String genBankSignStr() throws Exception {
		StringBuffer signSrc = new StringBuffer();
		if (StringUtils.isNotEmpty(bankCode)) {
			signSrc.append("bank_code=").append(bankCode).append("&");	
		}
		if (StringUtils.isNotEmpty(extendParam)) {
			signSrc.append("extend_param=").append(extendParam).append("&");	
		}
		signSrc.append("input_charset=").append(inputCharset).append("&");			
		signSrc.append("interface_version=").append(interfaceVersion).append("&");
		signSrc.append("merchant_code=").append(merchantCode).append("&");
		signSrc.append("notify_url=").append(notifyUrl).append("&");					
		signSrc.append("order_amount=").append(orderAmount).append("&");
		signSrc.append("order_no=").append(orderNo).append("&");		
		signSrc.append("order_time=").append(orderTime).append("&");			
		signSrc.append("product_name=").append(productName).append("&");
		if (StringUtils.isNotEmpty(redoFlag)) {
			signSrc.append("redo_flag=").append(redoFlag).append("&");	
		}
		if (StringUtils.isNotEmpty(returnUrl)) {
			signSrc.append("return_url=").append(returnUrl).append("&");	
		}		
		signSrc.append("service_type=").append(serviceType);
		
		String signInfo = signSrc.toString();
		System.out.println("网银RSA-S签名参数排序：" + signInfo);
		return RSAWithSoftware.signByPrivateKey(signInfo,PRIVATEKEY);
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getInterfaceVersion() {
		return interfaceVersion;
	}

	public void setInterfaceVersion(String interfaceVersion) {
		this.interfaceVersion = interfaceVersion;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
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

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getRedoFlag() {
		return redoFlag;
	}

	public void setRedoFlag(String redoFlag) {
		this.redoFlag = redoFlag;
	}

	public String getExtendParam() {
		return extendParam;
	}

	public void setExtendParam(String extendParam) {
		this.extendParam = extendParam;
	}
}
