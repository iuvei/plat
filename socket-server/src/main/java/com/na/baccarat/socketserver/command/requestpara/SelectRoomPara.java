package com.na.baccarat.socketserver.command.requestpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.baccarat.socketserver.common.enums.SelectRoomSearchType;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.enums.VirtualGameTableType;

/**
 * 选择房间
 * 
 * @author alan
 * @date 2017年4月29日 下午4:37:36
 */
public class SelectRoomPara extends CommandReqestPara {
	
	/**
	 * 类型  1为普通  2为代理VIP
	 */
	private Integer type;
	/**
     * 实体桌ID
     */
    @JSONField(name = "tid")
    private Integer tableId;
    
    /**
     * 页数 1,2,3,4....
     */
    private Integer pageNum;

	/**
	 * 每页条数.
	 */
	private Integer pageSize = 12;
    
    /**
     * 搜索使用
     */
    private Integer vTableId;
    
    /**
     * 1.满人房 2.缺人房 3.空房
     */
    private Integer searchFlag;
    
	public Integer getvTableId() {
		return vTableId;
	}

	public void setvTableId(Integer vTableId) {
		this.vTableId = vTableId;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public Integer getType() {
		return type;
	}
	
	public VirtualGameTableType getTypeEnum() {
		if(type == null) {
			return null;
		}
		return VirtualGameTableType.get(type);
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSearchFlag() {
		return searchFlag;
	}
	
	public SelectRoomSearchType getSearchFlagEnum() {
		if(searchFlag == null) {
			return null;
		}
		return SelectRoomSearchType.get(searchFlag);
	}

	public void setSearchFlag(Integer searchFlag) {
		this.searchFlag = searchFlag;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if(pageSize!=null) {
			this.pageSize = pageSize;
		}
	}
}
