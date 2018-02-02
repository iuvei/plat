package com.na.manager.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sunny on 2017/6/22 0022.
 */
public class Page<T> implements Serializable{
    private long total;
    private long pageSize;
    private List<T> data;
    private Object otherInfo;

    public Page(PageCondition pageCondition) {
        this.pageSize = pageCondition.getPageSize();
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotalPage() {
        long t = total%pageSize;
        return t==0 ? total/pageSize : total/pageSize+1;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public Object getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(Object otherInfo) {
		this.otherInfo = otherInfo;
	}
}
