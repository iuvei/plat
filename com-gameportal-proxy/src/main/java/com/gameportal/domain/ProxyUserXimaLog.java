package com.gameportal.domain;

/**
 * 下线洗码明细
 * @author leron
 *
 */
public class ProxyUserXimaLog extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1167231552877676332L;
	/**
	 * 记录ID
	 */
	private Long logid;
	
	/**
	 * 用户ID
	 */
	private Integer uiid;
	
	/**
	 * 会员账号
	 */
	private String account;
	
	/**
	 * 会员姓名
	 */
	private String uname;
	
	/**
	 * 代理ID
	 */
	private Integer puiid;
	
	/**
	 * 洗码比例
	 */
	private String ximascale;
	
	/**
	 * 洗码金额
	 */
	private String ximamoney;
	
	/**
	 * 优惠金额
	 */
	private String yhmoney;
	
	/**
	 * 有效投注额
	 */
	private String validmoney;
	
	/**
	 * 结算开始日期
	 */
	private String jsstarttime;
	
	/**
	 * 结算结束日期
	 */
	private String jsendtime;
	
	/**
	 * 结算日期
	 */
	private String ximatime;
	
	/**
	 * 0：未入账
	 * 1：已入账
	 * 2：审核不通过
	 */
	private int status;
	
	/**
	 * 代理账号
	 */
	private String proxyaccount;
	
	/**
	 * 代理姓名
	 */
	private String proxyuname;

	public Long getLogid() {
		return logid;
	}

	public void setLogid(Long logid) {
		this.logid = logid;
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

	public Integer getPuiid() {
		return puiid;
	}

	public void setPuiid(Integer puiid) {
		this.puiid = puiid;
	}

	public String getXimascale() {
		return ximascale;
	}

	public void setXimascale(String ximascale) {
		this.ximascale = ximascale;
	}

	public String getXimamoney() {
		return ximamoney;
	}

	public void setXimamoney(String ximamoney) {
		this.ximamoney = ximamoney;
	}

	public String getYhmoney() {
		return yhmoney;
	}

	public void setYhmoney(String yhmoney) {
		this.yhmoney = yhmoney;
	}

	public String getValidmoney() {
		return validmoney;
	}

	public void setValidmoney(String validmoney) {
		this.validmoney = validmoney;
	}

	public String getJsstarttime() {
		return jsstarttime;
	}

	public void setJsstarttime(String jsstarttime) {
		this.jsstarttime = jsstarttime;
	}

	public String getJsendtime() {
		return jsendtime;
	}

	public void setJsendtime(String jsendtime) {
		this.jsendtime = jsendtime;
	}

	public String getXimatime() {
		return ximatime;
	}

	public void setXimatime(String ximatime) {
		this.ximatime = ximatime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getProxyaccount() {
		return proxyaccount;
	}

	public void setProxyaccount(String proxyaccount) {
		this.proxyaccount = proxyaccount;
	}

	public String getProxyuname() {
		return proxyuname;
	}

	public void setProxyuname(String proxyuname) {
		this.proxyuname = proxyuname;
	}
}
