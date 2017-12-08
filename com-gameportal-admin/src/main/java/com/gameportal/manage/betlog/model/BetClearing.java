package com.gameportal.manage.betlog.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 代理结算对象
 * @author Administrator
 *
 */
public class BetClearing extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1785745977670760330L;
	
	/**
	 * 对应用户ID
	 */
	private Integer uiid;
	
	/**
	 * 用户账号
	 */
	private String account;
	
	/**
	 * 用户姓名
	 */
	private String uname;
	
	/**
	 * 占成数据对于ID
	 */
	private Integer pyid;
	
	/**
	 * 占成比例
	 */
	private String returnscale;
	
	/**
	 * 洗码比例
	 */
	private String ximascale;
	
	/**
	 * 是否可洗码标示
	 * 0：不能洗码
	 * 1：可以洗码
	 */
	private int isximaflag;
	
	/**
	 * 结算类型
	 * 0：输值结算按月
	 * 1：按月洗码
	 * 2：按天洗码
	 */
	private int clearingtype;
	
	/**
	 * 注单数量
	 */
	private String betTotel;
	
	/**
	 * 下级会员数
	 */
	private String lowerUser;
	
	/**
	 * 盈亏
	 */
	private String finalamountTotal;
	
	/**
	 * 有效投注总额
	 */
	private String validBetAmountTotal;
	
	/**
	 * 下线本月洗码额
	 */
	private String ximaAmount;
	
	/**
	 * 本月总优惠
	 */
	private String preferentialTotal;
	
	/**
	 * 结算状态
	 */
	private String clearingstatus;
	
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

	public Integer getPyid() {
		return pyid;
	}

	public void setPyid(Integer pyid) {
		this.pyid = pyid;
	}

	public String getReturnscale() {
		return returnscale;
	}

	public void setReturnscale(String returnscale) {
		this.returnscale = returnscale;
	}

	public String getXimascale() {
		return ximascale;
	}

	public void setXimascale(String ximascale) {
		this.ximascale = ximascale;
	}

	public String getBetTotel() {
		return betTotel;
	}

	public void setBetTotel(String betTotel) {
		this.betTotel = betTotel;
	}

	public String getLowerUser() {
		return lowerUser;
	}

	public void setLowerUser(String lowerUser) {
		this.lowerUser = lowerUser;
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

	public String getClearingstatus() {
		return clearingstatus;
	}

	public String getXimaAmount() {
		return ximaAmount;
	}

	public void setXimaAmount(String ximaAmount) {
		this.ximaAmount = ximaAmount;
	}

	public String getPreferentialTotal() {
		return preferentialTotal;
	}

	public void setPreferentialTotal(String preferentialTotal) {
		this.preferentialTotal = preferentialTotal;
	}

	public int getIsximaflag() {
		return isximaflag;
	}

	public void setIsximaflag(int isximaflag) {
		this.isximaflag = isximaflag;
	}

	public void setClearingstatus(String clearingstatus) {
//		if(StringUtils.isNotBlank(clearingstatus)){//如果查询不等于null的话表示已经结算
//			clearingstatus = "1";
//		}else{
//			clearingstatus = "0";
//		}
		this.clearingstatus = clearingstatus;
	}
	

	public int getClearingtype() {
		return clearingtype;
	}

	public void setClearingtype(int clearingtype) {
		this.clearingtype = clearingtype;
	}

	@Override
	public String toString() {
		return "BetClearing [uiid=" + uiid + ", account=" + account
				+ ", uname=" + uname + ", pyid=" + pyid + ", returnscale="
				+ returnscale + ", ximascale=" + ximascale + ", betTotel="
				+ betTotel + ", lowerUser=" + lowerUser + ", finalamountTotal="
				+ finalamountTotal + ", clearingstatus=" + clearingstatus + "]";
	}



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.getUiid();
	}

}
