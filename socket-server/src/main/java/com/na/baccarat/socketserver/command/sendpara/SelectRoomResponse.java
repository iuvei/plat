package com.na.baccarat.socketserver.command.sendpara;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.IResponse;

/**
 * Created by Administrator on 2017/4/27 0027.
 */
public class SelectRoomResponse implements IResponse {
	/**
	 * 返回的虚拟桌信息
	 */
	public class VirtualTableInfo {
		
		/**
		 * 虚拟桌Id
		 */
		@JSONField(name = "vtid")
		public Integer tableId;

		/**
		 * 桌子名称
		 */
		public String name;
	    /**
	     * 人数
	     */
	    @JSONField(name = "playerNumber")
	    public Integer playerNumber;
	    /**
	     * 是否满员
	     */
	    @JSONField(name = "isFull")
	    public Boolean isFull;

		public BigDecimal minBalance;

		public Integer tid;
    }
	
	/**
     * 所有虚拟桌数据
     */
    @JSONField(name = "vtlist")
    private List<VirtualTableInfo> tableList;

    private Integer tid;
    /**
     * 虚拟桌总数
     */
    private Integer total;
    /**
     * VIP房间总人数
     */
    private Integer totalPlayer;
    
	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	public List<VirtualTableInfo> getTableList() {
		return tableList;
	}

	public void setTableList(List<VirtualTableInfo> tableList) {
		this.tableList = tableList;
	}

	public Integer getTotalPlayer() {
		return totalPlayer;
	}

	public void setTotalPlayer(Integer totalPlayer) {
		this.totalPlayer = totalPlayer;
	}
    
}
