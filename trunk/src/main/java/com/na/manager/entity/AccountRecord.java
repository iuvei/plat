package com.na.manager.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.manager.enums.AccountRecordType;

/**
 * 资金流水表
 * 
 * @author andy
 * @date 2017年6月22日 上午11:45:46
 * 
 */
public class AccountRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 主键 */
	@JSONField(serialize = false)
	private Long id;
	/** 用户主键 */
	private Long userId;
	/** 流水号 */
	private String sn;
	/** 发生时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date time;
	/** 本次发生金额 */
	private BigDecimal amount;
	/** 帐变前余额 */
	private BigDecimal preBalance;
	/** 类型(1转入 2转出 3 下注 4返奖 5 返还) */
	private Integer type;
	/** 关联字段（3、4、5 注单表ID） */
	private String businessKey;
	/** 备注 */
	private String remark;
	/** 操作人 */
	private String execUser;

	private BigDecimal afterAmount;
	/** 登录名 */
	private String loginName;
	
	/** 推送标识 0：未推送 1:已推送 */
	private Integer flag;

	/**用户当前余额（实时）*/
	private BigDecimal currentBalance;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getPreBalance() {
		return preBalance;
	}

	public void setPreBalance(BigDecimal preBalance) {
		this.preBalance = preBalance;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public AccountRecordType getTypeEnum() {
		if(type == null) {
			return null;
		}
		return AccountRecordType.get(type);
	}

	public void setTypeEnum(AccountRecordType type) {
		this.type = type.get();
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getExecUser() {
		return execUser;
	}

	public void setExecUser(String execUser) {
		this.execUser = execUser;
	}

	public BigDecimal getAfterAmount() {
		return afterAmount;
	}

	public void setAfterAmount(BigDecimal afterAmount) {
		this.afterAmount = afterAmount;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@JSONField(name = "id")
	public String getIdStr(){
		return this.id+"";
	}

	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
}
