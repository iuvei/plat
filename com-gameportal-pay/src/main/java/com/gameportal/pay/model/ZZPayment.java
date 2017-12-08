package com.gameportal.pay.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.gameportal.pay.model.zz.zzwxResponse;
import com.gameportal.pay.util.HttpUtil;
import com.gameportal.pay.util.IdGenerator;
import com.gameportal.pay.util.SFMD5Util;
import com.gameportal.pay.util.SignatureUtil;
import com.gameportal.pay.util.WebConst;
import com.gameportal.pay.util.XstreamUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class ZZPayment implements Serializable {

	private static final long serialVersionUID = 1L;

	// 接口类型
	private String service = "pay.weixin.native";

	// 版本号
	private String version = "2.0";

	// 字符集
	private String charset = "UTF-8";

	// 签名方式
	@XStreamAlias("sign_type")
	private String signType = "MD5";

	// 商户号
	@XStreamAlias("mch_id")
	private String mchId = "";

	// 商户订单号
	@XStreamAlias("out_trade_no")
	private String outTradeNo = "";

	// 商品描述
	private String body = WebConst.getSubjectList().get(new Random().nextInt(32)).trim();

	// 附加信息
	private String attach = String.valueOf(new Date().getTime());

	// 总金额 单位：分
	@XStreamAlias("total_fee")
	private int totalFee;

	// 终端 IP
	@XStreamAlias("mch_create_ip")
	private String mchCreateIp;

	// 通知地址
	@XStreamAlias("notify_url")
	private String notifyUrl;

	// 随机字符串 、不长于 32 位
	@XStreamAlias("nonce_str")
	private Long nonceStr = new Date().getTime();

	// 签名
	private String sign = "";

	public String buildSignatrue(String key) {
		// MD5(原字符串&key=商户密钥).toUpperCase()
		Map<String, Object> map = new HashMap<>();
		map.put("body", body);
		map.put("mch_create_ip", mchCreateIp);
		map.put("nonce_str", nonceStr+"");
		map.put("mch_id",mchId);
		map.put("notify_url", notifyUrl);
		map.put("out_trade_no", outTradeNo);
		map.put("service", service);
		map.put("total_fee", totalFee+"");
		map.put("version", version);
		map.put("charset", charset);
		map.put("sign_type", signType);
		map.put("attach", attach);
		
		String source =SignatureUtil.hex(map);
		source += "&key="+key;
		System.out.println("Before MD5："+source);
		source= SFMD5Util.encryption(source).toUpperCase();
		System.out.println("After MD5："+source);
		return source;
	}
	
	public static void main(String[] args) {
		ZZPayment zz = new ZZPayment();
		zz.setMchId("7559527001");
		zz.setOutTradeNo(IdGenerator.genOrdId16(""));
		zz.setTotalFee(1);
		zz.setMchCreateIp("127.0.0.1");
		zz.setNotifyUrl("http://app.hhh656.pw/pay/zzwx/callback.html");
		zz.setSign(zz.buildSignatrue("35918c57313e4d6183d3cebdb5d79573"));
		XStream xstream = XstreamUtil.initXStream();
		xstream.processAnnotations(ZZPayment.class);
		String param =xstream.toXML(zz).replace("__", "_");
		System.out.println(param);
		String result = HttpUtil.doPost("http://pay.szzhangzhi.cn/gateway/", param, "UTF-8");
		System.out.println(result.replace("<![CDATA[", "").replace("]]>", ""));
		
		System.out.println(XstreamUtil.toBean(result,zzwxResponse.class).getCode_url());
	}

	public ZZPayment() {
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

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

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public int getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}

	public String getMchCreateIp() {
		return mchCreateIp;
	}

	public void setMchCreateIp(String mchCreateIp) {
		this.mchCreateIp = mchCreateIp;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public Long getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(Long nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
