package com.gameportal.domain;

/**
 * 注单统计
 * @author leron
 *
 */
public class BetLogTotal extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4520103418602743327L;

	private Integer puiid;					//上级代理
	private Integer uiid;					//对应用户ID
	private String account;					//会员账号
	private String uname;					//会员姓名
	private String phone;					//电话
	private String email;					//Email
	private String qq;						//QQ
	private String birthday;				//生日
	private String betTotel;				//总投注数量
	private String gamecode;				//游戏CODE
	private String platformcode;			//平台
	private String betAmountTotal;			//下注总额
	private String profitamountTotal;		//派彩金额总计
	private String finalamountTotal;		//输赢总额，用于计算代理盈亏
	private String validBetAmountTotal;		//有效投注额总计
	private String betdate;					//投注日期
	private String ximaAmount;				//洗码金额
	private String gamename;				//游戏名称
	private String preferentialTotal;		//总优惠
	private String ximascale;				//洗码比例
	private int clearingstatus;				//结算状态
	public Integer getPuiid() {
		return puiid;
	}
	public void setPuiid(Integer puiid) {
		this.puiid = puiid;
	}
	public Integer getUiid() {
		return uiid;
	}
	public void setUiid(Integer uiid) {
		this.uiid = uiid;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getBetTotel() {
		return betTotel;
	}
	public void setBetTotel(String betTotel) {
		this.betTotel = betTotel;
	}
	public String getGamecode() {
		return gamecode;
	}
	public void setGamecode(String gamecode) {
		this.gamecode = gamecode;
	}
	public String getPlatformcode() {
		return platformcode;
	}
	public void setPlatformcode(String platformcode) {
		this.platformcode = platformcode;
	}
	public String getBetAmountTotal() {
		return betAmountTotal;
	}
	public void setBetAmountTotal(String betAmountTotal) {
		this.betAmountTotal = betAmountTotal;
	}
	public String getProfitamountTotal() {
		return profitamountTotal;
	}
	public void setProfitamountTotal(String profitamountTotal) {
		this.profitamountTotal = profitamountTotal;
	}
	public String getFinalamountTotal() {
		return finalamountTotal;
	}
	public void setFinalamountTotal(String finalamountTotal) {
		this.finalamountTotal = finalamountTotal;
	}
	public String getValidBetAmountTotal() {
		return validBetAmountTotal;
	}
	public void setValidBetAmountTotal(String validBetAmountTotal) {
		this.validBetAmountTotal = validBetAmountTotal;
	}
	public String getBetdate() {
		return betdate;
	}
	public void setBetdate(String betdate) {
		this.betdate = betdate;
	}
	public String getXimaAmount() {
		return ximaAmount;
	}
	public void setXimaAmount(String ximaAmount) {
		this.ximaAmount = ximaAmount;
	}
	public String getGamename() {
		return gamename;
	}
	public void setGamename(String gamename) {
		this.gamename = gamename;
	}
	public String getPreferentialTotal() {
		return preferentialTotal;
	}
	public void setPreferentialTotal(String preferentialTotal) {
		this.preferentialTotal = preferentialTotal;
	}
	public String getXimascale() {
		return ximascale;
	}
	public void setXimascale(String ximascale) {
		this.ximascale = ximascale;
	}
	public int getClearingstatus() {
		return clearingstatus;
	}
	public void setClearingstatus(int clearingstatus) {
		this.clearingstatus = clearingstatus;
	}
	
}
