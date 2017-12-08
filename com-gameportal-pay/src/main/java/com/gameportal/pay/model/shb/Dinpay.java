package com.gameportal.pay.model.shb;

import com.gameportal.pay.util.XstreamUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("dinpay")
public class Dinpay {
	@XStreamAlias("response")
	private ZFResponse response;

	public ZFResponse getResponse() {
		return response;
	}

	public void setResponse(ZFResponse response) {
		this.response = response;
	}
	
	public static void main(String[] args) {
		String str="<dinpay><response><interface_version>V3.1</interface_version><merchant_code>Z800888111028</merchant_code><order_amount>10.00</order_amount><order_no>2017040615144204</order_no><order_time>2017-04-06 15:14:42</order_time><qrcode>weixin://wxpay/bizpayurl?pr=42sKJ64</qrcode><resp_code>SUCCESS</resp_code><resp_desc>通讯成功</resp_desc><result_code>0</result_code><sign>GCN0mMOGqWE8bx9lA5+158WliTkuRUXtZc3VrqN2KGlsLFXZZ9E9nTZzypXzdLHjm3AvFlrsAkCQlwEEFSY/smbzUWA+kax/JPpG7jA/v48PhhLuATmCvFdLypWy4E3Xxp3bxLw46Rfstse+yDRbyeNO+c9MCntOKqizpKBsPTs=</sign><sign_type>RSA-S</sign_type><trade_no>Z1001220163</trade_no><trade_time>2017-04-06 15:14:43</trade_time></response></dinpay>";
		System.out.println(XstreamUtil.toBean(str, Dinpay.class).getResponse().getQrCode());
	}
}
