package com.gameportal.manage.pojo;

import com.gameportal.manage.user.model.UserInfo;

public class UserInfoNode extends UserInfo {
	private Boolean expanded;
	private Boolean leaf;

	public UserInfoNode() {
		super();
	}

	public UserInfoNode(UserInfo userInfo) {
		this.setLeaf(true);
		this.setExpanded(false);

		this.setUiid(userInfo.getUiid());
		this.setAccount(userInfo.getAccount());
		this.setPasswd(userInfo.getPasswd());
		this.setAccounttype(userInfo.getAccounttype());
		this.setUname(userInfo.getUname());
		this.setAtmpasswd(userInfo.getAtmpasswd());
		this.setIdentitycard(userInfo.getIdentitycard());
		this.setPhone(userInfo.getPhone());
		this.setEmail(userInfo.getEmail());
		this.setQq(userInfo.getQq());
		this.setBirthday(userInfo.getBirthday());
		this.setGrade(userInfo.getGrade());
		this.setPuiid(userInfo.getPuiid());
		this.setStatus(userInfo.getStatus());
		this.setCreateDate(userInfo.getCreateDate());
		this.setUpdateDate(userInfo.getUpdateDate());
	}

	public Boolean getExpanded() {
		return expanded;
	}

	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}

	public Boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

}
