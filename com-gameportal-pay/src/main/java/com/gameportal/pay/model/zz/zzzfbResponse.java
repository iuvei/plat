package com.gameportal.pay.model.zz;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class zzzfbResponse {

	private String charset;
	
	private String code_img_url;
	
	private String code_url;
	
	private String mch_id;
	
	private String nonce_str;
	
	private String result_code;
	
	private String sign;
	
	private String sign_type;
	
	private String status;
	
	private String version;

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getCode_img_url() {
		return code_img_url;
	}

	public void setCode_img_url(String code_img_url) {
		this.code_img_url = code_img_url;
	}

	public String getCode_url() {
		return code_url;
	}

	public void setCode_url(String code_url) {
		this.code_url = code_url;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
