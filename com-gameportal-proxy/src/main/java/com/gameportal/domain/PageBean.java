package com.gameportal.domain;

import java.io.Serializable;

/**
 * 分页类
 * @author YTO_ZJ
 *
 */
public class PageBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8159422585675210632L;
	
	/**
	 * 每页记录数  默认10条
	 */
	private int pageSize = 20;

	/**
	 * 总记录数
	 */
	private int totalResults;
	
	/**
	 * 当前页数
	 */
	private int currentPage = 1;

	/**
	 * 总页数
	 */
	private int totalPages;
	
	/**
	 * 开始记录行
	 */
	private int start;
	
	/**
	 * 结束记录行
	 */
	private int end;
	
	private void init() {
		// 设置总页数
		setTotalPages();
		// 校正从jsp传过来的currentPage
		adjustCurrentPage();
		// 设置起始记录行号
		setStart();
		// 设置结束记录行号
		setEnd();
	}
	
	public int getEnd() {
		return end;
	}

	public void setEnd() {
		this.end = this.start + (pageSize - 1);
	}

	public int getStart() {
		return start;
	}

	public void setStart() {
		this.start = this.pageSize * ((this.currentPage <= 0 ? 1 : this.currentPage) - 1);
	}
	
	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
		init();
	}

	/**
	 * 校正从jsp传过来的currentPage
	 * 如果 currentPage 小于1, 则校正为1
	 * 如果 currentPage 大于总页数(totalPages), 则校正为totalPages
	 */
	private void adjustCurrentPage() {
		this.currentPage = this.currentPage < 1 ? 1 : (this.currentPage > this.totalPages ? this.totalPages : this.currentPage);
	}

	/**
	 * 获取当前页数
	 * 
	 * @return
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * 设置当前页数
	 * 
	 * @param currentPage
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * 获取每页记录数
	 * 
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页记录数
	 * 
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		this.setTotalPages();
	}

	/**
	 * 获取总页数
	 * 
	 * @return
	 */
	public int getTotalPages() {
		return totalPages;
	}

	/**
	 * 获取总记录数
	 * 
	 * @return
	 */
	public int getTotalResults() {
		return totalResults;
	}

	/**
	 * 设置总页数
	 */
	private void setTotalPages() {
		this.totalPages = (int) Math.ceil(this.totalResults
				/ (double) this.pageSize);
	}

}
