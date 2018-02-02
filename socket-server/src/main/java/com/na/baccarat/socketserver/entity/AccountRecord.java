package com.na.baccarat.socketserver.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * Created by Administrator on 2017/5/3 0003.
 */
public class AccountRecord {
    /**主键*/
    private Long id;
    /**用户主键*/
    private Long userId;
    /**流水号*/
    private String sn;
    /**发生时间*/
    private Date time;
    /**本次发生金额*/
    private BigDecimal amount;
    /**帐变前余额*/
    private BigDecimal preBalance;
    /**类型(1转入 2转出 3 下注 4返奖 5 返还)*/
    private Integer type;
    /**关联字段（3、4、5 注单表ID）*/
    private String businessKey;
    /**备注*/
    private String remark;
    /**操作人*/
    private String execUser;

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
    
}
