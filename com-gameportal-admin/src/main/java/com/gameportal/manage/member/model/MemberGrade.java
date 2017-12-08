package com.gameportal.manage.member.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;


public class MemberGrade extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "MemberGrade";
	public static final String ALIAS_GID = "gid";
	public static final String ALIAS_GRADE = "grade";
	public static final String ALIAS_BETAMONT = "betamont";
	public static final String ALIAS_REMARK = "remark";
	
	
	//columns START
	private java.lang.Long gid;
	
	/**
	 * 等级标示
	 */
	private java.lang.Integer grade;
	
	/**
	 * 下注额
	 */
	private java.lang.Long betamont;
	
	/**
	 * 每日提款次数
	 */
	private java.lang.Integer withdrawalcount;
	
	/**
	 * 单笔提款金额
	 */
	private String withdrawalquota;
	
	/**
	 * 备注
	 */
	private java.lang.String remark;
	//columns END

	public MemberGrade(){
	}

	public MemberGrade(
		java.lang.Long gid
	){
		this.gid = gid;
	}

	
	public void setGid(java.lang.Long value) {
		this.gid = value;
	}
	
	public java.lang.Long getGid() {
		return this.gid;
	}
	
	public void setGrade(java.lang.Integer value) {
		this.grade = value;
	}
	
	public java.lang.Integer getGrade() {
		return this.grade;
	}
	
	public void setBetamont(java.lang.Long value) {
		this.betamont = value;
	}
	
	public java.lang.Long getBetamont() {
		return this.betamont;
	}
	
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	
	public java.lang.String getRemark() {
		return this.remark;
	}
	

	public java.lang.Integer getWithdrawalcount() {
		return withdrawalcount;
	}

	public void setWithdrawalcount(java.lang.Integer withdrawalcount) {
		this.withdrawalcount = withdrawalcount;
	}

	public String getWithdrawalquota() {
		return withdrawalquota;
	}

	public void setWithdrawalquota(String withdrawalquota) {
		this.withdrawalquota = withdrawalquota;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("Gid",getGid())
			.append("Grade",getGrade())
			.append("Betamont",getBetamont())
			.append("Remark",getRemark())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getGid())
			.append(getGrade())
			.append(getBetamont())
			.append(getRemark())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof MemberGrade == false) return false;
		if(this == obj) return true;
		MemberGrade other = (MemberGrade)obj;
		return new EqualsBuilder()
			.append(getGid(),other.getGid())
			.append(getGrade(),other.getGrade())
			.append(getBetamont(),other.getBetamont())
			.append(getRemark(),other.getRemark())
			.isEquals();
	}
	



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return  this.gid;
	}
}

