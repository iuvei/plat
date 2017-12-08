package com.gameportal.pay.model;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.gameportal.pay.util.DateUtil;
import com.gameportal.pay.util.MD5Util;
import com.gameportal.pay.util.RandomUtil;
import com.gameportal.pay.util.StringUtil;

/**
 * 中联支付。
 * 
 * @author Administrator
 *
 */
public class UnitedPayment {

	/**
	 * 中联分配的商户号
	 */
	private String merchantCode;

	/**
	 * 商户系统唯一的订单编号
	 */
	private String outOrderId;

	/**
	 * 第三方订单号
	 */
	private String instructCode;

	/**
	 * 交易类型
	 */
	private String transType;

	/**
	 * 交易时间
	 */
	private String transTime;

	/**
	 * 商户系统中的会员ID
	 */
	private String outUserId;

	/**
	 * 支付金额：分
	 */
	private Long totalAmount;

	/**
	 * 商品名称
	 */
	private String goodsName = "";

	/**
	 * 商品描述
	 */
	private String goodsDescription = "";
	
	/**
	 * 订单创建时间
	 */
	private String merchantOrderTime = DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT_MEDIA);

	/**
	 * 最晚支付时间
	 */
	private String lastestPayTime = DateUtil.getStrByDate(DateUtil.addDay(new Date(), 1), DateUtil.TIME_FORMAT_MEDIA);

	/**
	 * 商户取货URL
	 */
	private String merUrl;

	/**
	 * 通知商户服务端地址
	 */
	private String notifyUrl;

	/**
	 * 随机字符 随机字符
	 */
	private String randomStr = RandomUtil.getRandomCode(29);

	/**
	 * 签名
	 */
	private String sign;
	
	public UnitedPayment(){}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getOutOrderId() {
		return outOrderId;
	}

	public void setOutOrderId(String outOrderId) {
		this.outOrderId = outOrderId;
	}

	public String getInstructCode() {
		return instructCode;
	}

	public void setInstructCode(String instructCode) {
		this.instructCode = instructCode;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getOutUserId() {
		return outUserId;
	}

	public void setOutUserId(String outUserId) {
		this.outUserId = outUserId;
	}

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsDescription() {
		return goodsDescription;
	}

	public void setGoodsDescription(String goodsDescription) {
		this.goodsDescription = goodsDescription;
	}

	public String getMerchantOrderTime() {
		return merchantOrderTime;
	}

	public void setMerchantOrderTime(String merchantOrderTime) {
		this.merchantOrderTime = merchantOrderTime;
	}

	public String getLastestPayTime() {
		return lastestPayTime;
	}

	public void setLastestPayTime(String lastestPayTime) {
		this.lastestPayTime = lastestPayTime;
	}

	public String getMerUrl() {
		return merUrl;
	}

	public void setMerUrl(String merUrl) {
		this.merUrl = merUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getRandomStr() {
		return randomStr;
	}

	public void setRandomStr(String randomStr) {
		this.randomStr = randomStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignStr(String key) {
		Map<String, Object> parmMaps = new HashMap<String, Object>();
		parmMaps.put("merchantCode", merchantCode);
		parmMaps.put("notifyUrl", notifyUrl);
		parmMaps.put("outOrderId", outOrderId);
		parmMaps.put("totalAmount", totalAmount);
		parmMaps.put("outUserId", outUserId);
		parmMaps.put("merchantOrderTime", merchantOrderTime);
		try {
			String[] signFields = new String[6];
			signFields[0] = "merchantCode";
			signFields[1] = "notifyUrl";
			signFields[2] = "outOrderId";
			signFields[3] = "totalAmount";
			signFields[4] = "outUserId";
			signFields[5] = "merchantOrderTime";
			JSONObject param = (JSONObject) JSONObject.toJSON(parmMaps);
			String signSrc = orgSignSrc(signFields, param);
			// MD5的方式签名
			signSrc += "&KEY=" + key;
			System.out.println("签名加密前："+signSrc);
			return MD5Util.getMD5Encode(signSrc).toUpperCase();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String getCallSignStr(String key) {
		Map<String, Object> parmMaps = new HashMap<String, Object>();
		parmMaps.put("instructCode", instructCode);
		parmMaps.put("merchantCode", merchantCode);
		parmMaps.put("outOrderId", outOrderId);
		parmMaps.put("totalAmount", totalAmount);
		parmMaps.put("transTime", transTime);
		parmMaps.put("transType", transType);
		try {
			String[] signFields = new String[6];
			signFields[0] = "instructCode";
			signFields[1] = "merchantCode";
			signFields[2] = "outOrderId";
			signFields[3] = "totalAmount";
			signFields[4] = "transTime";
			signFields[5] = "transType";
			JSONObject param = (JSONObject) JSONObject.toJSON(parmMaps);
			String signSrc = orgSignSrc(signFields, param);
			// MD5的方式签名
			signSrc += "&KEY=" + key;
			return MD5Util.getMD5Encode(signSrc).toUpperCase();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static UnitedPayment getCallParam(String param){
		UnitedPayment unPayment = new UnitedPayment();
		Map<String, String> params = new HashMap<String, String>();
		String [] str1 = null;
		for(String str : param.split("&")){
			str1 =str.split("=");
			if(str1.length >1){
				params.put(str1[0], str1[1]);
			}
		}
		unPayment.setSign(params.get("sign"));
		unPayment.setTransType(params.get("transType"));
		unPayment.setInstructCode(params.get("instructCode"));
		unPayment.setTransTime(params.get("transTime"));
		unPayment.setTotalAmount(Long.valueOf(params.get("totalAmount")));
		unPayment.setMerchantCode(params.get("merchantCode"));
		unPayment.setOutOrderId(params.get("outOrderId"));
		return unPayment;
	}

	/**
	 * 构建签名原文
	 * 
	 * @param signFilds
	 * @param param
	 * @return
	 */
	private static String orgSignSrc(String[] signFields, JSONObject param) {
		if (signFields != null) {
			Arrays.sort(signFields); // 对key按照 字典顺序排序
		}
		StringBuffer signSrc = new StringBuffer("");
		int i = 0;
		for (String field : signFields) {
			signSrc.append(field);
			signSrc.append("=");
			signSrc.append((StringUtil.isEmpty(param.getString(field)) ? "" : param.getString(field)));
			// 最后一个元素后面不加&
			if (i < (signFields.length - 1)) {
				signSrc.append("&");
			}
			i++;
		}
		return signSrc.toString();
	}
	public static void main(String[] args) {
		String str ="sign=9DE7309DF48DD8A5063FF5D05B97DF73&result=&transType=00200&instructCode=11001224431&waitTime=&autoJump=1&transTime=20150914184117&totalAmount=100&merchantCode=1000000391&outOrderId=2015091418390403";
		System.out.println(str);
		UnitedPayment un =getCallParam(str);
		System.out.println(un.getCallSignStr("f0ab44bc-553d-4616-97ff-04c1f319e18d"));
		
	}
}
