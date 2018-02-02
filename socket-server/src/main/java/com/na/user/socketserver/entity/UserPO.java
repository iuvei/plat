package com.na.user.socketserver.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.na.user.socketserver.common.enums.UserStatusEnum;
import com.na.user.socketserver.common.enums.UserTypeEnum;

/**
 * @author
 * @time 2015-07-21 18:13:40
 */
public class UserPO implements Serializable {
	
	private Logger log = LoggerFactory.getLogger(UserPO.class);

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String loginName;
	private String password;
	private String nickName;
	private BigDecimal balance;

	/**
	 * 用户状态 0正常1冻结2删除
	 */
	private Integer userStatus;
	/**
	 * 条形码
	 */
	private String barrcode;
	/**
	 * 1 系统用户 2 荷官用户 3 子账号 4 真人用户
	 */
	private Integer userType;
	/**
	 * 用户头像
	 */
	private String headPic;
	/**
	 * 用户加密用的盐
	 */
	private String passwordSalt;
	
	/**
	 * 用户登录ID
	 */
	private Long loginId;
	
	/**
	 * 是否在桌内
	 */
	private boolean isInTable;
	
	/**
	 * 当前用户所在游戏桌ID 不包括多台
	 */
	private Integer tableId;
	
	/**
	 * 用户手机设备号
	 */
	private String deviceNumber;
	
	/**
	 * 游戏
	 */
	private String gameCode;
	
	/**
     * 下注设备类型：1 WEB  2 IOS  3 ANDROID 4 PC
     */
    private Integer deviceType;
    
    /**
     * 设备信息
     */
    private String deviceInfo;

	public String getHeadPic() {
		return headPic;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		log.debug("用户： " + this.id + ",账户原余额 : " + this.balance + ",新余额:" + balance);
		this.balance = balance;
	}
	public String getBarrcode() {
		return barrcode;
	}
	public void setBarrcode(String barrcode) {
		this.barrcode = barrcode;
	}
	public Integer getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	
	@JsonIgnore
	public UserTypeEnum getUserTypeEnum() {
		return UserTypeEnum.get(this.userType);
	}
	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}
	
	@JsonIgnore
	public UserStatusEnum getUserStatusEnum() {
		return UserStatusEnum.get(this.getUserStatus());
	}
	public Long getLoginId() {
		return loginId;
	}
	public void setLoginId(Long loginId) {
		this.loginId = loginId;
	}
	public Integer getTableId() {
		return tableId;
	}
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPasswordSalt() {
		return passwordSalt;
	}
	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public boolean isInTable() {
		return isInTable;
	}
	public void setInTable(boolean isInTable) {
		this.isInTable = isInTable;
	}
	public String getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	public String getGameCode() {
		return gameCode;
	}
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	public Integer getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserPO other = (UserPO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}