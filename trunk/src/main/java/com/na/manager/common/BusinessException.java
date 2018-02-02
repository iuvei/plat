package com.na.manager.common;

/**
 * @author Andy
 * @version 创建时间：2017年10月6日 下午2:46:45
 */
public class BusinessException extends RuntimeException {
	private String key;
	private Object[] args;

	public BusinessException(String key, Object[] args) {
		super();
		this.key = key;
		this.args = args;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
}
