package com.gameportal.domain;


/**
 * 游戏平台
 * @author leron
 *
 */
public class GamePlatform extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8389280788696944163L;
	
	//alias
		public static final String TABLE_ALIAS = "GamePlatform";
		public static final String ALIAS_GPID = "游戏平台ID";
		public static final String ALIAS_GPNAME = "游戏平台名称";
		public static final String ALIAS_DOMAINNAME = "游戏平台域名";
		public static final String ALIAS_DOMAINIP = "游戏平台IP";
		public static final String ALIAS_DESKEY = "游戏平台KEY";
		public static final String ALIAS_APIACCOUNT = "API帐号";
		public static final String ALIAS_CIPHERCODE = "游戏平台密码";
		public static final String ALIAS_STATUS = "状态 0 关闭 1开启";
		public static final String ALIAS_CREATE_DATE = "创建时间";
		public static final String ALIAS_UPDATE_DATE = "更新时间";
		
		
		//columns START
		private java.lang.Long gpid;
		private java.lang.String gpname;
		private java.lang.String domainname;
		private java.lang.String domainip;
		private java.lang.String deskey;
		private java.lang.String apiaccount;
		private java.lang.String ciphercode;
		private java.lang.Integer status;
		private java.util.Date createDate;
		private java.util.Date updateDate;
		//columns END
		public java.lang.Long getGpid() {
			return gpid;
		}
		public void setGpid(java.lang.Long gpid) {
			this.gpid = gpid;
		}
		public java.lang.String getGpname() {
			return gpname;
		}
		public void setGpname(java.lang.String gpname) {
			this.gpname = gpname;
		}
		public java.lang.String getDomainname() {
			return domainname;
		}
		public void setDomainname(java.lang.String domainname) {
			this.domainname = domainname;
		}
		public java.lang.String getDomainip() {
			return domainip;
		}
		public void setDomainip(java.lang.String domainip) {
			this.domainip = domainip;
		}
		public java.lang.String getDeskey() {
			return deskey;
		}
		public void setDeskey(java.lang.String deskey) {
			this.deskey = deskey;
		}
		public java.lang.String getApiaccount() {
			return apiaccount;
		}
		public void setApiaccount(java.lang.String apiaccount) {
			this.apiaccount = apiaccount;
		}
		public java.lang.String getCiphercode() {
			return ciphercode;
		}
		public void setCiphercode(java.lang.String ciphercode) {
			this.ciphercode = ciphercode;
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
		
}
