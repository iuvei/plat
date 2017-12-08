package com.gameportal.pay.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.gameportal.pay.util.CoderUtil;
import com.gameportal.pay.util.HttpUtil;
import com.gameportal.pay.util.IdGenerator;
import com.gameportal.pay.util.MD5Util;
import com.gameportal.pay.util.RandomUtil;
import com.gameportal.pay.util.WebConst;

public class YPayment implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 应用ID
	 */
	private String appid="10028";
	
	/**
	 * 订单号
	 */
	private String orderid=IdGenerator.genOrdId16("");
	
	/**
	 * 商品名称
	 */
	private String subject= WebConst.getSubjectList().get(new Random().nextInt(32)).trim();
	
	/**
	 * 充值金额 （单位：分）
	 */
	private String fee="100";
	
	/**
	 * 接收交易结果的通知地址
	 */
	private String tongbu_url="http://app.qianbaobet.com/pay/ypwx/callback.html";
	
	/**
	 * 透传参数
	 */
	private String cpparam = RandomUtil.getRandomCode(6);
	
	/**
	 * 用户IP
	 */
	private String clientip="127.0.0.1"; 
	
	/**
	 * 支付返回页
	 */
	private String back_url="http://app.qianbaobet.com/pay/ypwx/view.html";
	
	/**
	 * 订单签名数据
	 */
	private String sign="";
	
	/**
	 * 客户端模式
	 */
	private String sfrom="pc";
	
	/**
	 * 支付方式
	 * 0或者空则跳转收银台，
	 * 21 WAP微信支付，
	 * 22 WAP支付宝支付，
	 * 23 WAP银联支付
	 * 31 PC微信支付，
	 * 32 PC支付宝支付，
	 * 33 PC银联支付
	 */
	private String paytype="31";

	public String getAppid() {
		return appid;
	}
	
	public String signauture(String key){
		//sign = Md5(appid+orderid+fee+tongbu_url+appkey)
		String source =appid+orderid+fee+tongbu_url;
		try {
			source =MD5Util.signByMD5(source, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return source.toLowerCase();
	}
	public Map<String, Object> getMap(String key){
		Map<String, Object> map = new HashMap<>();
		map.put("appid", appid);
		map.put("orderid", orderid);
		try {
			map.put("subject", URLEncoder.encode(subject,"utf8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		map.put("fee", fee);
		map.put("tongbu_url", tongbu_url);
		map.put("clientip", clientip);
		map.put("back_url", back_url);
		map.put("sign", signauture("ba545f136267f54db995abe5b4f5b313"));
		map.put("sfrom", sfrom);
		map.put("paytype", paytype);
		return map;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		YPayment yp = new YPayment();
		Map<String, Object> map = new HashMap<>();
		map.put("appid", yp.getAppid());
		map.put("orderid", yp.getOrderid());
		map.put("subject", URLEncoder.encode(yp.getSubject(),"utf8"));
		map.put("fee", yp.getFee());
		map.put("tongbu_url", yp.getTongbu_url());
		map.put("clientip", yp.getClientip());
		map.put("back_url", yp.getBack_url());
		map.put("sign", yp.signauture("ba545f136267f54db995abe5b4f5b313"));
		map.put("sfrom", yp.getSfrom());
		map.put("paytype", yp.getPaytype());
		String result;
		try {
			result = new String(HttpUtil.doPost("http://sanfang.youpay.cc/dealpay.php",map),"utf-8");
			System.out.println("返回結果"+result);
		
		//System.out.println(HttpUtil.doPost("http://sanfang.youpay.cc/sanfang_qrcode.php?url=weixin://wxpay/bizpayurl?pr=GRuACJe",null));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		
//		System.out.println(String.valueOf(new BigDecimal(1).subtract(new BigDecimal(0.01)).doubleValue()));
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getTongbu_url() {
		return tongbu_url;
	}

	public void setTongbu_url(String tongbu_url) {
		this.tongbu_url = tongbu_url;
	}

	public String getCpparam() {
		return cpparam;
	}

	public void setCpparam(String cpparam) {
		this.cpparam = cpparam;
	}

	public String getClientip() {
		return clientip;
	}

	public void setClientip(String clientip) {
		this.clientip = clientip;
	}

	public String getBack_url() {
		return back_url;
	}

	public void setBack_url(String back_url) {
		this.back_url = back_url;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSfrom() {
		return sfrom;
	}

	public void setSfrom(String sfrom) {
		this.sfrom = sfrom;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
}
