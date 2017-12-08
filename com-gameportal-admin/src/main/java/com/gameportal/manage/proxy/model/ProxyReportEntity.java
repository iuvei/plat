package com.gameportal.manage.proxy.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 代理报表
 * @author Administrator
 *
 */
public class ProxyReportEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -937228189478272213L;

	/**
	 * 代理ID
	 */
	private Long proxyid;
	
	/**
	 * 代理帐号
	 */
	private String proxyaccount;
	
	/**
	 * 代理姓名
	 */
	private String proxyname;
	
	/**
	 * 代理域名
	 */
	private String proxydomain;
	
	/**
	 * 结算类型
	 * 0：输值结算按月
	 * 1：按月洗码
	 * 2：按天洗码
	 */
	private Integer clearingtype = 0;
	
	/**
	 * 占成比例
	 */
	private String returnscale;
	
	/**
	 * 洗吗比例
	 */
	private String ximascale;
	
	/**
	 * 洗码状态
	 */
	private String ximaStatus;
	
	/**
	 * 注单数量
	 */
	private String betTotel;
	
	/**
	 * 注册人数
	 */
	private String lowerUser;
	
	/**
	 * 报表人数
	 * 用于查询时间段的人数
	 */
	private String lowecCount;
	
	/**
	 * 活跃人数
	 */
	private String activeUser;
	
	/**
	 * 首充人数
	 */
	private String fcCount;
	
	/**
	 * 首充金额
	 */
	private String fcAmount;
	
	/**
	 * 首充会员充值总金额
	 */
	private String fcTotalAmount;
	
	/**
	 * 充值人数
	 */
	private String payusercount;
	
	/**
	 * 充值比数
	 */
	private String paycount;
	
	/**
	 * 充值金额
	 */
	private String payAmountTotal;
	
	/**
	 * 提款比数
	 */
	private String atmcount;
	
	/**
	 * 提款人数
	 */
	private String atmusercount;
	
	/**
	 * 提款金额
	 */
	private String atmAmountTotal;
	
	/**
	 * 总投注额
	 */
	private String betAmountTotal;
	
	/**
	 * 有效投注总额
	 */
	private String validBetAmountTotal;
	
	/**
	 * 总输赢
	 */
	private String winlossTotal;
	
	/**
	 * 总优惠
	 */
	private String preferentialTotal;
	
	/**
	 * 总洗码
	 */
	private String ximaAmountTotal;
	
	/**
	 * 实际盈亏=盈亏-（总优惠+总洗码）
	 */
	private String realPLs;
	
	/**
	 * 佣金
	 */
	private String commissionAmount;
	
	/**
	 * 扣款金额
	 */
	private String buckleAmount;
	
	/**
	 * 手动洗码金额
	 */
	private String sdximaAmount;
	
	/**
	 * 代理给下线玩家洗码金额
	 */
	private String proxyXimaAmount;
	
	/**
	 * 系统洗吗金额
	 */
	private String sysXimaAmount;
	
	/**
	 * 累计负盈利
	 * 代理上月是否有负盈利记录
	 */
	private String recordAmount;
	
	public Long getProxyid() {
		return proxyid;
	}


	public void setProxyid(Long proxyid) {
		this.proxyid = proxyid;
	}


	public String getProxyaccount() {
		return proxyaccount;
	}


	public void setProxyaccount(String proxyaccount) {
		this.proxyaccount = proxyaccount;
	}


	public String getProxyname() {
		return proxyname;
	}


	public void setProxyname(String proxyname) {
		this.proxyname = proxyname;
	}


	public String getProxydomain() {
		return proxydomain;
	}


	public void setProxydomain(String proxydomain) {
		if(!StringUtils.isNotBlank(proxydomain)){
			proxydomain = "qianbao188.com/register.html?param="+proxyid;
		}
		this.proxydomain = proxydomain;
	}


	public Integer getClearingtype() {
		return clearingtype;
	}


	public void setClearingtype(Integer clearingtype) {
		this.clearingtype = clearingtype;
	}


	public String getReturnscale() {
		return returnscale;
	}


	public void setReturnscale(String returnscale) {
		if(!StringUtils.isNotBlank(returnscale)){
			returnscale = "0.00";
		}
		this.returnscale = returnscale;
	}


	public String getBetTotel() {
		return betTotel;
	}


	public void setBetTotel(String betTotel) {
		if(!StringUtils.isNotBlank(betTotel)){
			betTotel = "0";
		}
		this.betTotel = betTotel;
	}


	public String getLowerUser() {
		return lowerUser;
	}


	public void setLowerUser(String lowerUser) {
		this.lowerUser = lowerUser;
	}


	public String getPayusercount() {
		return payusercount;
	}


	public void setPayusercount(String payusercount) {
		this.payusercount = payusercount;
	}


	public String getPaycount() {
		return paycount;
	}


	public void setPaycount(String paycount) {
		this.paycount = paycount;
	}


	public String getPayAmountTotal() {
		return payAmountTotal;
	}


	public void setPayAmountTotal(String payAmountTotal) {
		if(!StringUtils.isNotBlank(payAmountTotal)){
			payAmountTotal = "0.00";
		}
		this.payAmountTotal = payAmountTotal;
	}


	public String getAtmcount() {
		return atmcount;
	}


	public void setAtmcount(String atmcount) {
		if(!StringUtils.isNotBlank(atmcount)){
			atmcount = "0.00";
		}
		this.atmcount = atmcount;
	}


	public String getAtmusercount() {
		return atmusercount;
	}


	public void setAtmusercount(String atmusercount) {
		this.atmusercount = atmusercount;
	}


	public String getXimascale() {
		return ximascale;
	}


	public void setXimascale(String ximascale) {
		if(!StringUtils.isNotBlank(ximascale)){
			ximascale = "0.00";
		}
		this.ximascale = ximascale;
	}


	public String getBetAmountTotal() {
		return betAmountTotal;
	}


	public void setBetAmountTotal(String betAmountTotal) {
		if(!StringUtils.isNotBlank(betAmountTotal)){
			betAmountTotal = "0.00";
		}
		this.betAmountTotal = betAmountTotal;
	}


	public String getValidBetAmountTotal() {
		return validBetAmountTotal;
	}


	public void setValidBetAmountTotal(String validBetAmountTotal) {
		if(!StringUtils.isNotBlank(validBetAmountTotal)){
			validBetAmountTotal = "0.00";
		}
		this.validBetAmountTotal = validBetAmountTotal;
	}


	public String getWinlossTotal() {
		return winlossTotal;
	}


	public void setWinlossTotal(String winlossTotal) {
		if(!StringUtils.isNotBlank(winlossTotal)){
			winlossTotal = "0.00";
		}
		this.winlossTotal = winlossTotal;
	}


	public String getPreferentialTotal() {
		return preferentialTotal;
	}


	public void setPreferentialTotal(String preferentialTotal) {
		if(!StringUtils.isNotBlank(preferentialTotal)){
			preferentialTotal = "0.00";
		}
		this.preferentialTotal = preferentialTotal;
	}


	public String getXimaAmountTotal() {
		return ximaAmountTotal;
	}


	public void setXimaAmountTotal(String ximaAmountTotal) {
		if(!StringUtils.isNotBlank(ximaAmountTotal)){
			ximaAmountTotal = "0.00";
		}
		this.ximaAmountTotal = ximaAmountTotal;
	}


	public String getRealPLs() {
		return realPLs;
	}


	public void setRealPLs(String realPLs) {
		if(!StringUtils.isNotBlank(realPLs)){
			realPLs = "0.00";
		}
		this.realPLs = realPLs;
	}


	public String getCommissionAmount() {
		return commissionAmount;
	}


	public void setCommissionAmount(String commissionAmount) {
		if(!StringUtils.isNotBlank(commissionAmount)){
			commissionAmount = "0.00";
		}
		this.commissionAmount = commissionAmount;
	}

	public String getActiveUser() {
		return activeUser;
	}

	public void setActiveUser(String activeUser) {
		this.activeUser = activeUser;
	}
	

	public String getXimaStatus() {
		return ximaStatus;
	}


	public void setXimaStatus(String ximaStatus) {
		this.ximaStatus = ximaStatus;
	}

	public String getLowecCount() {
		return lowecCount;
	}


	public void setLowecCount(String lowecCount) {
		this.lowecCount = lowecCount;
	}
	
	

	public String getAtmAmountTotal() {
		return atmAmountTotal;
	}


	public void setAtmAmountTotal(String atmAmountTotal) {
		if(!StringUtils.isNotBlank(atmAmountTotal)){
			atmAmountTotal = "0.00";
		}
		this.atmAmountTotal = atmAmountTotal;
	}
	
	

	public String getBuckleAmount() {
		return buckleAmount;
	}

	public void setBuckleAmount(String buckleAmount) {
		if(!StringUtils.isNotBlank(buckleAmount)){
			buckleAmount = "0.00";
		}
		this.buckleAmount = buckleAmount;
	}


	public String getSdximaAmount() {
		return sdximaAmount;
	}


	public void setSdximaAmount(String sdximaAmount) {
		if(!StringUtils.isNotBlank(sdximaAmount)){
			sdximaAmount = "0.00";
		}
		this.sdximaAmount = sdximaAmount;
	}


	public String getProxyXimaAmount() {
		return proxyXimaAmount;
	}


	public void setProxyXimaAmount(String proxyXimaAmount) {
		if(!StringUtils.isNotBlank(proxyXimaAmount)){
			proxyXimaAmount = "0.00";
		}
		this.proxyXimaAmount = proxyXimaAmount;
	}

	public String getSysXimaAmount() {
		return sysXimaAmount;
	}


	public void setSysXimaAmount(String sysXimaAmount) {
		if(!StringUtils.isNotBlank(sysXimaAmount)){
			sysXimaAmount = "0.00";
		}
		this.sysXimaAmount = sysXimaAmount;
	}
	
	
	
	public String getRecordAmount() {
		return recordAmount;
	}


	public void setRecordAmount(String recordAmount) {
		if(!StringUtils.isNotBlank(recordAmount)){
			recordAmount = "0.00";
		}
		this.recordAmount = recordAmount;
	}
	
	public String getFcCount() {
		return fcCount;
	}


	public void setFcCount(String fcCount) {
		this.fcCount = fcCount;
	}


	public String getFcAmount() {
		return fcAmount;
	}


	public void setFcAmount(String fcAmount) {
		this.fcAmount = fcAmount;
	}
	
	public String getFcTotalAmount() {
		return fcTotalAmount;
	}

	public void setFcTotalAmount(String fcTotalAmount) {
		this.fcTotalAmount = fcTotalAmount;
	}
	@Override
	public String toString() {
		return "ProxyReportEntity [proxyid=" + proxyid + ", proxyaccount="
				+ proxyaccount + ", proxyname=" + proxyname + ", proxydomain="
				+ proxydomain + ", clearingtype=" + clearingtype
				+ ", returnscale=" + returnscale + ", ximascale=" + ximascale
				+ ", ximaStatus=" + ximaStatus + ", betTotel=" + betTotel
				+ ", lowerUser=" + lowerUser + ", lowecCount=" + lowecCount
				+ ", activeUser=" + activeUser + ", payusercount="
				+ payusercount + ", paycount=" + paycount + ", payAmountTotal="
				+ payAmountTotal + ", atmcount=" + atmcount + ", atmusercount="
				+ atmusercount + ", atmAmountTotal=" + atmAmountTotal
				+ ", betAmountTotal=" + betAmountTotal
				+ ", validBetAmountTotal=" + validBetAmountTotal
				+ ", winlossTotal=" + winlossTotal + ", preferentialTotal="
				+ preferentialTotal + ", ximaAmountTotal=" + ximaAmountTotal
				+ ", realPLs=" + realPLs + ", commissionAmount="
				+ commissionAmount + ", buckleAmount=" + buckleAmount
				+ ", sdximaAmount=" + sdximaAmount + ", proxyXimaAmount="
				+ proxyXimaAmount + ", sysXimaAmount=" + sysXimaAmount + "]";
	}


	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.getProxyid();
	}

}
