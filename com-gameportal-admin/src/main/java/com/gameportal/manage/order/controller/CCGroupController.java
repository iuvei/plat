package com.gameportal.manage.order.controller;

import java.sql.Timestamp;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.order.model.CCAndGroup;
import com.gameportal.manage.order.model.CCGroup;
import com.gameportal.manage.order.model.CompanyCard;
import com.gameportal.manage.order.service.ICCAndGroupService;
import com.gameportal.manage.order.service.ICCGroupService;
import com.gameportal.manage.order.service.ICompanyCardService;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.pojo.TreeCompanyCard;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.system.service.ISystemService;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.WebConstants;

@Controller
@RequestMapping(value = "/manage/ccgroup")
public class CCGroupController {

	private static final Logger logger = Logger
			.getLogger(CCGroupController.class);

	@Resource(name = "cCAndGroupServiceImpl")
	private ICCAndGroupService cCAndGroupService = null;
	@Resource(name = "companyCardServiceImpl")
	private ICompanyCardService companyCardService = null;
	@Resource(name = "cCGroupServiceImpl")
	private ICCGroupService cCGroupService = null;
	@Resource(name = "systemServiceImpl")
	private ISystemService systemService = null;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;

	public CCGroupController() {
		super();
	}

	@RequestMapping(value = "/index")
	public String index(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		// `status` int(2) default NULL COMMENT '状态 0未锁定 1锁定',
		JSONObject map = new JSONObject();
		map.put("0", "星级分组");
		map.put("1", "自定义分组");
		request.setAttribute("typeMap", map.toString());
		return "manage/order/ccgroup";
	}

	@RequestMapping(value = "/queryCcgroup")
	public @ResponseBody
	Object queryCCGroup(
			@RequestParam(value = "ccgid", required = false) Long ccgid,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "grade", required = false) Integer grade,
			@RequestParam(value = "name", required = false) String name,

			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (null != ccgid) {
			params.put("ccgid", ccgid);
		}
		if (null != type) {
			params.put("type", type);
		}
		if (null != grade) {
			params.put("grade", grade);
		}
		if (StringUtils.isNotBlank(name)) {
			params.put("name", name);
		}
		Long count = cCGroupService.queryCCGroupCount(params);
		List<CCGroup> list = cCGroupService.queryCCGroup(params, startNo,
				pageSize);
		return new GridPanel(count, list, true);
	}

	@RequestMapping(value = "/save")
	@ResponseBody
	public Object save(@ModelAttribute CCGroup cCGroup,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Timestamp date = new Timestamp(new Date().getTime());
			if (!StringUtils.isNotBlank(ObjectUtils.toString(cCGroup))) {
				return new ExtReturn(false, "分组不能为空！");
			}

			if (0 == cCGroup.getType()) {
				// 会员星级分组
				if (null == cCGroup.getGrade() || cCGroup.getGrade() < 0) {
					return new ExtReturn(false, "星级分组的星级必须大于0！");
				}
				cCGroup.setName(cCGroup.getGrade() + "星级分组");
				// 查询是否已存在对应星级分组
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("type", 0);
				params.put("grade", cCGroup.getGrade());
				List<CCGroup> existList = cCGroupService.queryCCGroup(params,
						0, 0);
				if (null != existList && existList.size() > 0) {
					for (CCGroup ccg : existList) {
						if (cCGroup.getGrade().intValue() == ccg.getGrade()
								.intValue()
								&& cCGroup.getCcgid().longValue() != ccg
										.getCcgid().longValue()) {
							return new ExtReturn(false, "该星级[" + ccg.getGrade()
									+ "]下已有分组，请重新填写星级！");
						}
					}
				}
			} else if (1 == cCGroup.getType()) {
				// 自定义分组
				if (null == cCGroup.getName()) {
					cCGroup.setName("自定义分组" + cCGroup.getCcgid());
				}
				// 查询是否已存在相同名称的自定义分组
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("name", cCGroup.getName());
				List<CCGroup> existList = cCGroupService.queryCCGroup(params,
						0, 0);
				if (null != existList && existList.size() > 0) {
					for (CCGroup ccg : existList) {
						if (cCGroup.getName().equals(ccg.getName())) {
							return new ExtReturn(false, "该分组名称["
									+ ccg.getName() + "]已被占用，请重新填写分组名称！");
						}
					}
				}
			}

			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(
					key, c);

			if (StringUtils
					.isNotBlank(ObjectUtils.toString(cCGroup.getCcgid()))) {
				CCGroup old = cCGroupService.queryById(cCGroup.getCcgid());
				if (null != old) {
					cCGroup.setCreatetime(old.getCreatetime());
					cCGroup.setCreateuserid(old.getCreateuserid());
					cCGroup.setCreateusername(old.getCreateusername());
				}
				// 更新分组
				cCGroup.setUpdatetime(date);
				cCGroup.setUpdateuserid(systemUser.getUserId());
				cCGroup.setUpdateusername(systemUser.getRealName());
			} else {
				// 新建分组
				cCGroup.setCreatetime(date);
				cCGroup.setCreateuserid(systemUser.getUserId());
				cCGroup.setCreateusername(systemUser.getRealName());
			}
			if (cCGroupService.saveOrUpdateCCGroup(cCGroup)) {
				return new ExtReturn(true, "提交成功！");
			} else {
				return new ExtReturn(false, "提交失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/del/{ccgid}")
	@ResponseBody
	public Object delCCGroup(@PathVariable Long ccgid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(ccgid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			Map<String, Object> params = new HashMap<String, Object>();
			if (null != ccgid) {
				params.put("ccgid", ccgid);
			}
			List<CCAndGroup> list = cCAndGroupService.queryCCAndGroup(params,
					0, 0);
			if (null != list && list.size() > 0) {
				return new ExtReturn(false, "该分组下还有公司银行卡，不能删除！");
			}
			if (cCGroupService.deleteCCGroup(ccgid)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/cc/queryByCcgid/{ccgid}")
	public @ResponseBody
	Object queryCCByCcgid(@PathVariable Long ccgid) {
		List<CompanyCard> list = companyCardService.queryByCcgid(ccgid);
		return list;
	}

	@RequestMapping("/cc/queryAll")
	public @ResponseBody
	Object queryCCAll() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", 0); // 状态 0未锁定 1锁定
		params.put("sortColumns", "ccid asc");
		List<CompanyCard> list = companyCardService.queryCompanyCard(params, 0,
				100);
		TreeCompanyCard cctree = new TreeCompanyCard(list);
		return JSONArray.fromObject(JSONArray
				.fromObject(cctree.getTreeJson().getChildren()).toString()
				.replaceAll("\"leaf", "\"checked\":false,\"leaf"));
	}
	
	@RequestMapping("/cc/saveCCAndGroup")
	@ResponseBody
	public Object saveCCAndGroup(@RequestParam(value = "ccgid") Long ccgid,
			@RequestParam(value = "ccids") String ccids) {
		try {
			ArrayList<Long> ccidIdList = new ArrayList<Long>();
			if (null == ccgid) {
				return new ExtReturn(false, "分组不能为空！");
			}
			if (StringUtils.isBlank(ccids)) {
				return new ExtReturn(false, "选择的银行卡不能为空！");
			} else {
				String[] arr = StringUtils.split(ccids, ",");
				if (null == arr || arr.length == 0) {
					return new ExtReturn(false, "选择的银行卡不能为空！");
				}
				for (int i = 0; i < arr.length; i++) {
					ccidIdList.add(new Long(arr[i]));
				}
				if (cCAndGroupService.saveOrUpdateCCAndGroup(ccgid,
						ccidIdList)) {
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

}
