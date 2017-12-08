package com.gameportal.pay.model;

import java.util.Date;
import java.util.Random;

import com.gameportal.pay.util.DateUtil;
import com.gameportal.pay.util.RandomUtil;
import com.gameportal.pay.util.WebConst;

/**
 * 泽圣支付
 * 
 * @author sun
 *
 */
public class ZSPayment {
	// 模块名
	private String model = "QR_CODE";
	// 商户号
	private String merchantCode;
	// 商户订单号
	private String outOrderId;
	// 设备号
	private String deviceNo = RandomUtil.getRandomCode(18);
	// 支付金额 分
	private Long amount;
	// 商品名称
	private String goodsName = WebConst.getSubjectList().get(new Random().nextInt(32)).trim();
	// 扩展字段
	private String ext = "";
	// 创建时间
	private String orderCreateTime = DateUtil.getStrByDate(new Date(), "yyyyMMddHHmmss");

	private String noticeUrl; // 通知商户服务端地址
	public int isSupportCredit = 1;// 必填，默认1-代表支持信用卡
	public String ip = "192.168.1.1";// 必填， 这个必须填写为商户的IP,或者用户的IP
	public String sign = "";// 必填
	public String payChannel = "21"; // 21 微信，30-支付宝，31-QQ钱包
	//INSERT INTO `a_pay_platform` VALUES ('233', 'PC-泽圣-支付宝扫码', 'http://payment.zsagepay.com/scan/entrance.do', null, null, null, null, '1', '2017-08-01 22:28:42', '2017-08-01 22:28:49', '1', '1', '0.0100');
	//INSERT INTO `a_pay_platform` VALUES ('234', 'WAP-泽圣-支付宝扫码', 'http://payment.zsagepay.com/scan/entrance.do', null, null, null, null, '1', '2017-08-01 22:28:44', '2017-08-01 22:28:51', '1', '0', '0.0100');


	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

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

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getOrderCreateTime() {
		return orderCreateTime;
	}

	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}

	public String getNoticeUrl() {
		return noticeUrl;
	}

	public void setNoticeUrl(String noticeUrl) {
		this.noticeUrl = noticeUrl;
	}

	public int getIsSupportCredit() {
		return isSupportCredit;
	}

	public void setIsSupportCredit(int isSupportCredit) {
		this.isSupportCredit = isSupportCredit;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}
}
