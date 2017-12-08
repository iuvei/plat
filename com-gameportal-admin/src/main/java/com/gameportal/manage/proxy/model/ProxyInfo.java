package com.gameportal.manage.proxy.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 代理信息
 * @author Administrator
 *
 */
public class ProxyInfo extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8042682737547129993L;

	/**
	 * 用户ID
	 */
	private java.lang.Long uiid;
	
	/**
	 * 代理账号
	 */
	private java.lang.String account;
	
	/**
	 * 代理登录密码
	 */
	private java.lang.String passwd;
	
	/**
	 * 账号类型
	 */
	private java.lang.Integer accounttype;
	
	/**
	 * 代理姓名
	 */
	private java.lang.String uname;
	
	/**
	 * 取款密码
	 */
	private java.lang.String atmpasswd;
	
	/**
	 * 身份证
	 */
	private java.lang.String identitycard;
	
	/**
	 * 手机号码
	 */
	private java.lang.String phone;
	
	/**
	 * email
	 */
	private java.lang.String email;
	
	
	/**
	 * QQ
	 */
	private java.lang.String qq;
	
	/**
	 * 生日
	 */
	private java.lang.String birthday;
	
	/**
	 * 推广地址
	 */
	private java.lang.String url;
	
	/**
	 * 注册IP
	 */
	private java.lang.String regip;
	
	/**
	 * 会员等级
	 */
	private java.lang.Integer grade;
	
	/**
	 * 上级用户ID
	 */
	private java.lang.Long puiid;
	
	/**
	 * 状态
	 */
	private java.lang.Integer status;
	
	/**
	 * 创建时间
	 */
	private java.util.Date createDate;
	
	/**
	 * 更新时间
	 */
	private java.util.Date updateDate;
	
	/**
	 * 登录次数
	 */
	private java.lang.Integer logincount;
	
	/**
	 * 登录IP
	 */
	private java.lang.String loginip;		//登录IP
	
	//附加信息
	private java.lang.Integer lowerUser;	//下级会员数
	
	/**
	 * 自动增长
	 */
	private java.lang.Integer pyid;
	
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
	
	public ProxyInfo(){
	}

	public ProxyInfo(java.lang.Long uiid){
			this.uiid = uiid;
	}
	
	public java.lang.Long getUiid() {
		return uiid;
	}

	public void setUiid(java.lang.Long uiid) {
		this.uiid = uiid;
	}

	public java.lang.String getAccount() {
		return account;
	}

	public void setAccount(java.lang.String account) {
		this.account = account;
	}

	public java.lang.String getPasswd() {
		return passwd;
	}

	public void setPasswd(java.lang.String passwd) {
		this.passwd = passwd;
	}

	public java.lang.Integer getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(java.lang.Integer accounttype) {
		this.accounttype = accounttype;
	}

	public java.lang.String getUname() {
		return uname;
	}

	public void setUname(java.lang.String uname) {
		this.uname = uname;
	}
	

	public int getClearingtype() {
		return clearingtype;
	}

	public void setClearingtype(int clearingtype) {
		this.clearingtype = clearingtype;
	}

	public java.lang.String getAtmpasswd() {
		return atmpasswd;
	}

	public void setAtmpasswd(java.lang.String atmpasswd) {
		this.atmpasswd = atmpasswd;
	}

	public java.lang.String getIdentitycard() {
		return identitycard;
	}

	public void setIdentitycard(java.lang.String identitycard) {
		this.identitycard = identitycard;
	}

	public java.lang.String getPhone() {
		return phone;
	}

	public void setPhone(java.lang.String phone) {
		this.phone = phone;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.lang.String getQq() {
		return qq;
	}

	public void setQq(java.lang.String qq) {
		this.qq = qq;
	}

	public java.lang.String getBirthday() {
		return birthday;
	}

	public void setBirthday(java.lang.String birthday) {
		this.birthday = birthday;
	}

	public java.lang.String getUrl() {
		
		return url;
	}

	public void setUrl(java.lang.String url) {
		if(!StringUtils.isNotBlank(url)){
			url = "qianbao188.com/register.html?param="+uiid;
		}
		this.url = url;
	}

	public java.lang.String getRegip() {
		return regip;
	}

	public void setRegip(java.lang.String regip) {
		this.regip = regip;
	}

	public java.lang.Integer getGrade() {
		return grade;
	}

	public void setGrade(java.lang.Integer grade) {
		this.grade = grade;
	}

	public java.lang.Long getPuiid() {
		return puiid;
	}

	public void setPuiid(java.lang.Long puiid) {
		this.puiid = puiid;
	}

	public java.lang.Integer getStatus() {
		return status;
	}

	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}

	public java.util.Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}

	public java.util.Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}

	public java.lang.Integer getLogincount() {
		return logincount;
	}

	public void setLogincount(java.lang.Integer logincount) {
		this.logincount = logincount;
	}

	public java.lang.String getLoginip() {
		return loginip;
	}

	public void setLoginip(java.lang.String loginip) {
		this.loginip = loginip;
	}
	
	public java.lang.Integer getLowerUser() {
		return lowerUser;
	}

	public void setLowerUser(java.lang.Integer lowerUser) {
		this.lowerUser = lowerUser;
	}
	

	public java.lang.Integer getPyid() {
		return pyid;
	}

	public void setPyid(java.lang.Integer pyid) {
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

	public int getIsximaflag() {
		return isximaflag;
	}

	public void setIsximaflag(int isximaflag) {
		this.isximaflag = isximaflag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account == null) ? 0 : account.hashCode());
		result = prime * result
				+ ((accounttype == null) ? 0 : accounttype.hashCode());
		result = prime * result
				+ ((atmpasswd == null) ? 0 : atmpasswd.hashCode());
		result = prime * result
				+ ((birthday == null) ? 0 : birthday.hashCode());
		result = prime * result
				+ ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((grade == null) ? 0 : grade.hashCode());
		result = prime * result
				+ ((identitycard == null) ? 0 : identitycard.hashCode());
		result = prime * result
				+ ((logincount == null) ? 0 : logincount.hashCode());
		result = prime * result + ((loginip == null) ? 0 : loginip.hashCode());
		result = prime * result + ((passwd == null) ? 0 : passwd.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((puiid == null) ? 0 : puiid.hashCode());
		result = prime * result + ((qq == null) ? 0 : qq.hashCode());
		result = prime * result + ((regip == null) ? 0 : regip.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((uiid == null) ? 0 : uiid.hashCode());
		result = prime * result + ((uname == null) ? 0 : uname.hashCode());
		result = prime * result
				+ ((updateDate == null) ? 0 : updateDate.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		ProxyInfo other = (ProxyInfo) obj;
		if (account == null) {
			if (other.account != null)
				return false;
		} else if (!account.equals(other.account))
			return false;
		if (accounttype == null) {
			if (other.accounttype != null)
				return false;
		} else if (!accounttype.equals(other.accounttype))
			return false;
		if (atmpasswd == null) {
			if (other.atmpasswd != null)
				return false;
		} else if (!atmpasswd.equals(other.atmpasswd))
			return false;
		if (birthday == null) {
			if (other.birthday != null)
				return false;
		} else if (!birthday.equals(other.birthday))
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (grade == null) {
			if (other.grade != null)
				return false;
		} else if (!grade.equals(other.grade))
			return false;
		if (identitycard == null) {
			if (other.identitycard != null)
				return false;
		} else if (!identitycard.equals(other.identitycard))
			return false;
		if (logincount == null) {
			if (other.logincount != null)
				return false;
		} else if (!logincount.equals(other.logincount))
			return false;
		if (loginip == null) {
			if (other.loginip != null)
				return false;
		} else if (!loginip.equals(other.loginip))
			return false;
		if (passwd == null) {
			if (other.passwd != null)
				return false;
		} else if (!passwd.equals(other.passwd))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (puiid == null) {
			if (other.puiid != null)
				return false;
		} else if (!puiid.equals(other.puiid))
			return false;
		if (qq == null) {
			if (other.qq != null)
				return false;
		} else if (!qq.equals(other.qq))
			return false;
		if (regip == null) {
			if (other.regip != null)
				return false;
		} else if (!regip.equals(other.regip))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (uiid == null) {
			if (other.uiid != null)
				return false;
		} else if (!uiid.equals(other.uiid))
			return false;
		if (uname == null) {
			if (other.uname != null)
				return false;
		} else if (!uname.equals(other.uname))
			return false;
		if (updateDate == null) {
			if (other.updateDate != null)
				return false;
		} else if (!updateDate.equals(other.updateDate))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProxyInfo [uiid=" + uiid + ", account=" + account + ", passwd="
				+ passwd + ", accounttype=" + accounttype + ", uname=" + uname
				+ ", atmpasswd=" + atmpasswd + ", identitycard=" + identitycard
				+ ", phone=" + phone + ", email=" + email + ", qq=" + qq
				+ ", birthday=" + birthday + ", url=" + url + ", regip="
				+ regip + ", grade=" + grade + ", puiid=" + puiid + ", status="
				+ status + ", createDate=" + createDate + ", updateDate="
				+ updateDate + ", logincount=" + logincount + ", loginip="
				+ loginip + "]";
	}

	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.getUiid();
	}

}
