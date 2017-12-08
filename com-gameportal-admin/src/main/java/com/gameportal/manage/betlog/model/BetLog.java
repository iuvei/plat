package com.gameportal.manage.betlog.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;

public class BetLog extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//alias
	public static final String TABLE_ALIAS = "Betlog";
	public static final String ALIAS_PDID = "流水号ID";
	public static final String ALIAS_ACCOUNT = "用户ID";
	public static final String ALIAS_PLATFORMCODE = "平台代码";
	public static final String ALIAS_GAMECODE = "游戏代码";
	public static final String ALIAS_GAMENAME = "gamename";
	public static final String ALIAS_GAME_CATEGORY = "gameCategory";
	public static final String ALIAS_BETDATE = "押单时间";
	public static final String ALIAS_BETNO = "注单号码";
	public static final String ALIAS_BETAMOUNT = "下注金额";
	public static final String ALIAS_PROFITAMOUNT = "派彩金额";
	public static final String ALIAS_CURRENCY = "币别";
	public static final String ALIAS_TORMBRATE = "转人民币汇率";
	public static final String ALIAS_FINALAMOUNT = "输赢金额";
	public static final String ALIAS_ORIGIN = "押单装置(I.苹果手机 A.安卓手机 P.电脑)";
	public static final String ALIAS_GAME_RESULT = "gameResult";
	public static final String ALIAS_RESULT = "result";
	
	
	//columns START
	private java.lang.Long pdid;
	private java.lang.String account;
	private java.lang.String platformcode;
	private java.lang.String gamecode;
	private java.lang.String gamename;
	private java.lang.String gameCategory;
	private java.lang.String betdate; //投注时间
	private java.lang.String betno;
	private java.math.BigDecimal betamount;
	private java.math.BigDecimal profitamount;
	private java.lang.String currency;
	private java.math.BigDecimal tormbrate;
	private java.math.BigDecimal finalamount;
	private java.lang.String origin;
	private java.lang.String gameResult;
	private java.lang.String result;
	private java.math.BigDecimal validBetAmount;//有效投注额
	private String commission;//退水
	/**
	 * 结算状态
	 * 1:已结算
	 * 0：未结算
	 * -1：重置试玩额度
	 * -2：注单被篡改
	 * -8：此局注单被取消
	 * -9：取消注单
	 */
	private java.lang.String flag;
	private java.lang.String playType;//游戏玩法
	private java.lang.String tableCode;//桌子编号
	private java.lang.String inningsCode;//局号
	private java.lang.String loginIP;//登录IP
	private java.math.BigDecimal beforeCerdit;//下注前余额
	/**
	 * 游戏厅类型
	 * DSP – AG 国际厅; 
	 * AGQ –AG 旗舰厅; 
	 * VIP – VIP 包桌厅;
	 * SLOT – 电子游戏; 
	 * LED – 竞咪厅;
	 */
	private String round;
	private String recalcuTime;//注单重新派彩时间
	private String roundNo;//靴号场次
	//columns END

	public BetLog(){
	}

	public BetLog(
		java.lang.Long pdid
	){
		this.pdid = pdid;
	}

	
	public void setPdid(java.lang.Long value) {
		this.pdid = value;
	}
	
	public java.lang.Long getPdid() {
		return this.pdid;
	}
	
	public void setAccount(java.lang.String value) {
		this.account = value;
	}
	
	public java.lang.String getAccount() {
		return this.account;
	}
	
	public void setPlatformcode(java.lang.String value) {
		this.platformcode = value;
	}
	
	public java.lang.String getPlatformcode() {
		return this.platformcode;
	}
	
	public void setGamecode(java.lang.String value) {
		this.gamecode = value;
	}
	
	public java.lang.String getGamecode() {
		return this.gamecode;
	}
	
	public void setGamename(java.lang.String value) {
		this.gamename = value;
	}
	
	public java.lang.String getGamename() {
		return this.gamename;
	}
	
	public void setGameCategory(java.lang.String value) {
		this.gameCategory = value;
	}
	
	public java.lang.String getGameCategory() {
		return this.gameCategory;
	}
	
	
	
	public void setBetno(java.lang.String value) {
		this.betno = value;
	}
	
	public java.lang.String getBetno() {
		return this.betno;
	}
	
	public void setBetamount(java.math.BigDecimal value) {
		this.betamount = value;
	}
	
	public java.math.BigDecimal getBetamount() {
		return this.betamount;
	}
	
	public void setProfitamount(java.math.BigDecimal value) {
		this.profitamount = value;
	}
	
	public java.math.BigDecimal getProfitamount() {
		return this.profitamount;
	}
	
	public void setCurrency(java.lang.String value) {
		this.currency = value;
	}
	
	public java.lang.String getCurrency() {
		return this.currency;
	}
	
	public void setTormbrate(java.math.BigDecimal value) {
		this.tormbrate = value;
	}
	
	public java.math.BigDecimal getTormbrate() {
		return this.tormbrate;
	}
	
	public void setFinalamount(java.math.BigDecimal value) {
		this.finalamount = value;
	}
	
	public java.math.BigDecimal getFinalamount() {
		return this.finalamount;
	}
	
	public void setOrigin(java.lang.String value) {
		this.origin = value;
	}
	
	public java.lang.String getOrigin() {
		return this.origin;
	}
	
	public void setGameResult(java.lang.String value) {
		this.gameResult = value;
	}
	
	public java.lang.String getGameResult() {
		return this.gameResult;
	}
	
	public void setResult(java.lang.String value) {
		this.result = value;
	}
	
	public java.lang.String getResult() {
		return this.result;
	}
	

	public java.math.BigDecimal getValidBetAmount() {
		return validBetAmount;
	}

	public void setValidBetAmount(java.math.BigDecimal validBetAmount) {
		this.validBetAmount = validBetAmount;
	}

	public java.lang.String getFlag() {
		return flag;
	}

	public void setFlag(java.lang.String flag) {
		this.flag = flag;
	}

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public String getTableCode() {
		return tableCode;
	}

	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}

	public String getInningsCode() {
		return inningsCode;
	}

	public void setInningsCode(String inningsCode) {
		this.inningsCode = inningsCode;
	}

	public String getLoginIP() {
		return loginIP;
	}

	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}

	public java.math.BigDecimal getBeforeCerdit() {
		return beforeCerdit;
	}

	public void setBeforeCerdit(java.math.BigDecimal beforeCerdit) {
		this.beforeCerdit = beforeCerdit;
	}
	

	public String getRound() {
		return round;
	}

	public void setRound(String round) {
		this.round = round;
	}

	
	public String getRecalcuTime() {
		return recalcuTime;
	}

	public void setRecalcuTime(String recalcuTime) {
		this.recalcuTime = recalcuTime;
	}
	
	

	public java.lang.String getBetdate() {
		return betdate;
	}

	public void setBetdate(java.lang.String betdate) {
		this.betdate = betdate;
	}

	public String getRoundNo() {
		return roundNo;
	}

	public void setRoundNo(String roundNo) {
		this.roundNo = roundNo;
	}
	
	public String getCommission() {
		return commission;
	}

	public void setCommission(String commission) {
		this.commission = commission;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("Pdid",getPdid())
			.append("Account",getAccount())
			.append("Platformcode",getPlatformcode())
			.append("Gamecode",getGamecode())
			.append("Gamename",getGamename())
			.append("GameCategory",getGameCategory())
			.append("Betdate",getBetdate())
			.append("Betno",getBetno())
			.append("Betamount",getBetamount())
			.append("Profitamount",getProfitamount())
			.append("Currency",getCurrency())
			.append("Tormbrate",getTormbrate())
			.append("Finalamount",getFinalamount())
			.append("Origin",getOrigin())
			.append("GameResult",getGameResult())
			.append("Result",getResult())
			.append("Flag",getFlag())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getPdid())
			.append(getAccount())
			.append(getPlatformcode())
			.append(getGamecode())
			.append(getGamename())
			.append(getGameCategory())
			.append(getBetdate())
			.append(getBetno())
			.append(getBetamount())
			.append(getProfitamount())
			.append(getCurrency())
			.append(getTormbrate())
			.append(getFinalamount())
			.append(getOrigin())
			.append(getGameResult())
			.append(getResult())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof BetLog == false) return false;
		if(this == obj) return true;
		BetLog other = (BetLog)obj;
		return new EqualsBuilder()
			.append(getPdid(),other.getPdid())
			.append(getAccount(),other.getAccount())
			.append(getPlatformcode(),other.getPlatformcode())
			.append(getGamecode(),other.getGamecode())
			.append(getGamename(),other.getGamename())
			.append(getGameCategory(),other.getGameCategory())
			.append(getBetdate(),other.getBetdate())
			.append(getBetno(),other.getBetno())
			.append(getBetamount(),other.getBetamount())
			.append(getProfitamount(),other.getProfitamount())
			.append(getCurrency(),other.getCurrency())
			.append(getTormbrate(),other.getTormbrate())
			.append(getFinalamount(),other.getFinalamount())
			.append(getOrigin(),other.getOrigin())
			.append(getGameResult(),other.getGameResult())
			.append(getResult(),other.getResult())
			.isEquals();
	}
	



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return  this.pdid;
	}
}

