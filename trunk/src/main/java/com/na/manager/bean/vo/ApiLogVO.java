package com.na.manager.bean.vo;
/**
 * @author andy
 * @date 2017年6月23日 下午3:23:28
 * 
 */
public class ApiLogVO {
	private String merchantNo;
	private String ip;
	private Integer operDesc;
	private String errorDesc;
	private String createTime;
	private Integer status;
	private String encryptData;
	private String decryptData;
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getOperDesc() {
		return operDesc;
	}
	public void setOperDesc(Integer operDesc) {
		this.operDesc = operDesc;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getEncryptData() {
		return encryptData;
	}
	public void setEncryptData(String encryptData) {
		this.encryptData = encryptData;
	}
	public String getDecryptData() {
		return decryptData;
	}
	public void setDecryptData(String decryptData) {
		this.decryptData = decryptData;
	}
}
