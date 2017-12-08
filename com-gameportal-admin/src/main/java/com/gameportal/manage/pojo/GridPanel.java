package com.gameportal.manage.pojo;

public class GridPanel extends BasePojo {
	private Long totalProperty = null;
	private Object data = null;
	private boolean successProperty = false;

	
	public GridPanel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GridPanel(Long totalProperty, Object data, boolean successProperty) {
		super();
		this.totalProperty = totalProperty;
		this.data = data;
		this.successProperty = successProperty;
	}

	public Long getTotalProperty() {
		return totalProperty;
	}

	public void setTotalProperty(Long totalProperty) {
		this.totalProperty = totalProperty;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean isSuccessProperty() {
		return successProperty;
	}

	public void setSuccessProperty(boolean successProperty) {
		this.successProperty = successProperty;
	}

}
