package com.gameportal.web.user.model;

import java.io.Serializable;

/**
 * 用户等级
 * @author Administrator
 *
 */
public class UserGrade extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -835531161934503519L;
	
	private Long gid;
	private int grade;
	private String betamont;
	private int withdrawalcount;
	private String withdrawalquota;

	public Long getGid() {
		return gid;
	}

	public void setGid(Long gid) {
		this.gid = gid;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getBetamont() {
		return betamont;
	}

	public void setBetamont(String betamont) {
		this.betamont = betamont;
	}

	public int getWithdrawalcount() {
		return withdrawalcount;
	}

	public void setWithdrawalcount(int withdrawalcount) {
		this.withdrawalcount = withdrawalcount;
	}

	public String getWithdrawalquota() {
		return withdrawalquota;
	}

	public void setWithdrawalquota(String withdrawalquota) {
		this.withdrawalquota = withdrawalquota;
	}

	@Override
	public String toString() {
		return "UserGrade [gid=" + gid + ", grade=" + grade + ", betamont="
				+ betamont + ", withdrawalcount=" + withdrawalcount
				+ ", withdrawalquota=" + withdrawalquota + "]";
	}

	@Override
	public Serializable getID() {
		return this.getGid();
	}

}
