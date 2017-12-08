package com.gameportal.manage.betlog.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;


public class SysParam extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "SysParam";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_PARAMKEY = "paramkey";
	public static final String ALIAS_PARAMVALUE = "paramvalue";
	
	
	//columns START
	private java.lang.Long id;
	private java.lang.String paramkey;
	private java.lang.String paramvalue;
	//columns END

	public SysParam(){
	}

	public SysParam(
		java.lang.Long id
	){
		this.id = id;
	}

	
	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getId() {
		return this.id;
	}
	
	public void setParamkey(java.lang.String value) {
		this.paramkey = value;
	}
	
	public java.lang.String getParamkey() {
		return this.paramkey;
	}
	
	public void setParamvalue(java.lang.String value) {
		this.paramvalue = value;
	}
	
	public java.lang.String getParamvalue() {
		return this.paramvalue;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("Id",getId())
			.append("Paramkey",getParamkey())
			.append("Paramvalue",getParamvalue())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.append(getParamkey())
			.append(getParamvalue())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof SysParam == false) return false;
		if(this == obj) return true;
		SysParam other = (SysParam)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.append(getParamkey(),other.getParamkey())
			.append(getParamvalue(),other.getParamvalue())
			.isEquals();
	}
	



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return  this.id;
	}
}

