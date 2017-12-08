package com.gameportal.manage.betlog.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 注单统计
 * @author Administrator
 *
 */
public class BetLogTotal extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1725830644911573657L;
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
	
	public String getGamecode() {
		return gamecode;
	}
	public void setGamecode(String gamecode) {
		this.gamecode = gamecode;
	}
	
	public String getGamename() {
		return gamename;
	}
	public void setGamename(String gamename) {
		this.gamename = gamename;
	}
	public String getPlatformcode() {
		return platformcode;
	}
	public void setPlatformcode(String platformcode) {
		this.platformcode = platformcode;
	}
	
	public String getBetTotel() {
		return betTotel;
	}
	public void setBetTotel(String betTotel) {
		this.betTotel = betTotel;
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
	public String getValidBetAmountTotal() {
		return validBetAmountTotal;
	}
	public void setValidBetAmountTotal(String validBetAmountTotal) {
		this.validBetAmountTotal = validBetAmountTotal;
	}
	public String getFinalamountTotal() {
		return finalamountTotal;
	}
	public void setFinalamountTotal(String finalamountTotal) {
		this.finalamountTotal = finalamountTotal;
	}
	public String getXimaAmount() {
		return ximaAmount;
	}
	public void setXimaAmount(String ximaAmount) {
		this.ximaAmount = ximaAmount;
	}
	public String getBetdate() {
		return betdate;
	}
	public void setBetdate(String betdate) {
		this.betdate = betdate;
	}
	public String getPreferentialTotal() {
		return preferentialTotal;
	}
	public void setPreferentialTotal(String preferentialTotal) {
		this.preferentialTotal = preferentialTotal;
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
	
	public int getClearingstatus() {
		return clearingstatus;
	}
	public void setClearingstatus(int clearingstatus) {
		this.clearingstatus = clearingstatus;
	}
	
	public String getXimascale() {
		return ximascale;
	}
	public void setXimascale(String ximascale) {
		this.ximascale = ximascale;
	}
	@Override
	public String toString() {
		return "BetLogTotal [account=" + account + ", uname=" + uname
				+ ", platformcode=" + platformcode + ", betTotal=" + betAmountTotal
				 + ", profitamountTotal="
				+ profitamountTotal + ", validBetAmountTotal="
				+ validBetAmountTotal + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account == null) ? 0 : account.hashCode());
		result = prime * result
				+ ((betAmountTotal == null) ? 0 : betAmountTotal.hashCode());
		result = prime * result
				+ ((platformcode == null) ? 0 : platformcode.hashCode());
		result = prime
				* result
				+ ((profitamountTotal == null) ? 0 : profitamountTotal
						.hashCode());
		result = prime * result + ((uname == null) ? 0 : uname.hashCode());
		result = prime
				* result
				+ ((validBetAmountTotal == null) ? 0 : validBetAmountTotal
						.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BetLogTotal other = (BetLogTotal) obj;
		if (account == null) {
			if (other.account != null)
				return false;
		} else if (!account.equals(other.account))
			return false;
		if (betAmountTotal == null) {
			if (other.betAmountTotal != null)
				return false;
		} else if (!betAmountTotal.equals(other.betAmountTotal))
			return false;
		if (platformcode == null) {
			if (other.platformcode != null)
				return false;
		} else if (!platformcode.equals(other.platformcode))
			return false;
		if (profitamountTotal == null) {
			if (other.profitamountTotal != null)
				return false;
		} else if (!profitamountTotal.equals(other.profitamountTotal))
			return false;
		if (uname == null) {
			if (other.uname != null)
				return false;
		} else if (!uname.equals(other.uname))
			return false;
		if (validBetAmountTotal == null) {
			if (other.validBetAmountTotal != null)
				return false;
		} else if (!validBetAmountTotal.equals(other.validBetAmountTotal))
			return false;
		return true;
	}
	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
