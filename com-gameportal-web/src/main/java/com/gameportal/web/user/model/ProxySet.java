package com.gameportal.web.user.model;

import java.io.Serializable;

/**
 * 代理设置对象
 * @author Administrator
 *
 */
public class ProxySet extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8978068189270002306L;

	/**
	 * 自动增长
	 */
	private int pyid;
	
	/**
	 * 对应用户ID
	 */
	private int uiid;
	
	/**
	 * 用户账号
	 */
	private String account;
	
	/**
	 * 用户姓名
	 */
	private String uname;
	
	/**
	 * 占成比例
	 */
	private String returnscale;
	
	/**
	 * 洗码比例
	 */
	private String ximascale;
	
	/**
	 * 结算类型
	 * 0：输值结算按月
	 * 1：按月洗码
	 * 2：按天洗码
	 */
	private int clearingtype;
	
	/**
	 * 是否可洗码标示
	 * 0：不能洗码
	 * 1：可以洗码
	 */
	private int isximaflag;
	
	/**
	 * 状态
	 * 0:禁用
	 * 1：启用
	 */
	private int status;
	
	/**
	 * 创建时间
	 */
	private String createtime;
	
	/**
	 * 修改时间
	 */
	private String uptime;
	
	/**
	 * 修改人
	 */
	private String upuser;
	
	/**
	 * 修改客户端IP 
	 */
	private String upclientip;
	
	public int getPyid() {
		return pyid;
	}

	public void setPyid(int pyid) {
		this.pyid = pyid;
	}

	public int getUiid() {
		return uiid;
	}

	public void setUiid(int uiid) {
		this.uiid = uiid;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getUptime() {
		return uptime;
	}

	public void setUptime(String uptime) {
		this.uptime = uptime;
	}

	public String getUpuser() {
		return upuser;
	}

	public void setUpuser(String upuser) {
		this.upuser = upuser;
	}

	public String getUpclientip() {
		return upclientip;
	}

	public void setUpclientip(String upclientip) {
		this.upclientip = upclientip;
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
	

	public int getIsximaflag() {
		return isximaflag;
	}

	public void setIsximaflag(int isximaflag) {
		this.isximaflag = isximaflag;
	}

	
	
	public int getClearingtype() {
		return clearingtype;
	}

	public void setClearingtype(int clearingtype) {
		this.clearingtype = clearingtype;
	}

	@Override
	public String toString() {
		return "ProxySet [pyid=" + pyid + ", uiid=" + uiid + ", returnscale="
				+ returnscale + ", ximascale=" + ximascale + ", status="
				+ status + ", createtime=" + createtime + ", uptime=" + uptime
				+ ", upuser=" + upuser + ", upclientip=" + upclientip + "]";
	}



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.getPyid();
	}
}
