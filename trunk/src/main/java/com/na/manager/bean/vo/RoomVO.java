package com.na.manager.bean.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 包房对冲报表
 * 
 * @author andy
 * @date 2017年6月22日 下午2:29:46
 * 
 */
public class RoomVO implements Serializable {
	private static final long serialVersionUID = 8064983590778309494L;
	// 代理账号
	private String loginName;
	// 代理总金额
	private BigDecimal balance;

	// 统计类型
	private Integer statisType;

	// 统计统计类型描述
	private String statisTypeDesc;

	// 包房编号
	private String roomNumber;

	// 代理账户
	private String createUser;

	// 代理昵称
	private String nickName;

	// 包房名称
	private String roomName;

	// 局号
	private String roundId;

	// 开牌结果 1庄 2闲 3和
	private String openResult;

	// 代理洗码比
	private BigDecimal proxyWashPercentage;

	// 对冲庄投注额
	private BigDecimal bankTotal;

	// 对冲闲投注额
	private BigDecimal playerTotal;

	//对冲庄赢金额
	private BigDecimal hedgeBankWinTotal;
	
	// 余量金额
	private BigDecimal ylTotal;
	
	//庄赢金额
	private BigDecimal bankerWinTotal;
	

	// 非对冲金额
	private BigDecimal noHedgeTotal;

	// 对冲占成比
	private BigDecimal hedgePercentage;

	// 非对冲占成
	private BigDecimal noHedgePercentage;

	// 打水占成
	private BigDecimal waterPercentage;

	// 余量佣金
	private BigDecimal ylCommission;

	// 庄赢抽水
	private BigDecimal waterCommission;

	// 非对冲佣金
	private BigDecimal nohedgeCommission;

	// 余量输赢
	private BigDecimal ylWinLost;

	// 非对冲输赢
	private BigDecimal noHedgeWinLost;

	// 余量收益
	private BigDecimal ylSettle;

	// 抽水收益
	private BigDecimal waterSettle;

	// 非对冲收益
	private BigDecimal noHedgeSettle;

	// 交公司
	private BigDecimal companySettle;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Integer getStatisType() {
		return statisType;
	}

	public void setStatisType(Integer statisType) {
		this.statisType = statisType;
	}

	public String getStatisTypeDesc() {
		return statisTypeDesc;
	}

	public void setStatisTypeDesc(String statisTypeDesc) {
		this.statisTypeDesc = statisTypeDesc;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRoundId() {
		return roundId;
	}

	public void setRoundId(String roundId) {
		this.roundId = roundId;
	}

	public String getOpenResult() {
		return openResult;
	}

	public void setOpenResult(String openResult) {
		this.openResult = openResult;
	}

	public BigDecimal getProxyWashPercentage() {
		return proxyWashPercentage;
	}

	public void setProxyWashPercentage(BigDecimal proxyWashPercentage) {
		this.proxyWashPercentage = proxyWashPercentage;
	}

	public BigDecimal getBankTotal() {
		return bankTotal;
	}

	public void setBankTotal(BigDecimal bankTotal) {
		this.bankTotal = bankTotal;
	}

	public BigDecimal getPlayerTotal() {
		return playerTotal;
	}

	public void setPlayerTotal(BigDecimal playerTotal) {
		this.playerTotal = playerTotal;
	}

	public BigDecimal getHedgeBankWinTotal() {
		return hedgeBankWinTotal;
	}

	public void setHedgeBankWinTotal(BigDecimal hedgeBankWinTotal) {
		this.hedgeBankWinTotal = hedgeBankWinTotal;
	}

	public BigDecimal getNoHedgeTotal() {
		return noHedgeTotal;
	}

	public void setNoHedgeTotal(BigDecimal noHedgeTotal) {
		this.noHedgeTotal = noHedgeTotal;
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

	public BigDecimal getWaterCommission() {
		return waterCommission;
	}

	public void setWaterCommission(BigDecimal waterCommission) {
		this.waterCommission = waterCommission;
	}

	public BigDecimal getNohedgeCommission() {
		return nohedgeCommission;
	}

	public void setNohedgeCommission(BigDecimal nohedgeCommission) {
		this.nohedgeCommission = nohedgeCommission;
	}

	public BigDecimal getNoHedgeWinLost() {
		return noHedgeWinLost;
	}

	public void setNoHedgeWinLost(BigDecimal noHedgeWinLost) {
		this.noHedgeWinLost = noHedgeWinLost;
	}

	public BigDecimal getWaterSettle() {
		return waterSettle;
	}

	public void setWaterSettle(BigDecimal waterSettle) {
		this.waterSettle = waterSettle;
	}

	public BigDecimal getNoHedgeSettle() {
		return noHedgeSettle;
	}

	public void setNoHedgeSettle(BigDecimal noHedgeSettle) {
		this.noHedgeSettle = noHedgeSettle;
	}

	public BigDecimal getCompanySettle() {
		return companySettle;
	}

	public void setCompanySettle(BigDecimal companySettle) {
		this.companySettle = companySettle;
	}

	public BigDecimal getBankerWinTotal() {
		return bankerWinTotal;
	}

	public void setBankerWinTotal(BigDecimal bankerWinTotal) {
		this.bankerWinTotal = bankerWinTotal;
	}

	public BigDecimal getYlTotal() {
		return ylTotal;
	}

	public void setYlTotal(BigDecimal ylTotal) {
		this.ylTotal = ylTotal;
	}

	public BigDecimal getYlCommission() {
		return ylCommission;
	}

	public void setYlCommission(BigDecimal ylCommission) {
		this.ylCommission = ylCommission;
	}

	public BigDecimal getYlWinLost() {
		return ylWinLost;
	}

	public void setYlWinLost(BigDecimal ylWinLost) {
		this.ylWinLost = ylWinLost;
	}

	public BigDecimal getYlSettle() {
		return ylSettle;
	}

	public void setYlSettle(BigDecimal ylSettle) {
		this.ylSettle = ylSettle;
	}
}
