package com.gameportal.persistence;

import java.util.List;

import com.gameportal.domain.MenuEntity;

/**
 * 菜单查询
 * @author brooke
 *
 */
public interface MenuMapper {

	/**
	 * 查询全部菜单
	 * @return
	 * @throws Exception
	 */
	public List<MenuEntity> listAllParentMenu() throws Exception;
	
	/**
	 * 根据父菜单查询子菜单
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<MenuEntity> listSubMenuByParentId(String parentId) throws Exception;
}
