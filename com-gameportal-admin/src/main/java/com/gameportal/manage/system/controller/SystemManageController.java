package com.gameportal.manage.system.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.listener.SystemFieldsCache;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.pojo.TreeMenu;
import com.gameportal.manage.system.model.SystemField;
import com.gameportal.manage.system.model.SystemModule;
import com.gameportal.manage.system.model.SystemRole;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.system.model.SystemUserLoginLog;
import com.gameportal.manage.system.service.ISystemService;
import com.gameportal.manage.system.service.ISystemUserLoginLogService;
import com.gameportal.manage.util.DateConvertEditor;
import com.gameportal.manage.util.DateUtil;

/**
 * @ClassName: SystemManageController
 * @Description: TODO(系统控制类)
 * @author shejia@gz-mstc.com
 * @date 2014-4-10 下午2:51:41
 */
@Controller
@RequestMapping(value = "/manage/system")
public class SystemManageController {
	@Resource(name = "systemServiceImpl")
	private ISystemService iSystemService = null;
	@Resource(name = "systemUserLoginLogService")
	private ISystemUserLoginLogService systemUserLoginLogService;
	public static final Logger logger = Logger
			.getLogger(SystemManageController.class);

	public SystemManageController() {
		super();
		// TODO Auto-generated constructor stub
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DateConvertEditor());
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@RequestMapping(value = "/module/index")
	public String index(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("id", id);
		List<Map<String, Object>> list = iSystemService.querySelectModules();
		JSONObject map = new JSONObject();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> temp = list.get(i);
			map.put(temp.get("i"), temp.get("n"));
		}
		map.put("0", "根菜单");
		request.setAttribute("moduleMap", map.toString());
		return "manage/system/systemModule";
	}

	@RequestMapping(value = "/module/queryModule")
	public @ResponseBody
	Object queryModule(
			@RequestParam(value = "moduleId", required = false) Long moduleId,
			@RequestParam(value = "moduleName", required = false) String moduleName,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pagaSize,
			HttpServletRequest request, HttpServletResponse response) {
		Long count = iSystemService.getSystemModuleCount(moduleId, moduleName);
		List<SystemModule> list = iSystemService.querySystemModule(moduleId,
				moduleName, startNo, pagaSize);
		return new GridPanel(count, list, true);
	}

	@RequestMapping(value = "/module/queryModulesByRoleId/{roleId}")
	public @ResponseBody
	Object queryModulesByRoleId(@PathVariable Long roleId) {
		List<SystemModule> list = iSystemService.queryModulesByRoleId(roleId);
		return list;
	}

	@RequestMapping(value = "/module/queryModulesAll")
	public @ResponseBody
	Object queryModulesAll() {
		List<SystemModule> list = iSystemService.queryModulesAll();
		TreeMenu menu = new TreeMenu(list);
		return JSONArray.fromObject(JSONArray
				.fromObject(menu.getTreeJson().getChildren()).toString()
				.replaceAll("\"leaf", "\"checked\":false,\"leaf"));
	}

	@RequestMapping(value = "/module/saveModule")
	@ResponseBody
	public Object saveModule(@ModelAttribute SystemModule systemModule,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Date date = new Date();
			if (!StringUtils.isNotBlank(ObjectUtils.toString(systemModule))) {
				return new ExtReturn(false, "模块不能为空！");
			}
			if (!StringUtils.isNotBlank(systemModule.getModuleName())) {
				return new ExtReturn(false, "模块名称不能为空！");
			}
			if (StringUtils.isNotBlank(ObjectUtils.toString(systemModule
					.getModuleId()))) {
				systemModule.setUpdateDate(date);
			} else {
				systemModule.setStatus(1);
				systemModule.setCreateDate(date);
				systemModule.setUpdateDate(date);
			}
			if (iSystemService.saveOrUpdateSystemModule(systemModule)) {
				return new ExtReturn(true, "保存成功！");
			} else {
				return new ExtReturn(false, "保存失败！");
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/module/delModule/{moduleId}")
	@ResponseBody
	public Object deleteModule(@PathVariable Long moduleId) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(moduleId))) {
				return new ExtReturn(false, "模块主键不能为空！");
			}
			if (iSystemService.deleteModule(moduleId)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * @Title: roleIndex
	 * @Description: TODO(角色信息首页)
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/role/index")
	public String roleIndex(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("id", id);

		return "manage/system/systemRole";
	}

	@RequestMapping(value = "/role/{userId}")
	@ResponseBody
	public Object queryRoleByUserId(@PathVariable Long userId,
			HttpServletRequest request, HttpServletResponse response) {
		JSONArray jarr = new JSONArray();
		List<SystemRole> list = iSystemService.queryRoleByUserId(userId);
		if (null != list) {
			for (SystemRole sr : list) {
				jarr.add(JSONObject.fromObject(sr));
			}
		}
		return jarr;
	}
	
	@RequestMapping(value = "/role/select")
	@ResponseBody
	public Object queryAllRole(HttpServletRequest request,
			HttpServletResponse response) {
		List<SystemRole> list = iSystemService.querySystemRole(null, null, 1,
				0, Integer.MAX_VALUE);
		long total = 0L;
		if (null != list) {
			total = list.size();
		}
		return new GridPanel(total, list, true);
	}
	
	@RequestMapping(value = "/role/queryRole")
	@ResponseBody
	public Object queryRole(
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pagaSize,
			HttpServletRequest request, HttpServletResponse response) {
		Long count = iSystemService.getSystemRoleCount(null, key, 1);
		List<SystemRole> list = iSystemService.querySystemRole(null, key, 1,
				startNo, pagaSize);
		return new GridPanel(count, list, true);
	}

	@RequestMapping(value = "/role/saveSystemRole")
	@ResponseBody
	public Object saveSystemRole(@ModelAttribute SystemRole systemRole,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Date date = new Date();
			if (!StringUtils.isNotBlank(ObjectUtils.toString(systemRole))) {
				return new ExtReturn(false, "角色不能为空！");
			}
			if (!StringUtils.isNotBlank(systemRole.getRoleName())) {
				return new ExtReturn(false, "角色名称不能为空！");
			}
			if (StringUtils.isNotBlank(ObjectUtils.toString(systemRole
					.getRoleId()))) {
				List<SystemRole> srList = iSystemService.querySystemRole(systemRole
						.getRoleId(), null, 1, null, null);
				systemRole.setCreateDate(srList.get(0).getCreateDate());
				systemRole.setUpdateDate(date);
			} else {
				systemRole.setStatus(1);
				systemRole.setCreateDate(date);
				systemRole.setUpdateDate(date);
			}
			if (iSystemService.saveOrUpdateSystemRole(systemRole)) {
				return new ExtReturn(true, "保存成功！");
			} else {
				return new ExtReturn(false, "保存失败！");
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * @Title: saveRoleModule
	 * @Description: (保存角色的系统菜单信息)
	 * @param roleId
	 * @param moduleIds
	 * @return 设定文件
	 * @return Object 返回类型
	 */
	@RequestMapping("/role/saveRoleModule")
	@ResponseBody
	public Object saveRoleModule(@RequestParam(value = "roleId") String roleId,
			@RequestParam(value = "moduleIds") String moduleIds) {
		try {
			ArrayList<Long> modulesIdList = new ArrayList<Long>();
			if (StringUtils.isBlank(roleId)) {
				return new ExtReturn(false, "角色不能为空！");
			}
			if (StringUtils.isBlank(moduleIds)) {
				return new ExtReturn(false, "选择的资源不能为空！");
			} else {
				String[] modules = StringUtils.split(moduleIds, ",");
				if (null == modules || modules.length == 0) {
					return new ExtReturn(false, "选择的资源不能为空！");
				}
				for (int i = 0; i < modules.length; i++) {
					modulesIdList.add(new Long(modules[i]));
				}
				if (iSystemService.saveOrUpdateSystemRoleModule(
						Long.parseLong(roleId), modulesIdList)) {
					return new ExtReturn(true, "保存成功！");
				} else {
					return new ExtReturn(false, "保存失败！");
				}
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * @Title: userIndex
	 * @Description: TODO(用户设置首页)
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @return String 返回类型
	 */
	@RequestMapping(value = "/user/index")
	public String userIndex(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("id", id);
		return "manage/system/systemUser";
	}

	@RequestMapping(value = "/user/querySystemUser")
	@ResponseBody
	public Object querySystemUser(
			@RequestParam(value = "realName", required = false) String realName,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pagaSize,
			HttpServletRequest request, HttpServletResponse response) {
		Long count = iSystemService.getSystemUserCount(null, realName, null);
		List<SystemUser> list = iSystemService.querySystemUser(null, realName, null,
				startNo, pagaSize);
		return new GridPanel(count, list, true);
	}
	

	@RequestMapping(value = "/user/save")
	@ResponseBody
	public Object saveSystemUser(@ModelAttribute SystemUser systemUser,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Date date = new Date();
			
			if (!StringUtils.isNotBlank(ObjectUtils.toString(systemUser))) {
				return new ExtReturn(false, "用户不能为空！");
			}
			if (!StringUtils.isNotBlank(request.getParameter("roleIds"))) {
				return new ExtReturn(false, "请至少为此用户分配一个角色！");
			}
			if (StringUtils.isNotBlank(ObjectUtils.toString(systemUser
					.getUserId()))) {
				systemUser.setUpdateDate(date);
				systemUser.setStatus(1);
			} else {
				systemUser.setStatus(1);
				systemUser.setCreateDate(date);
				systemUser.setUpdateDate(date);
			}
			System.out.println("====="+systemUser);
			System.out.println("===getRoleIds=="+systemUser.getRoleIds());
			if (iSystemService.saveOrUpdateSystemUserAndRoleIds(systemUser,
					systemUser.getRoleIds())) {
				return new ExtReturn(true, "保存成功！");
			} else {
				return new ExtReturn(false, "保存失败！");
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping(value = "/user/resetPwd")
	@ResponseBody
	public Object resetPwd(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "newpassword", required = false) String newpassword,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(userId))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			if (iSystemService.resetSysUserPwd(userId,newpassword)) {
				return new ExtReturn(true, "密码重置成功！");
			} else {
				return new ExtReturn(false, "密码重置失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping("/user/del/{userId}")
	@ResponseBody
	public Object delSystemUser(@PathVariable Long userId) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(userId))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			if (iSystemService.dellSystemUser(userId)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * @Title: fieldIndex
	 * @Description: TODO(系统字段设置首页)
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @return String 返回类型
	 */
	@RequestMapping(value = "/field/index")
	public String fieldIndex(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("id", id);
		return "manage/system/systemField";
	}

	@RequestMapping(value = "/field/querySystemField")
	@ResponseBody
	public Object querySystemField(
			@RequestParam(value = "fieldName", required = false) String key,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pagaSize,
			HttpServletRequest request, HttpServletResponse response) {
		Long count = iSystemService.getSystemFieldCount(null, key, null);
		List<SystemField> list = iSystemService.querySystemField(null, key,
				null, startNo, pagaSize);
		return new GridPanel(count, list, true);
	}

	@RequestMapping(value = "/field/saveSystemField")
	@ResponseBody
	public Object saveSystemField(@ModelAttribute SystemField systemField,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Date date = new Date();
			if (!StringUtils.isNotBlank(ObjectUtils.toString(systemField))) {
				return new ExtReturn(false, "系统字段不能为空！");
			}
			if (!StringUtils.isNotBlank(systemField.getField())) {
				return new ExtReturn(false, "字段不能为空！");
			}
			if (!StringUtils.isNotBlank(systemField.getFieldName())) {
				return new ExtReturn(false, "字段名称不能为空！");
			}
			if (!StringUtils.isNotBlank(systemField.getValueField())) {
				return new ExtReturn(false, "字段值不能为空！");
			}
			if (!StringUtils.isNotBlank(systemField.getDisplayField())) {
				return new ExtReturn(false, "字段显示值不能为空！");
			}
			if (iSystemService.saveOrUpdateSystemField(systemField)) {
				// 更新缓存数据字典数据。
				SystemFieldsCache.fields.clear();
				SystemFieldsCache.fields.putAll(iSystemService.qeuryAllFields());
				return new ExtReturn(true, "保存成功！");
			} else {
				return new ExtReturn(false, "保存失败！");
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/field/delSystemField/{fieldId}")
	@ResponseBody
	public Object deleteSystemField(@PathVariable Long fieldId) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(fieldId))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			if (iSystemService.dellSystemField(fieldId)) {
				SystemFieldsCache.fields.clear();
				SystemFieldsCache.fields.putAll(iSystemService.qeuryAllFields());
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping(value = "/systemlog/index")
	public String systemlogIndex(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("id", id);
		return "manage/system/systemlog";
	}
	
	@RequestMapping(value = "/systemlog/querySystemLog")
	@ResponseBody
	public Object querySystemLog(
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(account)){
			params.put("loginaccount", account);
		}
		if(StringUtils.isNotBlank(startDate)){
			params.put("startDate", startDate);
		}
		if(StringUtils.isNotBlank(endDate)){
			params.put("endDate", endDate);
		}
		params.put("sortColumns", "logintime DESC");
		Long count = systemUserLoginLogService.count(params);
		List<SystemUserLoginLog> list = systemUserLoginLogService.getList(params, thisPage, pageSize);
		return new GridPanel(count, list, true);
	}

}
