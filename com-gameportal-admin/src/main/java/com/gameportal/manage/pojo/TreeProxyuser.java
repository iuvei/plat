package com.gameportal.manage.pojo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gameportal.manage.user.model.UserInfo;

/**
 * 会员代理
 * 
 * @author wbm
 * @date 2015-05-10 上午10:36:23
 */
public class TreeProxyuser {

	private List<UserInfoNode> list;
	private UserInfoNode root;
	private UserInfoNode leaf;

	public TreeProxyuser(List<UserInfo> list) { // 多个根节点
		this.list = this.convert(list);
	}
	
	public TreeProxyuser(List<UserInfo> list, UserInfo userInfo) { // 唯一根节点
		this.list = this.convert(list);
		this.root = new UserInfoNode(userInfo);
		this.leaf = new UserInfoNode(userInfo);
	}

	private List<UserInfoNode> convert(List<UserInfo> list) {
		List<UserInfoNode> nodeList = new ArrayList<UserInfoNode>();
		if (null != list) {
			for (UserInfo u : list) {
				nodeList.add(new UserInfoNode(u));
			}
		}
		return nodeList;
	}

	/**
	 * 组合json(获取子节点)
	 * 
	 * @param list
	 * @param node
	 */
	private Tree getChildNodeJson(List<UserInfoNode> list, UserInfoNode node) {
		Tree tree = new Tree();
		tree.setId("_generate_" + node.getUiid());
		tree.setText(node.getAccount());
		// tree.setIconCls("1");
		tree.setChildren(new ArrayList<Tree>());
		if (list == null) {
			// 防止没有节点
			return tree;
		}
		tree.setUrl("");
		tree.setLeaf(true);
		tree.setExpanded(false);

		if (this.hasChild(list, node)) {
			List<Tree> lt = new ArrayList<Tree>();
			List<UserInfoNode> childList = this.getChildList(list, node);
			Iterator<UserInfoNode> it = childList.iterator();
			while (it.hasNext()) {
				UserInfoNode u = (UserInfoNode) it.next();
				// 递归
				lt.add(getChildNodeJson(list, u));
			}
			tree.setChildren(lt);
			tree.setLeaf(false);
			tree.setExpanded(true);
		}
		return tree;
	}

	/**
	 * 判断是否有子节点
	 */
	private boolean hasChild(List<UserInfoNode> list, UserInfoNode node) {
		return this.getChildList(list, node).size() > 0 ? true : false;
	}

	/**
	 * 得到子节点列表
	 */
	private List<UserInfoNode> getChildList(List<UserInfoNode> list,
			UserInfoNode userInfo) {
		List<UserInfoNode> li = new ArrayList<UserInfoNode>();
		Iterator<UserInfoNode> it = list.iterator();
		while (it.hasNext()) {
			UserInfoNode temp = (UserInfoNode) it.next();
			if (null!=temp.getPuiid() && temp.getPuiid().longValue() == userInfo.getUiid().longValue()) {
				li.add(temp);
			}
		}
		return li;
	}

	/**
	 * 组合json(获取父节点)
	 * 
	 * @param list
	 * @param node
	 * @param tree
	 * @return
	 */
	private Tree getParentNodeJson(List<UserInfoNode> list, UserInfoNode node,
			Tree tree) {
		if (null == tree) {
			tree = new Tree();
			tree.setId("_generate_" + node.getUiid());
			tree.setText(node.getAccount());
			// tree.setIconCls("1");
			tree.setUrl("");
			tree.setLeaf(node.getLeaf());
			tree.setExpanded(node.getExpanded());
			tree.setChildren(new ArrayList<Tree>());
		}
		if (list == null) {
			// 防止没有节点
			return tree;
		}
		if (null == node.getPuiid()) {
			return tree;
		}
		UserInfoNode parent = getParent(list, node); // 获取父节点
		if (null != parent) {
			parent.setLeaf(false);
			parent.setExpanded(true);

			Tree parentTree = new Tree();
			parentTree.setId("_generate_" + parent.getUiid());
			parentTree.setText(parent.getAccount());
			// tree.setIconCls("1");
			parentTree.setUrl("");
			parentTree.setLeaf(parent.getLeaf());
			parentTree.setExpanded(parent.getExpanded());
			List<Tree> treeList = new ArrayList<Tree>();
			treeList.add(tree);
			parentTree.setChildren(treeList);

			// 递归
			return this.getParentNodeJson(list, parent, parentTree);
		} else {
			return tree;
		}
	}

	/**
	 * 得到父节点
	 */
	private UserInfoNode getParent(List<UserInfoNode> list, UserInfoNode node) {
		Iterator<UserInfoNode> it = list.iterator();
		while (it.hasNext()) {
			UserInfoNode temp = (UserInfoNode) it.next();
			if (temp.getUiid().longValue() == node.getPuiid().longValue()) {
				return temp;
			}
		}
		return null;
	}

	/**
	 * 获取根节点列表
	 * @param list
	 * @return
	 */
	private List<UserInfoNode> getRootNodes(List<UserInfoNode> list){
		List<UserInfoNode> rootList = new ArrayList<UserInfoNode>();
		for (UserInfoNode n : list) {
			if(null==n){
				continue;
			}
			if (null == n.getPuiid() || n.getPuiid() <= 0) {
				rootList.add(n);
				continue;
			}
		}
		return rootList;
	}
	
	public Tree getAllChildTreeJson() {
		ArrayList<Tree> children = new ArrayList<Tree>();
		// 多个根节点
		for (UserInfoNode n : this.getRootNodes(this.list)) {
			children.add(this.getChildNodeJson(this.list, n));
		}
		Tree tree = new Tree();
		tree.setId("-1");
		tree.setText("平台");
		tree.setUrl("");
		tree.setLeaf(false);
		tree.setExpanded(true);
		tree.setChildren(children);
		return tree;
	}
	
	public Tree getChildTreeJson() {
		// 父节点
		this.root.setLeaf(false);
		this.root.setExpanded(true);

		return this.getChildNodeJson(this.list, this.root);
	}

	public Tree getParentTreeJson() {
		// 子节点
		this.leaf.setLeaf(true);
		this.leaf.setExpanded(false);

		return this.getParentNodeJson(this.list, this.leaf, null);
	}
}
