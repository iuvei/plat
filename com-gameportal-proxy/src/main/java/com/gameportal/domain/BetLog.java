package com.gameportal.domain;

/**
 * 注单记录
 * @author leron
 *
 */
public class BetLog extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5114481494515606535L;

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
	public java.lang.Long getPdid() {
		return pdid;
	}
	public void setPdid(java.lang.Long pdid) {
		this.pdid = pdid;
	}
	public java.lang.String getAccount() {
		return account;
	}
	public void setAccount(java.lang.String account) {
		this.account = account;
	}
	public java.lang.String getPlatformcode() {
		return platformcode;
	}
	public void setPlatformcode(java.lang.String platformcode) {
		this.platformcode = platformcode;
	}
	public java.lang.String getGamecode() {
		return gamecode;
	}
	public void setGamecode(java.lang.String gamecode) {
		this.gamecode = gamecode;
	}
	public java.lang.String getGamename() {
		return gamename;
	}
	public void setGamename(java.lang.String gamename) {
		this.gamename = gamename;
	}
	public java.lang.String getGameCategory() {
		return gameCategory;
	}
	public void setGameCategory(java.lang.String gameCategory) {
		this.gameCategory = gameCategory;
	}
	public java.lang.String getBetdate() {
		return betdate;
	}
	public void setBetdate(java.lang.String betdate) {
		this.betdate = betdate;
	}
	public java.lang.String getBetno() {
		return betno;
	}
	public void setBetno(java.lang.String betno) {
		this.betno = betno;
	}
	public java.math.BigDecimal getBetamount() {
		return betamount;
	}
	public void setBetamount(java.math.BigDecimal betamount) {
		this.betamount = betamount;
	}
	public java.math.BigDecimal getProfitamount() {
		return profitamount;
	}
	public void setProfitamount(java.math.BigDecimal profitamount) {
		this.profitamount = profitamount;
	}
	public java.lang.String getCurrency() {
		return currency;
	}
	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}
	public java.math.BigDecimal getTormbrate() {
		return tormbrate;
	}
	public void setTormbrate(java.math.BigDecimal tormbrate) {
		this.tormbrate = tormbrate;
	}
	public java.math.BigDecimal getFinalamount() {
		return finalamount;
	}
	public void setFinalamount(java.math.BigDecimal finalamount) {
		this.finalamount = finalamount;
	}
	public java.lang.String getOrigin() {
		return origin;
	}
	public void setOrigin(java.lang.String origin) {
		this.origin = origin;
	}
	public java.lang.String getGameResult() {
		return gameResult;
	}
	public void setGameResult(java.lang.String gameResult) {
		this.gameResult = gameResult;
	}
	public java.lang.String getResult() {
		return result;
	}
	public void setResult(java.lang.String result) {
		this.result = result;
	}
	public java.math.BigDecimal getValidBetAmount() {
		return validBetAmount;
	}
	public void setValidBetAmount(java.math.BigDecimal validBetAmount) {
		this.validBetAmount = validBetAmount;
	}
	public String getCommission() {
		return commission;
	}
	public void setCommission(String commission) {
		this.commission = commission;
	}
	public java.lang.String getFlag() {
		return flag;
	}
	public void setFlag(java.lang.String flag) {
		this.flag = flag;
	}
	public java.lang.String getPlayType() {
		return playType;
	}
	public void setPlayType(java.lang.String playType) {
		this.playType = playType;
	}
	public java.lang.String getTableCode() {
		return tableCode;
	}
	public void setTableCode(java.lang.String tableCode) {
		this.tableCode = tableCode;
	}
	public java.lang.String getInningsCode() {
		return inningsCode;
	}
	public void setInningsCode(java.lang.String inningsCode) {
		this.inningsCode = inningsCode;
	}
	public java.lang.String getLoginIP() {
		return loginIP;
	}
	public void setLoginIP(java.lang.String loginIP) {
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
	public String getRoundNo() {
		return roundNo;
	}
	public void setRoundNo(String roundNo) {
		this.roundNo = roundNo;
	}
	
}
