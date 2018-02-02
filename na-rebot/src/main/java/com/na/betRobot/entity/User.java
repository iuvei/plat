package com.na.betRobot.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.na.betRobot.entity.enums.UserStatus;
import com.na.betRobot.entity.enums.UserType;

/**
 * @author
 * @time 2015-07-21 18:13:40
 */
public class User implements Serializable {
	
	private Logger log = LoggerFactory.getLogger(User.class);

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
	
	public UserType getUserTypeEnum() {
		return UserType.get(this.userType);
	}
	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}
	
	public UserStatus getUserStatusEnum() {
		return UserStatus.get(this.getUserStatus());
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
	public boolean isInTable() {
		return isInTable;
	}
	public void setInTable(boolean isInTable) {
		this.isInTable = isInTable;
	}
	
}