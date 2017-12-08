package com.gameportal.manage.member.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 会员输赢统计对象
 * @author Administrator
 *
 */
public class MemberInfoReport extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5853422909178515317L;
	
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
	 * 第三方游戏API密码
	 */
	private String apipassword;
	
	/**
	 * 钱包余额
	 */
	private String money;
	
	/**
	 * 总存款
	 */
	private String depositTotal;
	
	/**
	 * 总提款
	 */
	private String withdrawalTotal;
	
	/**
	 * 总优惠
	 */
	private String preferentialTotal;
	
	/**
	 * 总洗码
	 */
	private String ximaTotal;
	
	/**
	 * 总输赢
	 */
	private String winLossTotal;
	
	
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


	public String getMoney() {
		return money;
	}


	public void setMoney(String money) {
		this.money = money;
	}


	public String getDepositTotal() {
		return depositTotal;
	}


	public void setDepositTotal(String depositTotal) {
		this.depositTotal = depositTotal;
	}


	public String getWithdrawalTotal() {
		return withdrawalTotal;
	}


	public void setWithdrawalTotal(String withdrawalTotal) {
		this.withdrawalTotal = withdrawalTotal;
	}


	public String getPreferentialTotal() {
		return preferentialTotal;
	}


	public void setPreferentialTotal(String preferentialTotal) {
		this.preferentialTotal = preferentialTotal;
	}


	public String getXimaTotal() {
		return ximaTotal;
	}


	public void setXimaTotal(String ximaTotal) {
		this.ximaTotal = ximaTotal;
	}


	public String getWinLossTotal() {
		return winLossTotal;
	}


	public void setWinLossTotal(String winLossTotal) {
		this.winLossTotal = winLossTotal;
	}
	
	

	public String getApipassword() {
		return apipassword;
	}


	public void setApipassword(String apipassword) {
		this.apipassword = apipassword;
	}


	@Override
	public String toString() {
		return "MemberInfoReport [uiid=" + uiid + ", account=" + account
				+ ", uname=" + uname + ", money=" + money + ", depositTotal="
				+ depositTotal + ", withdrawalTotal=" + withdrawalTotal
				+ ", preferentialTotal=" + preferentialTotal + ", ximaTotal="
				+ ximaTotal + ", winLossTotal=" + winLossTotal + "]";
	}

	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.getUiid();
	}
    

	
    
    
}
