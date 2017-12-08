package com.gameportal.manage.system.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.system.model.SystemField;
import com.gameportal.manage.system.model.SystemModule;
import com.gameportal.manage.system.model.SystemRole;
import com.gameportal.manage.system.model.SystemUser;

/**
 * @ClassName: ISystemService
 * @Description: TODO(系统服务接口)
 * @author shejia@gz-mstc.com
 * @date 2014-4-10 下午4:39:05
 */
public abstract interface ISystemService {
	/**
	 * @Title: querySystemModule
	 * @Description: TODO(按条件查询模块信息)
	 * @param moduleId
	 *            模块ID
	 * @param key
	 *            模块名
	 * @param startNo
	 *            开始页
	 * @param pagaSize
	 *            页大小
	 * @return List<SystemModule> 返回类型
	 * @throws
	 */
	public abstract List<SystemModule> querySystemModule(Long moduleId,
			String key, Integer startNo, Integer pagaSize);

	/**
	 * @Title: queryModulesByRoleId
	 * @Description: TODO(按角色查询系统模块信息)
	 * @param roleId
	 *            角色ID
	 * @return List<SystemModule> 返回类型 模块信息列表
	 * @throws
	 */
	public abstract List<SystemModule> queryModulesByRoleId(Long roleId);

	/**
	 * @Title: queryModulesAll
	 * @Description: TODO(查询全部模块信息)
	 * @return List<SystemModule> 返回类型 模块信息列表
	 * @throws
	 */
	public abstract List<SystemModule> queryModulesAll();

	/**
	 * @Title: getSystemModuleCount
	 * @Description: TODO(按条件查询模块信息统计)
	 * @param moduleId
	 *            模块ID
	 * @param key
	 *            模块名
	 * @return Long 返回类型 条数
	 * @throws
	 */
	public abstract Long getSystemModuleCount(Long moduleId, String key);

	/**
	 * @Title: qeuryAllFields
	 * @Description: TODO(查询基础数据)
	 * @return Map<String,String> 返回类型
	 * @throws
	 */
	public abstract Map<String, String> qeuryAllFields();

	/**
	 * @Title: querySelectModules
	 * @Description: TODO(查询父模块)
	 * @return Map 返回类型
	 * @throws
	 */
	public abstract List<Map<String, Object>> querySelectModules();

	/**
	 * @Title: saveSystemModule
	 * @Description: TODO(保存系统模块信息)
	 * @param systemModule
	 *            模块对象
	 * @throws Exception
	 * @return SystemModule 返回类型 true 保存成功 false 保存失败
	 */
	public SystemModule saveSystemModule(SystemModule systemModule)
			throws Exception;

	/**
	 * @Title: saveOrUpdateSystemModule
	 * @Description: TODO(保存系统模块信息或修改模块信息)
	 * @param systemModule
	 *            模块对象
	 * @throws Exception
	 * @return boolean 返回类型 true 保存或修改成功 false 保存或修改失败
	 */
	public boolean saveOrUpdateSystemModule(SystemModule systemModule)
			throws Exception;

	/**
	 * @Title: deleteModule
	 * @Description: TODO(按模块ID删除系统模块信息)
	 * @param moduleId
	 *            模块ID
	 * @throws Exception
	 * @return boolean 返回类型 true 删除成功 false 删除失败
	 */
	public boolean deleteModule(Long moduleId) throws Exception;

	/**
	 * @Title: querySystemRole
	 * @Description: TODO(按角色ID或角色名或角色状态查询角色信息)
	 * @param rid
	 *            角色ID
	 * @param roleName
	 *            角色名
	 * @param status
	 *            状态 1 正常
	 * @param startNo
	 *            开始页
	 * @param pagaSize
	 *            页大小
	 * @return List<SystemRole> 返回类型 角色信息列表
	 */
	public List<SystemRole> querySystemRole(Long rid, String roleName,
			Integer status, Integer startNo, Integer pagaSize);
	/**
	 * @Title: queryRoleByUserId
	 * @Description: TODO(按用户ID查询角色信息)
	 * @param userId
	 *            用户ID
	 * @return List<SystemUserRole> 返回类型 角色信息列表
	 */
	public List<SystemRole> queryRoleByUserId(Long userId);
	/**
	 * @Title: getSystemRoleCount
	 * @Description: TODO(按角色ID或角色名或角色状态查询角色信息总条数)
	 * @param rid
	 *            角色ID
	 * @param roleName
	 *            角色名
	 * @param status
	 *            状态 1 正常
	 * @return Long 返回类型 条数
	 */
	public Long getSystemRoleCount(Long rid, String roleName, Integer status);

	/**
	 * @Title: saveOrUpdateSystemRole
	 * @Description: TODO(保存角色或更新角色)
	 * @param systemRole
	 *            角色对象
	 * @throws Exception
	 * @return boolean 返回类型 true 保存成功 false 保存失败
	 */
	public boolean saveOrUpdateSystemRole(SystemRole systemRole)
			throws Exception;
	/**
	 * @Title: saveOrUpdateSystemRoleModule
	 * @Description: TODO(保存角色或更新角色的权限)
	 * @param roleId
	 *            角色id
	 * @param moduleIdList
	 *            模块id集合
	 * @throws Exception
	 * @return boolean 返回类型 true 保存成功 false 保存失败
	 */
	public boolean saveOrUpdateSystemRoleModule(Long roleId, List<Long> moduleIdList)
			throws Exception;

	/**
	 * @Title: querySystemUser
	 * @Description: TODO(查询用户信息按用户ID、用户名、状态)
	 * @param userId
	 *            用户ID
	 * @param key
	 *            用户名
	 * @param status
	 *            状态 1 正常
	 * @param startNo
	 *            开始页
	 * @param pagaSize
	 *            页大小
	 * @return List<SystemUser> 返回类型 用户信息列表
	 */
	public List<SystemUser> querySystemUser(Long userId, String key,
			Integer status, Integer startNo, Integer pagaSize);

	/**
	 * @Title: querySystemUserCount
	 * @Description: TODO(查询用户信息按用户ID、用户名、状态记录数)
	 * @param userId
	 *            用户ID
	 * @param key
	 *            用户名
	 * @param status
	 *            状态 1 正常
	 * @return Long 返回类型 条数
	 */
	public Long getSystemUserCount(Long userId, String key, Integer status);

	/**
	 * @Title: saveOrUpdateSystemUser
	 * @Description: TODO(添加、修改用户信息)
	 * @param systemUser
	 *            用户对象
	 * @throws Exception
	 * @return boolean 返回类型 true 保存成功 false 保存失败
	 */
	public boolean saveOrUpdateSystemUser(SystemUser systemUser)
			throws Exception;
	/**
	 * @Title: saveOrUpdateSystemUser
	 * @Description: TODO(添加、修改用户信息)
	 * @param systemUser
	 *            用户对象
	 * @param roleIds
	 *            角色id
	 * @throws Exception
	 * @return boolean 返回类型 true 保存成功 false 保存失败
	 */
	public boolean saveOrUpdateSystemUserAndRoleIds(SystemUser systemUser,String roleIds)
			throws Exception;

	/**
	 * @Title: dellSystemUser
	 * @Description: TODO(按用户ID删除用户信息)
	 * @param userId
	 *            用户ID
	 * @throws Exception
	 * @return boolean 返回类型 true 删除成功 false 删除失败
	 */
	public boolean dellSystemUser(Long userId) throws Exception;

	/**
	 * @Title: resetSysUserPwd
	 * @Description: TODO(重置系统用户密码)
	 * @param userId
	 * @throws Exception
	 * @return boolean 返回类型 true 重置成功 false 重置失败
	 */
	public boolean resetSysUserPwd(Long userId,String pwd);
	
	/**
	 * @Title: querySystemField
	 * @Description: TODO(查询系统基础数据信息)
	 * @param fieldId
	 * @param key
	 * @param startNo
	 * @param pagaSize
	 * @return List<SystemField> 返回类型
	 */
	public List<SystemField> querySystemField(Long fieldId, String key,
			Integer status, Integer startNo, Integer pagaSize);

	/**
	 * @Title: querySystemFieldCount
	 * @Description: TODO(查询系统基础数据信息记录数)
	 * @param fieldId
	 * @param key
	 * @return List<SystemField> 返回类型
	 */
	public Long getSystemFieldCount(Long fieldId, String key, Integer status);

	/**
	 * @Title: saveOrUpdateSystemField
	 * @Description: TODO(添加系统基础数据信息或修改)
	 * @param systemField
	 * @throws Exception
	 * @return boolean 返回类型 true 保存成功 false 保存失败
	 */
	public boolean saveOrUpdateSystemField(SystemField systemField)
			throws Exception;

	/**
	 * @Title: dellSystemField
	 * @Description: TODO(删除系统基础信息)
	 * @param fieldId
	 * @throws Exception
	 * @return boolean 返回类型 true 删除成功 false 删除失败
	 */
	public boolean dellSystemField(Long fieldId) throws Exception;
	
}
