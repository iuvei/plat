package com.gameportal.web.util;


public class PageTool {

	/**
	 * 分页组件
	 * 
	 * @param sum
	 * @param pageId
	 * @param maxRows
	 * @return
	 */
	public static long getPage(long total,long pageSize){
		//计算总页数
		long pageCount = 0;
		if(total>0){
			if ((total % pageSize) == 0) {
				pageCount = total / pageSize;
			} else {
				pageCount = total / pageSize + 1;
			}
		}
		return pageCount;
	}
	
}
