package com.gameportal.domain;

import java.util.List;

/**
 * 菜单类
 * @author brooke
 *
 */
public class MenuEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4012023073336962506L;
	
	private String menu_id;
	private String menu_name;
	private String menu_url;
	private String parent_id;
	private String menu_order;
	private String menu_icon;
	private String menu_type;
	private String target;
	
	private MenuEntity parentMenu;
	private List<MenuEntity> subMenu;
	
	/**
	 * 用户是否有此权限
	 */
	private boolean hasMenu = true;

	public String getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}

	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}

	public String getMenu_url() {
		return menu_url;
	}

	public void setMenu_url(String menu_url) {
		this.menu_url = menu_url;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getMenu_order() {
		return menu_order;
	}

	public void setMenu_order(String menu_order) {
		this.menu_order = menu_order;
	}

	public String getMenu_icon() {
		return menu_icon;
	}

	public void setMenu_icon(String menu_icon) {
		this.menu_icon = menu_icon;
	}

	public String getMenu_type() {
		return menu_type;
	}

	public void setMenu_type(String menu_type) {
		this.menu_type = menu_type;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public MenuEntity getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(MenuEntity parentMenu) {
		this.parentMenu = parentMenu;
	}

	public List<MenuEntity> getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(List<MenuEntity> subMenu) {
		this.subMenu = subMenu;
	}

	public boolean isHasMenu() {
		return hasMenu;
	}

	public void setHasMenu(boolean hasMenu) {
		this.hasMenu = hasMenu;
	}
	
}
