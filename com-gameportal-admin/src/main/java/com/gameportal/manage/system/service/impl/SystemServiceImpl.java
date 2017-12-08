package com.gameportal.manage.system.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.system.dao.SystemFieldDao;
import com.gameportal.manage.system.dao.SystemModuleDao;
import com.gameportal.manage.system.dao.SystemRoleDao;
import com.gameportal.manage.system.dao.SystemRoleModuleDao;
import com.gameportal.manage.system.dao.SystemUserDao;
import com.gameportal.manage.system.dao.SystemUserRoleDao;
import com.gameportal.manage.system.model.SystemField;
import com.gameportal.manage.system.model.SystemModule;
import com.gameportal.manage.system.model.SystemRole;
import com.gameportal.manage.system.model.SystemRoleModule;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.system.model.SystemUserRole;
import com.gameportal.manage.system.service.ISystemService;
import com.gameportal.manage.util.MD5Util;
import com.gameportal.manage.util.PropertyContext;

@Service
public class SystemServiceImpl implements ISystemService {
	@Resource(name = "systemModuleDao")
	private SystemModuleDao systemModuleDao = null;
	@Resource(name = "systemFieldDao")
	private SystemFieldDao systemFieldDao = null;
	@Resource(name = "systemRoleDao")
	private SystemRoleDao systemRoleDao = null;
	@Resource(name = "systemRoleModuleDao")
	private SystemRoleModuleDao systemRoleModuleDao = null;
	@Resource(name = "systemUserDao")
	private SystemUserDao systemUserDao = null;
	@Resource(name = "systemUserRoleDao")
	private SystemUserRoleDao systemUserRoleDao = null;
	private Logger logger = Logger.getLogger(SystemServiceImpl.class);// 日志对象

	public SystemServiceImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<SystemModule> querySystemModule(Long moduleId, String key,
			Integer startNo, Integer pagaSize) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(ObjectUtils.toString(moduleId))) {
			paramMap.put("moduleId", moduleId);
		}
		if (StringUtils.isNotBlank(key)) {
			paramMap.put("moduleName", key);
		}
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pagaSize))) {
			startNo = 0;
			pagaSize = 30;
		}
		paramMap.put("limit", true);
		paramMap.put("thisPage", startNo);
		paramMap.put("pageSize", pagaSize);
		paramMap.put("sortColumns", "parent_id, display_index, create_date desc");
		List<SystemModule> list = systemModuleDao.getList(paramMap);
		return list;
	}

	@Override
	public List<SystemModule> queryModulesByRoleId(Long roleId) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(ObjectUtils.toString(roleId))) {
			paramMap.put("roleId", roleId);
		}
		List<SystemModule> list = systemModuleDao.queryForPager(
				"SystemModule.selectModulesByRoleId", paramMap, 0, 0);
		return list;
	}

	@Override
	public Long getSystemModuleCount(Long moduleId, String key) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(ObjectUtils.toString(moduleId))) {
			paramMap.put("moduleId", moduleId);
		}
		if (StringUtils.isNotBlank(key)) {
			paramMap.put("moduleName", key);
		}
		Long count = systemModuleDao.getRecordCount(paramMap);
		return count;
	}

	@Override
	public Map<String, String> qeuryAllFields() {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortColumns", "field desc ,sort asc");
		paramMap.put("enabled", "1");
		List<SystemField> list = systemFieldDao.queryForPager(paramMap, 0, 0);

		HashMap<String, LinkedHashMap<String, String>> all = new HashMap<String, LinkedHashMap<String, String>>();
		LinkedHashMap<String, String> part = null;
		for (int i = 0; i < list.size(); i++) {
			SystemField baseFields = list.get(i);
			String key = baseFields.getField();
			if (all.containsKey(key)) {
				// 如果包含这个field，就加入它的值
				part = all.get(key);
				part.put(baseFields.getValueField(),
						baseFields.getDisplayField());
			} else {
				part = new LinkedHashMap<String, String>();
				part.put(baseFields.getValueField(),
						baseFields.getDisplayField());
				// 没有这个fiel，则新加入这个filed
				all.put(key, part);
			}
		}
		part = new LinkedHashMap<String, String>();
		logger.info("开始读取系统默认配置");
		for (Entry<String, LinkedHashMap<String, String>> entry : all
				.entrySet()) {
			String key = entry.getKey();
			HashMap<String, String> value = entry.getValue();
			// 为了eval('(${applicationScope.fields.sex})')这个单引号使用,替换所有的'，为\'
			String val = JSONObject.fromObject(value).toString()
					.replaceAll("\\'", "\\\\'");
			logger.info(val);
			part.put(key, val);
		}
		return part;
	}

	@Override
	public List<Map<String, Object>> querySelectModules() {

		List<Map<String, Object>> map = (List<Map<String, Object>>) systemModuleDao
				.queryForPager("SystemModule.selectModules", null, 0, 0);
		return map;
	}

	@Override
	public SystemModule saveSystemModule(SystemModule systemModule)
			throws Exception {

		systemModule = (SystemModule) systemModuleDao.save(systemModule);
		return StringUtils.isNotBlank(ObjectUtils.toString(systemModule
				.getModuleId())) ? systemModule : null;
	}

	@Override
	public boolean saveOrUpdateSystemModule(SystemModule systemModule)
			throws Exception {

		return systemModuleDao.saveOrUpdate(systemModule);
	}

	@Override
	public boolean deleteModule(Long moduleId) throws Exception {

		return systemModuleDao.delete(moduleId);
	}

	@Override
	public List<SystemRole> querySystemRole(Long rid, String roleName,
			Integer status, Integer startNo, Integer pagaSize) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(ObjectUtils.toString(rid))) {
			paramMap.put("roleId", rid);
		}
		if (StringUtils.isNotBlank(roleName)) {
			paramMap.put("roleName", roleName);
		}
		paramMap.put("status", status);
		paramMap.put("sortColumns", " role_id desc");
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pagaSize))) {
			startNo = 0;
			pagaSize = 30;
		}
		paramMap.put("limit", true);
		paramMap.put("thisPage", startNo);
		paramMap.put("pageSize", pagaSize);
		List<SystemRole> list = systemRoleDao.getList(paramMap);
		return list;
	}
	
	@Override
	public List<SystemRole> queryRoleByUserId(Long userId) {
		return systemRoleDao.queryRoleByUserId(userId);
	}

	@Override
	public Long getSystemRoleCount(Long rid, String roleName, Integer status) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(ObjectUtils.toString(rid))) {
			paramMap.put("roleId", rid);
		}
		if (StringUtils.isNotBlank(roleName)) {
			paramMap.put("roleName", roleName);
		}
		paramMap.put("status", 1);
		paramMap.put("sortColumns", " role_id desc");
		Long count = systemRoleDao.getRecordCount(paramMap);
		return count;
	}

	@Override
	public List<SystemModule> queryModulesAll() {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("status", 1);
		List<SystemModule> list = systemModuleDao.queryForPager(paramMap, 0, 0);
		return list;
	}

	@Override
	public boolean saveOrUpdateSystemRole(SystemRole systemRole)
			throws Exception {

		return systemRoleDao.saveOrUpdate(systemRole);
	}
	@Override
	public boolean saveOrUpdateSystemRoleModule(Long roleId,
			List<Long> moduleIdList) throws Exception {

		// 删除原有角色模块关联记录
		systemRoleModuleDao.deleteByRoleId(roleId);
		// 新增现有角色模块关联记录
		if (null != moduleIdList && moduleIdList.size() > 0) {
			SystemRoleModule systemRoleModule = null;
			for (Long moduleId : moduleIdList) {
				systemRoleModule = new SystemRoleModule();
				systemRoleModule.setRoleId(roleId);
				systemRoleModule.setModuleId(moduleId);
				systemRoleModuleDao.save(systemRoleModule);
			}
			return true;
		}
		return false;
	}

	@Override
	public List<SystemUser> querySystemUser(Long userId, String key,
			Integer status, Integer startNo, Integer pagaSize) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(ObjectUtils.toString(userId))) {
			paramMap.put("userId", userId);
		}
		if (StringUtils.isNotBlank(key)) {
			paramMap.put("realName", key);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
			paramMap.put("status", status);
		}
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pagaSize))) {
			startNo = 0;
			pagaSize = 30;
		}
		paramMap.put("limit", true);
		paramMap.put("thisPage", startNo);
		paramMap.put("pageSize", pagaSize);
		List<SystemUser> list = systemUserDao.getList(paramMap);
		return list;
	}

	@Override
	public Long getSystemUserCount(Long userId, String key, Integer status) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(ObjectUtils.toString(userId))) {
			paramMap.put("userId", userId);
		}
		if (StringUtils.isNotBlank(key)) {
			paramMap.put("realName", key);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
			paramMap.put("status", status);
		}
		Long count = systemUserDao.getRecordCount(paramMap);
		return count;
	}

	@Override
	public boolean saveOrUpdateSystemUser(SystemUser systemUser)
			throws Exception {
		
		return systemUserDao.saveOrUpdate(systemUser);
	}
	@Override
	public boolean saveOrUpdateSystemUserAndRoleIds(SystemUser systemUser,String roleIds)
			throws Exception {
		// 保存或更新系统用户
		if (null == systemUser.getUserId()) {
			systemUser = (SystemUser) systemUserDao.save(systemUser);
		} else {
			if (false == systemUserDao.update(systemUser)) {
				throw new Exception("更新系统用户信息出错！");
			}
		}
		// 删除系统用户原有角色
		systemUserRoleDao.deleteByUserId(systemUser.getUserId());
		// 添加系统用户现有角色
		if(StringUtils.isNotBlank(roleIds)){
			String[] ridArr = roleIds.split(",");
			SystemUserRole sur = null;
			for (String rid : ridArr) {
				sur = new SystemUserRole();
				sur.setUserId(systemUser.getUserId());
				sur.setRoleId(new Long(rid));
				systemUserRoleDao.save(sur);
			}
		}
		return true;
	}

	@Override
	public boolean dellSystemUser(Long userId) throws Exception {

		return systemUserDao.delete(userId);
	}


	@Override
	public boolean resetSysUserPwd(Long userId,String pwd) {
		SystemUser systemUser = (SystemUser) systemUserDao.findById(userId);
		if (null != systemUser) {
			systemUser.setPassword(MD5Util.getMD5Encode(pwd));
			systemUser.setUpdateDate(new Date());
			systemUserDao.update(systemUser);
			return true;
		}
		return false;
	}
	
	@Override
	public List<SystemField> querySystemField(Long fieldId, String key,
			Integer status, Integer startNo, Integer pagaSize) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(ObjectUtils.toString(fieldId))) {
			paramMap.put("fieldId", fieldId);
		}
		if (StringUtils.isNotBlank(key)) {
			paramMap.put("fieldName", key);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
			paramMap.put("enabled", status);
		}
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pagaSize))) {
			startNo = 0;
			pagaSize = 30;
		}
		paramMap.put("limit", true);
		paramMap.put("thisPage", startNo);
		paramMap.put("pageSize", pagaSize);
		List list = systemFieldDao.getList(paramMap);
		return list;
	}

	@Override
	public Long getSystemFieldCount(Long fieldId, String key, Integer status) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(ObjectUtils.toString(fieldId))) {
			paramMap.put("fieldId", fieldId);
		}
		if (StringUtils.isNotBlank(key)) {
			paramMap.put("fieldName", key);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
			paramMap.put("enabled", status);
		}
		paramMap.put("sortColumns", "field desc ,sort asc");

		Long count = systemFieldDao.getRecordCount(paramMap);
		return count;
	}

	@Override
	public boolean saveOrUpdateSystemField(SystemField systemField)
			throws Exception {

		return systemFieldDao.saveOrUpdate(systemField);
	}

	@Override
	public boolean dellSystemField(Long fieldId) throws Exception {

		return systemFieldDao.delete(fieldId);
	}

}
