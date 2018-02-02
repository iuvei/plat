package com.na.manager.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.manager.common.annotation.I18NField;
import com.na.manager.enums.VirtualGameTableStatus;
import com.na.manager.enums.VirtualGameTableType;

/**
 * 虚拟游戏桌子 与gameTable现实桌台 一对多关系
 * 
 * @author nick
 * @date 2017年4月24日 上午11:43:58
 */
public class VirtualGameTable implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer gameTableId;

	private Integer gameId;

	/**
	 * 房间名称
	 */
	private String roomName;

	/**
	 * 房间类型 房间类型 1.虚拟房间 2.VIP对冲房 3 非对冲房
	 */
	private Integer type;

	/**
	 * 房间密码
	 */
	private String password;

	/**
	 * 员满人数
	 */
	private Integer maxMembers;

	/**
	 * 进入最低余额
	 */
	private BigDecimal minBalance;

	/**
	 * 对冲占成比
	 */
	private BigDecimal hedgePercentage;
	
	/**
	 * 非对冲占成
	 */
	private BigDecimal noHedgePercentage;
	
	/**
	 * 打水占成
	 */
	private BigDecimal waterPercentage;

	/**
	 * 房主ID
	 */
	private Long ownerId;
	
	/**
	 * 上级ID
	 */
	private Long parentId;
	
	/**
	 * 创建者
	 */
	private String createUser;

	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	/**
	 * 状态 0 取消 1 正常
	 */
	private Integer status;
	
	/**
	 * 关联游戏桌
	 */
	@I18NField
	private String tableName;
	
	/**
	 * 关联游戏
	 */
	@I18NField
	private String gameName;
	
	private String ids;
	
	public String getLid() {
		if (this.getHedgePercentage() != null && this.getNoHedgePercentage() != null
				&& this.getWaterPercentage() != null) {
			return this.getHedgePercentage().divide(new BigDecimal(100)).doubleValue() + "-"
					+ this.getNoHedgePercentage().divide(new BigDecimal(100)).doubleValue() + "-"
					+ this.getWaterPercentage().divide(new BigDecimal(100)).doubleValue();
		}
		return null;
	}

	@JSONField(serialize=false)
	public VirtualGameTableType getTypeEnum() {
		if (type == null) {
			return null;
		}
		return VirtualGameTableType.get(this.type);
	}
	
	@JSONField(serialize=false)
	public VirtualGameTableStatus getStatusEnum() {
		if (status == null) {
			return null;
		}
		return VirtualGameTableStatus.get(this.status);
	}

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

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getMaxMembers() {
		return maxMembers;
	}

	public void setMaxMembers(Integer maxMembers) {
		this.maxMembers = maxMembers;
	}

	public BigDecimal getMinBalance() {
		return minBalance;
	}

	public void setMinBalance(BigDecimal minBalance) {
		this.minBalance = minBalance;
	}

	public BigDecimal getHedgePercentage() {
		return hedgePercentage;
	}

	public void setHedgePercentage(BigDecimal hedgePercentage) {
		this.hedgePercentage = hedgePercentage;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
}
