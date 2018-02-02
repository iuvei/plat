package com.na.baccarat.socketserver.command.requestpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.baccarat.socketserver.common.enums.BetOrderSourceEnum;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;

/**
 * 加入房间请求参数
 * 
 * @author alan
 * @date 2017年4月29日 下午4:37:36
 */
public class QuickChangeRoomPara extends CommandReqestPara {

	/**
	 * 换之前游戏ID
	 */
	@JSONField(name = "preGameId")
	private Integer preGameId;
	/**
     * 离开的实体桌ID
     */
    @JSONField(name = "preTid")
    private Integer preTableId;
    
    /**
     * 离开的虚拟桌ID
     */
    @JSONField(name = "preVtid")
    private Integer preVirtualTableId;
    
	/**
     * 实体桌ID
     */
    @JSONField(name = "tid")
    private Integer tableId;
    /**
     * 虚拟桌ID
     */
    @JSONField(name = "vtid")
    private Integer virtualTableId;
    
    /**
     * 限红ID
     */
    private Integer chipId;
    
    /**
     * 加入座位类型(默认为1)
     * 1为进座位   2为旁注
     */
    private Integer source;
    
    /**
	 * 游戏ID
	 */
	@JSONField(name = "gameId")
	private Integer gameId;

    
    
	public Integer getPreTableId() {
		return preTableId;
	}

	public void setPreTableId(Integer preTableId) {
		this.preTableId = preTableId;
	}

	public Integer getPreVirtualTableId() {
		return preVirtualTableId;
	}

	public void setPreVirtualTableId(Integer preVirtualTableId) {
		this.preVirtualTableId = preVirtualTableId;
	}

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public Integer getVirtualTableId() {
		return virtualTableId;
	}

	public void setVirtualTableId(Integer virtualTableId) {
		this.virtualTableId = virtualTableId;
	}

	public Integer getChipId() {
		return chipId;
	}

	public void setChipId(Integer chipId) {
		this.chipId = chipId;
	}

	
	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public BetOrderSourceEnum getSourceEnum() {
		if(this.source != null) {
			return BetOrderSourceEnum.get(this.source);
		}
		return null;
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	public Integer getPreGameId() {
		return preGameId;
	}

	public void setPreGameId(Integer preGameId) {
		this.preGameId = preGameId;
	}
	
}
