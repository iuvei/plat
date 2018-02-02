package com.na.baccarat.socketserver.command.sendpara;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.baccarat.socketserver.common.enums.BetOrderSourceEnum;
import com.na.baccarat.socketserver.entity.UserBet;
import com.na.user.socketserver.command.sendpara.CommandResponse;

/**
 * 回到原桌位指令。
 * Created by sunny on 2017/4/27 0027.
 */
public class SeatGetBackResponse extends CommandResponse {
	
	@JSONField(name = "gid")
	private Integer gameId;
	
    @JSONField(name = "seat")
    private Integer seat;
    
    @JSONField(name = "tableId")
    private Integer tableId;
    
    /**
     * 虚拟桌ID
     */
    @JSONField(name = "vid")
    private Integer virtualTableId;
    @JSONField(name = "uid")
    private Long userId;
    @JSONField(name = "chipId")
    private Integer chipId;
    /**
     * 来源
     * @see com.na.baccarat.socketserver.common.enums.BetOrderSourceEnum
     */
    private Integer source;
    
//    @JSONField(name = "totype")
//    private Integer toType;
//    @JSONField(name = "jointype")
//    private String joinType;
    
    //用户投注细节
    private List<UserBet> userBetDetail;
    
    public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	public List<UserBet> getUserBetDetail() {
		return userBetDetail;
	}

	public void setUserBetDetail(List<UserBet> userBetDetail) {
		this.userBetDetail = userBetDetail;
	}

	public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getChipId() {
        return chipId;
    }

    public void setChipId(Integer chipId) {
        this.chipId = chipId;
    }
    
    public Integer getVirtualTableId() {
		return virtualTableId;
	}

	public void setVirtualTableId(Integer virtualTableId) {
		this.virtualTableId = virtualTableId;
	}

//	public Integer getToType() {
//        return toType;
//    }
//
//    public void setToType(Integer toType) {
//        this.toType = toType;
//    }
//
//    public String getJoinType() {
//        return joinType;
//    }
//
//    public void setJoinType(String joinType) {
//        this.joinType = joinType;
//    }

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}
	
	public void setSourceEnum(BetOrderSourceEnum betOrderSourceEnum) {
		this.source = betOrderSourceEnum.get();
	}
}
