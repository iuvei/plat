package com.gameportal.service;

import java.util.List;

import com.gameportal.domain.MenuEntity;


public interface IMenuService {

	public List<MenuEntity> listAllParentMenu();
	
	public List<MenuEntity> listSubMenuByParentId(String parentId);
	
	public List<MenuEntity> listAllMenu();
}
