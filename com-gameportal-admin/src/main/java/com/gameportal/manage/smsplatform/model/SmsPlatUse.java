package com.gameportal.manage.smsplatform.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;

public class SmsPlatUse extends BaseEntity {

	// alias
	public static final String TABLE_ALIAS = "SmsPlatUse";
	public static final String ALIAS_SPUID = "spuid";
	public static final String ALIAS_NAME = "name";
	public static final String ALIAS_CONTENT = "content";
	public static final String ALIAS_CREATETIME = "createtime";

	// columns START
	private java.lang.Long spuid;
	private java.lang.String name;
	private java.lang.String content;
	private java.sql.Timestamp createtime;

	// columns END

	public SmsPlatUse() {
	}

	public SmsPlatUse(java.lang.Long spuid) {
		this.spuid = spuid;
	}

	public void setSpuid(java.lang.Long value) {
		this.spuid = value;
	}

	public java.lang.Long getSpuid() {
		return this.spuid;
	}

	public void setName(java.lang.String value) {
		this.name = value;
	}

	public java.lang.String getName() {
		return this.name;
	}

	public void setContent(java.lang.String value) {
		this.content = value;
	}

	public java.lang.String getContent() {
		return this.content;
	}

	public void setCreatetime(java.sql.Timestamp value) {
		this.createtime = value;
	}

	public java.sql.Timestamp getCreatetime() {
		return this.createtime;
	}

	public String toString() {
		return new ToStringBuilder(this).append("Spuid", getSpuid())
				.append("Name", getName()).append("Content", getContent())
				.append("Createtime", getCreatetime()).toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getSpuid()).append(getName())
				.append(getContent()).append(getCreatetime()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof SmsPlatUse == false)
			return false;
		if (this == obj)
			return true;
		SmsPlatUse other = (SmsPlatUse) obj;
		return new EqualsBuilder().append(getSpuid(), other.getSpuid())
				.append(getName(), other.getName())
				.append(getContent(), other.getContent())
				.append(getCreatetime(), other.getCreatetime()).isEquals();
	}

	@Override
	public Serializable getID() {

		return this.spuid;
	}
}
