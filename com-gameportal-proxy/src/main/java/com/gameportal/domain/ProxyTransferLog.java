package com.gameportal.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理转账记录
 * 
 * @author sum
 *
 */
public class ProxyTransferLog extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 转帐记录ID
	 */
	private Integer tid;

	/**
	 * 转账ID
	 */
	private long pid;

	/**
	 * 转账账号
	 */
	private String paccount;

	/**
	 * 收款人ID
	 */
	private long lid;

	/**
	 * 收款人姓名
	 */
	private String laccount;

	/**
	 * 金额
	 */
	private Integer amount;

	/**
	 * 创建时间
	 */
	private Date createdate;

	/**
	 * 操作前余额
	 */
	private BigDecimal beforebalance;

	/**
	 * 操作后余额
	 */
	private BigDecimal afterbalance;
	
	/**
	 * 下级操作前余额
	 */
	private BigDecimal lbeforebalance;
	
	/**
	 * 下级操作后余额
	 */
	private BigDecimal lafterbalance;

	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getPaccount() {
		return paccount;
	}

	public void setPaccount(String paccount) {
		this.paccount = paccount;
	}

	public long getLid() {
		return lid;
	}

	public void setLid(long lid) {
		this.lid = lid;
	}

	public String getLaccount() {
		return laccount;
	}

	public void setLaccount(String laccount) {
		this.laccount = laccount;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public BigDecimal getBeforebalance() {
		return beforebalance;
	}

	public void setBeforebalance(BigDecimal beforebalance) {
		this.beforebalance = beforebalance;
	}

	public BigDecimal getAfterbalance() {
		return afterbalance;
	}

	public void setAfterbalance(BigDecimal afterbalance) {
		this.afterbalance = afterbalance;
	}

	public BigDecimal getLbeforebalance() {
		return lbeforebalance;
	}

	public void setLbeforebalance(BigDecimal lbeforebalance) {
		this.lbeforebalance = lbeforebalance;
	}

	public BigDecimal getLafterbalance() {
		return lafterbalance;
	}

	public void setLafterbalance(BigDecimal lafterbalance) {
		this.lafterbalance = lafterbalance;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
