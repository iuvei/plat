package com.na.manager.bean;

/**
 * Created by Sunny on 2017/6/22 0022.
 */
public class PageCondition {
    private long currentPage;
    private long pageSize;

    public PageCondition() {
    	currentPage = 1;
    	pageSize = 20;
    }

    public long getStartRow(){
         return  (currentPage - 1) * pageSize;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

}
