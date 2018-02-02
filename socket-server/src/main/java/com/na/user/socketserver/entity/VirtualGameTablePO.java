package com.na.user.socketserver.entity;


import java.io.Serializable;
import java.math.BigDecimal;

import com.na.user.socketserver.common.enums.VirtualGameTableStatus;
import com.na.user.socketserver.common.enums.VirtualGameTableType;

/**
 * 虚拟游戏桌子
 * 与gameTable现实桌台 一对多关系
 * @author nick
 * @date 2017年4月24日 上午11:43:58
 */
public class VirtualGameTablePO implements Serializable {
	
	private Integer id;
	
	private Integer gameTableId;
	
	private Integer gameId;
	
	/**
	 * 虚拟房间类型
	 * @see com.na.baccarat.socketserver.common.enums.VirtualTableTypeEnum
	 */
	private Integer type;
	
	/**
	 * 房主ID
	 */
	private Long ownerId;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 进入玩家数
	 */
	private Integer maxMembers;
	
	/**
	 * 进入最低余额
	 */
	private BigDecimal minBalance;
	
	/**
	 * 对冲占成比(代理VIP房间需要)
	 */
	private BigDecimal hedgePercentage;
	
	/**
	 * 非对冲占成(代理VIP房间需要)
	 */
	private BigDecimal noHedgePercentage;
	
	/**
	 * 打水占成比(代理VIP房间需要)
	 */
	private BigDecimal waterPercentage;
	
	/**
	 * 房间名称
	 */
	private String roomName;
	
	/**
	 * 状态
	 */
	private Integer status;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGameTableId() {
		return gameTableId;
	}

	public void setGameTableId(Integer gameTableId) {
		this.gameTableId = gameTableId;
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}


	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	
	public VirtualGameTableType getTypeEnum() {
		if(type == null) {
			return null;
		}
		return VirtualGameTableType.get(type);
	}

	public void setTypeEnum(VirtualGameTableType typeEnum) {
		this.type = typeEnum.get();
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public BigDecimal getMinBalance() {
		return minBalance;
	}

	public void setMinBalance(BigDecimal minBalance) {
		this.minBalance = minBalance;
	}

	public BigDecimal getIntoPercentage() {
		return hedgePercentage;
	}

	public void setIntoPercentage(BigDecimal intoPercentage) {
		this.hedgePercentage = intoPercentage;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Integer getMaxMembers() {
		return maxMembers;
	}

	public void setMaxMembers(Integer maxMembers) {
		this.maxMembers = maxMembers;
	}

	public Integer getStatus() {
		return status;
	}
	
	public VirtualGameTableStatus getStatusEnum() {
		if(status == null) {
			return null;
		}
		return VirtualGameTableStatus.get(status);
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getNoHedgePercentage() {
		return noHedgePercentage;
	}

	public void setNoHedgePercentage(BigDecimal noHedgePercentage) {
		this.noHedgePercentage = noHedgePercentage;
	}

	public BigDecimal getWaterPercentage() {
		return waterPercentage;
	}

	public void setWaterPercentage(BigDecimal waterPercentage) {
		this.waterPercentage = waterPercentage;
	}
	
	

}
