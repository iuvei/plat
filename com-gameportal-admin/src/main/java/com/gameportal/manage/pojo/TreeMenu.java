package com.gameportal.manage.pojo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gameportal.manage.system.model.SystemModule;

/**
 * 菜单类1.1
 * 
 * @author chenxin
 * @date 2011-10-25 下午9:53:59
 */
public class TreeMenu {

	private List<SystemModule> list;
	private SystemModule root;

	public TreeMenu(List<SystemModule> list) {
		this.list = list;
		this.root = new SystemModule();
	}

	/**
	 * 组合json
	 * 
	 * @param list
	 * @param node
	 */
	private Tree getNodeJson(List<SystemModule> list, SystemModule node) {
		Tree tree = new Tree();
		tree.setId("_generate_" + node.getModuleId());
		tree.setText(node.getModuleName());
		tree.setIconCls(node.getIconCss());
		tree.setChildren(new ArrayList<Tree>());
		if (list == null) {
			// 防止没有权限菜单时
			return tree;
		}
		if (hasChild(list, node)) {
			List<Tree> lt = new ArrayList<Tree>();
			tree.setUrl("");
			tree.setLeaf(node.getLeaf() == 1 ? true : false);
			tree.setExpanded(node.getExpanded() == 1 ? true : false);
			List<SystemModule> childList = getChildList(list, node);
			Iterator<SystemModule> it = childList.iterator();
			while (it.hasNext()) {
				SystemModule modules = (SystemModule) it.next();
				// 递归
				lt.add(getNodeJson(list, modules));
			}
			tree.setChildren(lt);
			// } else if ((node.getParentId() == root.getModuleId()) ||
			// node.getModuleUrl() == null) {
			// // 防止是主菜单,或者主菜单里面的url为空，但是下面没有子菜单的时候
			// tree.setUrl("");
			// tree.setLeaf(node.getLeaf() == 1 ? true : false);
			// tree.setExpanded(node.getExpanded() == 1 ? true : false);
		} else {
			tree.setUrl(node.getModuleUrl());
			tree.setLeaf(node.getLeaf() == 1 ? true : false);
			tree.setExpanded(node.getExpanded() == 1 ? true : false);
		}

		return tree;
	}

	/**
	 * 判断是否有子节点
	 */
	private boolean hasChild(List<SystemModule> list, SystemModule node) {
		return getChildList(list, node).size() > 0 ? true : false;
	}

	/**
	 * 得到子节点列表
	 */
	private List<SystemModule> getChildList(List<SystemModule> list,
			SystemModule modules) {
		List<SystemModule> li = new ArrayList<SystemModule>();
		Iterator<SystemModule> it = list.iterator();
		while (it.hasNext()) {
			SystemModule temp = (SystemModule) it.next();
			if (temp.getParentId().intValue() == modules.getModuleId()
					.intValue()) {
				li.add(temp);
			}
		}
		return li;
	}

	public Tree getTreeJson() {
		// 父菜单的id为0
		this.root.setModuleId(0L);
		this.root.setLeaf(0);
		this.root.setExpanded(0);
		return this.getNodeJson(this.list, this.root);
	}
}
