package com.gameportal.manage.order.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.gameportal.manage.order.model.CompanyCard;
import com.gameportal.manage.order.service.ICompanyCardService;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.WebConstants;

@Controller
@RequestMapping(value = "/manage/companycard")
public class CompanyCardController {
	@Resource(name = "companyCardServiceImpl")
	private ICompanyCardService companyCardService = null;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;

	private static final Logger logger = Logger
			.getLogger(CompanyCardController.class);

	public CompanyCardController() {
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
		map.put("0", "未锁定");
		map.put("1", "锁定");
		request.setAttribute("statusMap", map.toString());
		return "manage/order/companycard";
	}

	@RequestMapping(value = "/queryCompanycard")
	public @ResponseBody
	Object queryMember(
			@RequestParam(value = "ccno", required = false) String ccno,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(ccno)) {
			params.put("ccno", ccno);
		}
		Long count = companyCardService.queryCompanyCardCount(params);
		List<CompanyCard> list = companyCardService.queryCompanyCard(params,
				startNo, pageSize);
		return new GridPanel(count, list, true);
	}

	@RequestMapping(value = "/save")
	@ResponseBody
	public Object saveMember(@ModelAttribute CompanyCard companyCard,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Timestamp date = new Timestamp(new Date().getTime());
			if (!StringUtils.isNotBlank(ObjectUtils.toString(companyCard))) {
				return new ExtReturn(false, "银行卡不能为空！");
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(
					key, c);

			if (StringUtils.isNotBlank(ObjectUtils.toString(companyCard
					.getCcid()))) {
				companyCard.setUpdatetime(date);
				companyCard.setUpdateuserid(systemUser.getUserId());
				companyCard.setUpdateusername(systemUser.getRealName());
			} else {
				companyCard.setCreatetime(date);
				companyCard.setCreateuserid(systemUser.getUserId());
				companyCard.setCreateusername(systemUser.getRealName());
				companyCard.setUpdatetime(date);
				companyCard.setUpdateuserid(systemUser.getUserId());
				companyCard.setUpdateusername(systemUser.getRealName());
			}
			if (companyCardService.saveOrUpdateCompanyCard(companyCard)) {
				return new ExtReturn(true, "保存成功！");
			} else {
				return new ExtReturn(false, "保存失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/del/{ccid}")
	@ResponseBody
	public Object delUserInfo(@PathVariable Long ccid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(ccid))) {
				return new ExtReturn(false, "公司银行卡主键不能为空！");
			}
			if (companyCardService.deleteCompanyCard(ccid)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/enable/{uiid}")
	@ResponseBody
	public Object enableSmsPlatAccount(@PathVariable Long uiid, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(uiid))) {
				return new ExtReturn(false, "公司银行卡主键不能为空！");
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(
					key, c);

			if (companyCardService.updateStatus(uiid, 0, systemUser)) {
				return new ExtReturn(true, "解锁成功！");
			} else {
				return new ExtReturn(false, "解锁失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/disable/{uiid}")
	@ResponseBody
	public Object disableSmsPlatAccount(@PathVariable Long uiid, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(uiid))) {
				return new ExtReturn(false, "公司银行卡主键不能为空！");
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(
					key, c);
			if (companyCardService.updateStatus(uiid, 1, systemUser)) {
				return new ExtReturn(true, "锁定成功！");
			} else {
				return new ExtReturn(false, "锁定失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

}
