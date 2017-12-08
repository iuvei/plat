package com.gameportal.manage.pojo;

import com.gameportal.manage.order.model.CompanyCard;

public class CompanyCardNode extends CompanyCard {
	private Boolean expanded;
	private Boolean leaf;

	public CompanyCardNode() {
		super();
	}

	public CompanyCardNode(CompanyCard cc) {
		this.setLeaf(true);
		this.setExpanded(false);

		this.setCcno(cc.getCcno());
		this.setCcid(cc.getCcid());
		this.setBankname(cc.getBankname());
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
