package com.gameportal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gameportal.domain.MenuEntity;
import com.gameportal.persistence.MenuMapper;
import com.gameportal.service.BaseService;
import com.gameportal.service.IMenuService;


/**
 * 菜单业务逻辑层
 * @author brooke
 *
 */
@Service("menuService")
public class MenuServiceImpl extends BaseService implements IMenuService{

	@Autowired
	private MenuMapper menuMapper;
	
	@Override
	public List<MenuEntity> listAllParentMenu() {
		try {
			return menuMapper.listAllParentMenu();
		} catch (Exception e) {
			logger.error("查询所有菜单错误：", e);
		}
		return null;
	}

	@Override
	public List<MenuEntity> listSubMenuByParentId(String parentId) {
		try {
			return menuMapper.listSubMenuByParentId(parentId);
		} catch (Exception e) {
			logger.error("查询"+parentId+"子菜单错误：", e);
		}
		return null;
	}

	@Override
	public List<MenuEntity> listAllMenu() {
		List<MenuEntity> rl = this.listAllParentMenu();
		System.out.println("-->"+rl);
		for(MenuEntity menu : rl){
			List<MenuEntity> subList = this.listSubMenuByParentId(menu.getMenu_id());
			menu.setSubMenu(subList);
		}
		return rl;
	}

}
