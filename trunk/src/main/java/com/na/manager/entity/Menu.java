package com.na.manager.entity;

import com.na.manager.common.annotation.I18NField;

import java.io.Serializable;
/**
 * 菜单列表
 * @author andy
 * @date 2017年6月22日 上午11:45:46
 * 
 */
public class Menu implements Comparable<Menu>,Serializable{
	private Long id;
	@I18NField
	private String menuName;

	private String menuDesc;
	
	private String menuCls;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuDesc() {
		return menuDesc;
	}

	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
	}

	public String getMenuCls() {
		return menuCls;
	}

	public void setMenuCls(String menuCls) {
		this.menuCls = menuCls;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Menu)) return false;

		Menu menu = (Menu) o;

		return getId() .equals(menu.getId());
	}

	@Override
	public int hashCode() {
		return (int) (getId() ^ (getId() >>> 32));
	}

	@Override
	public int compareTo(Menu o) {
		return this.getId().compareTo(o.getId());
	}
}
