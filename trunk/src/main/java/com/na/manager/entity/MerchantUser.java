package com.na.manager.entity;

import java.io.Serializable;

import com.na.manager.enums.MerchantUserStatus;
import com.na.manager.enums.MerchantUserType;

/**
 * 商户表
 * 
 * @author
 * @time 2015-07-21 18:13:40
 */
public class MerchantUser extends LiveUser implements Serializable {

	private static final long serialVersionUID = -1839184093682713244L;

	private Long userId;
	private String number;
	private String privateKey;
	private String remark;
	/**
	 * 1 启用 2 停用
	 */
	private Integer status;
	/**
	 * 1线路商  2商户
	 */
	private Integer merchantType;
	
	private String returnUrl;
	/**
	 * 商户前缀
	 */
	private String merchantPrefix;
	/**
	 * 允许IP列表
	 */
	private String allowIpList;
	
	/**
	 * 包房数量
	 */
	private Integer roomMember;
	

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public MerchantUserStatus getStatusEnum() {
		if(status == null) {
			return null;
		}
		return MerchantUserStatus.get(status);
	}
	
	public void setStatusEnum(MerchantUserStatus status) {
		this.status = status.get();
	}

	public Integer getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(Integer merchantType) {
		this.merchantType = merchantType;
	}

	public MerchantUserType getMerchantTypeEnum() {
		if(merchantType == null) {
			return null;
		}
		return MerchantUserType.get(merchantType);
	}

	public void setMerchantTypeEnum(MerchantUserType merchantType) {
		this.merchantType = merchantType.get();
	}

	public String getMerchantPrefix() {
		return merchantPrefix;
	}

	public void setMerchantPrefix(String merchantPrefix) {
		this.merchantPrefix = merchantPrefix;
	}

	public String getAllowIpList() {
		return allowIpList;
	}

	public void setAllowIpList(String allowIpList) {
		this.allowIpList = allowIpList;
	}

	public Integer getRoomMember() {
		return roomMember;
	}

	public void setRoomMember(Integer roomMember) {
		this.roomMember = roomMember;
	}
}