package com.gameportal.comms.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * 抓取任务
 * @author brooke
 *
 */
public class CrawlJobAppException extends Exception{

	private static final long serialVersionUID = 1229846250593234149L;
	private String errorCode; 
	private String fieldName;
	private String mesageNo;

	/**
	 * @return the mesageNo
	 */
	public String getMesageNo() {
		return mesageNo;
	}

	/**
	 * @param mesageNo the mesageNo to set
	 */
	public void setMesageNo(String mesageNo) {
		this.mesageNo = mesageNo;
	}

	/**
	 * @return the params
	 */
	public List<String> getParams() {
		return params;
	}

	private List<String> params = new ArrayList<String>();

	private String errorMessage;

	public CrawlJobAppException() {

	}

	public CrawlJobAppException(String errorMessage) {
		setErrorMessage(errorMessage);
	}

	public CrawlJobAppException(String errorCode, String errorMessage) {
		this(errorMessage);
		this.errorCode = errorCode;;
	}
	
	public CrawlJobAppException(String errorCode, String fieldName, String errorMessage) {
		this(fieldName, errorMessage);
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
