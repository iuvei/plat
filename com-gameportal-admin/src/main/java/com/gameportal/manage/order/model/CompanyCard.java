package com.gameportal.manage.order.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;

public class CompanyCard extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "CompanyCard";
	public static final String ALIAS_CCID = "公司银行卡ID";
	public static final String ALIAS_CCHOLDER = "开户人";
	public static final String ALIAS_CCNO = "公司银行卡号";
	public static final String ALIAS_BANKNAME = "银行名称";
	public static final String ALIAS_OPENBANK = "开户行名称";
	public static final String ALIAS_BANKADDR = "开户行地址";
	public static final String ALIAS_REMARKS = "备注";
	public static final String ALIAS_STATUS = "状态 0未锁定 1锁定";
	public static final String ALIAS_CREATEUSERID = "创建者ID";
	public static final String ALIAS_CREATEUSERNAME = "创建者名称";
	public static final String ALIAS_CREATETIME = "创建时间";
	public static final String ALIAS_UPDATEUSERID = "修改者ID";
	public static final String ALIAS_UPDATEUSERNAME = "修改者名称";
	public static final String ALIAS_UPDATETIME = "修改时间";
	
	
	//columns START
	private java.lang.Long ccid;
	private java.lang.String ccholder;
	private java.lang.String ccno;
	private java.lang.String bankname;
	private java.lang.String openbank;
	private java.lang.String bankaddr;
	private java.lang.String remarks;
	private java.lang.Integer status;
	private java.lang.Long createuserid;
	private java.lang.String createusername;
	private java.sql.Timestamp createtime;
	private java.lang.Long updateuserid;
	private java.lang.String updateusername;
	private java.sql.Timestamp updatetime;
	//columns END

	public CompanyCard(){
	}

	public CompanyCard(
		java.lang.Long ccid
	){
		this.ccid = ccid;
	}

	
	public void setCcid(java.lang.Long value) {
		this.ccid = value;
	}
	
	public java.lang.Long getCcid() {
		return this.ccid;
	}
	
	public void setCcholder(java.lang.String value) {
		this.ccholder = value;
	}
	
	public java.lang.String getCcholder() {
		return this.ccholder;
	}
	
	public void setBankname(java.lang.String value) {
		this.bankname = value;
	}
	
	public java.lang.String getBankname() {
		return this.bankname;
	}
	public void setOpenbank(java.lang.String value) {
		this.openbank = value;
	}
	
	public java.lang.String getOpenbank() {
		return this.openbank;
	}
	
	public void setCcno(java.lang.String value) {
		this.ccno = value;
	}
	
	public java.lang.String getCcno() {
		return this.ccno;
	}
	
	public void setBankaddr(java.lang.String value) {
		this.bankaddr = value;
	}
	
	public java.lang.String getBankaddr() {
		return this.bankaddr;
	}
	
	public void setRemarks(java.lang.String value) {
		this.remarks = value;
	}
	
	public java.lang.String getRemarks() {
		return this.remarks;
	}
	
	public void setStatus(java.lang.Integer value) {
		this.status = value;
	}
	
	public java.lang.Integer getStatus() {
		return this.status;
	}
	
	public void setCreateuserid(java.lang.Long value) {
		this.createuserid = value;
	}
	
	public java.lang.Long getCreateuserid() {
		return this.createuserid;
	}
	
	public void setCreateusername(java.lang.String value) {
		this.createusername = value;
	}
	
	public java.lang.String getCreateusername() {
		return this.createusername;
	}
	
	public void setCreatetime(java.sql.Timestamp value) {
		this.createtime = value;
	}
	
	public java.sql.Timestamp getCreatetime() {
		return this.createtime;
	}
	
	public void setUpdateuserid(java.lang.Long value) {
		this.updateuserid = value;
	}
	
	public java.lang.Long getUpdateuserid() {
		return this.updateuserid;
	}
	
	public void setUpdateusername(java.lang.String value) {
		this.updateusername = value;
	}
	
	public java.lang.String getUpdateusername() {
		return this.updateusername;
	}
	
	public void setUpdatetime(java.sql.Timestamp value) {
		this.updatetime = value;
	}
	
	public java.sql.Timestamp getUpdatetime() {
		return this.updatetime;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("Ccid",getCcid())
			.append("Ccholder",getCcholder())
			.append("Ccno",getCcno())
			.append("Bankname",getBankname())
			.append("Openbank",getOpenbank())
			.append("Bankaddr",getBankaddr())
			.append("Remarks",getRemarks())
			.append("Status",getStatus())
			.append("Createuserid",getCreateuserid())
			.append("Createusername",getCreateusername())
			.append("Createtime",getCreatetime())
			.append("Updateuserid",getUpdateuserid())
			.append("Updateusername",getUpdateusername())
			.append("Updatetime",getUpdatetime())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getCcid())
			.append(getCcholder())
			.append(getCcno())
			.append(getBankname())
			.append(getOpenbank())
			.append(getBankaddr())
			.append(getRemarks())
			.append(getStatus())
			.append(getCreateuserid())
			.append(getCreateusername())
			.append(getCreatetime())
			.append(getUpdateuserid())
			.append(getUpdateusername())
			.append(getUpdatetime())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof CompanyCard == false) return false;
		if(this == obj) return true;
		CompanyCard other = (CompanyCard)obj;
		return new EqualsBuilder()
			.append(getCcid(),other.getCcid())
			.append(getCcholder(),other.getCcholder())
			.append(getCcno(),other.getCcno())
			.append(getBankname(),other.getBankname())
			.append(getOpenbank(),other.getOpenbank())
			.append(getBankaddr(),other.getBankaddr())
			.append(getRemarks(),other.getRemarks())
			.append(getStatus(),other.getStatus())
			.append(getCreateuserid(),other.getCreateuserid())
			.append(getCreateusername(),other.getCreateusername())
			.append(getCreatetime(),other.getCreatetime())
			.append(getUpdateuserid(),other.getUpdateuserid())
			.append(getUpdateusername(),other.getUpdateusername())
			.append(getUpdatetime(),other.getUpdatetime())
			.isEquals();
	}
	



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return  this.ccid;
	}
}

