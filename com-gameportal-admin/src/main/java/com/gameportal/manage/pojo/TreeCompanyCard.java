package com.gameportal.manage.pojo;

import java.util.ArrayList;
import java.util.List;

import com.gameportal.manage.order.model.CompanyCard;

/**
 * 公司银行卡
 * 
 * @author wbm
 * @date 2015-05-23 下午9:53:59
 */
public class TreeCompanyCard {

	private List<CompanyCardNode> list;
	private CompanyCardNode root;

	public TreeCompanyCard(List<CompanyCard> list) {
		if (null != list && list.size() > 0) {
			List<CompanyCardNode> nlist = new ArrayList<CompanyCardNode>();
			for (CompanyCard cc : list) {
				nlist.add(new CompanyCardNode(cc));
			}
			this.list = nlist;
		}
		this.root = new CompanyCardNode();
	}

	/**
	 * 组合json
	 * 
	 * @param list
	 * @param node
	 */
	private Tree getNodeJson(List<CompanyCardNode> list, CompanyCardNode node) {
		Tree tree = new Tree();
		tree.setId("_generate_" + node.getCcid());
		tree.setText("[" + node.getBankname() + "]" + node.getCcno());
		tree.setIconCls("");
		tree.setChildren(new ArrayList<Tree>());
		if (list == null) {
			// 防止没有记录时
			return tree;
		}
		tree.setUrl("");
		tree.setLeaf(node.getLeaf());
		tree.setExpanded(node.getExpanded());
		List<Tree> lt = new ArrayList<Tree>();
		for (CompanyCardNode ccn : list) {
			Tree subtree = new Tree();
			subtree.setId("_generate_" + ccn.getCcid());
			subtree.setText("[" + ccn.getBankname() + "]" + ccn.getCcno());
			subtree.setIconCls("");
			subtree.setUrl("");
			subtree.setLeaf(true);
			subtree.setExpanded(false);
			lt.add(subtree);
		}
		tree.setChildren(lt);

		return tree;
	}

	public Tree getTreeJson() {
		// 父菜单的id为0
		this.root.setCcid(0L);
		this.root.setLeaf(false);
		this.root.setExpanded(true);
		return this.getNodeJson(this.list, this.root);
	}
}
