package com.gameportal.manage.system.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SystemField extends BaseEntity {

	// alias
	public static final String TABLE_ALIAS = "SystemField";
	public static final String ALIAS_FIELD_ID = "字段ID";
	public static final String ALIAS_FIELD = "字段";
	public static final String ALIAS_FIELD_NAME = "字段名称";
	public static final String ALIAS_VALUE_FIELD = "字段值";
	public static final String ALIAS_DISPLAY_FIELD = "字段显示值";
	public static final String ALIAS_ENABLED = "是否启用";
	public static final String ALIAS_SORT = "排序";

	// columns START
	private java.lang.Long fieldId;
	private java.lang.String field;
	private java.lang.String fieldName;
	private java.lang.String valueField;
	private java.lang.String displayField;
	private java.lang.Integer enabled;
	private java.lang.Integer sort;

	// columns END

	public SystemField() {
	}

	public SystemField(java.lang.Long fieldId) {
		this.fieldId = fieldId;
	}

	public void setFieldId(java.lang.Long value) {
		this.fieldId = value;
	}

	public java.lang.Long getFieldId() {
		return this.fieldId;
	}

	public void setField(java.lang.String value) {
		this.field = value;
	}

	public java.lang.String getField() {
		return this.field;
	}

	public void setFieldName(java.lang.String value) {
		this.fieldName = value;
	}

	public java.lang.String getFieldName() {
		return this.fieldName;
	}

	public void setValueField(java.lang.String value) {
		this.valueField = value;
	}

	public java.lang.String getValueField() {
		return this.valueField;
	}

	public void setDisplayField(java.lang.String value) {
		this.displayField = value;
	}

	public java.lang.String getDisplayField() {
		return this.displayField;
	}

	public void setEnabled(java.lang.Integer value) {
		this.enabled = value;
	}

	public java.lang.Integer getEnabled() {
		return this.enabled;
	}

	public void setSort(java.lang.Integer value) {
		this.sort = value;
	}

	public java.lang.Integer getSort() {
		return this.sort;
	}

	public String toString() {
		return new ToStringBuilder(this).append("FieldId", getFieldId())
				.append("Field", getField())
				.append("FieldName", getFieldName())
				.append("ValueField", getValueField())
				.append("DisplayField", getDisplayField())
				.append("Enabled", getEnabled()).append("Sort", getSort())
				.toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getFieldId()).append(getField())
				.append(getFieldName()).append(getValueField())
				.append(getDisplayField()).append(getEnabled())
				.append(getSort()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof SystemField == false)
			return false;
		if (this == obj)
			return true;
		SystemField other = (SystemField) obj;
		return new EqualsBuilder().append(getFieldId(), other.getFieldId())
				.append(getField(), other.getField())
				.append(getFieldName(), other.getFieldName())
				.append(getValueField(), other.getValueField())
				.append(getDisplayField(), other.getDisplayField())
				.append(getEnabled(), other.getEnabled())
				.append(getSort(), other.getSort()).isEquals();
	}

	@Override
	public Serializable getID() {

		return this.fieldId;
	}
}
