package com.gameportal.manage.xima.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.user.model.UserInfo;
import com.gameportal.manage.user.service.IUserInfoService;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.WebConstants;
import com.gameportal.manage.xima.model.ProxyXimaSet;
import com.gameportal.manage.xima.service.IProxyXimaSetService;

@Controller
@RequestMapping(value = "/manage/proxyximaset")
public class ProxyXimaSetController {

	private static final Logger logger = Logger
			.getLogger(ProxyXimaSetController.class);

	@Resource(name = "proxyXimaSetServiceImpl")
	private IProxyXimaSetService proxyXimaSetService = null;
	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService = null;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;

	public ProxyXimaSetController() {
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
		return "manage/xima/proxyximaset";
	}

	@RequestMapping(value = "/queryProxyximaset")
	public @ResponseBody
	Object queryProxyXimaSet(
			@RequestParam(value = "account", required = false) String account,

			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (null != account) {
			params.put("account", account);
		}
		Long count = proxyXimaSetService.queryProxyXimaSetCount(params);
		List<ProxyXimaSet> list = proxyXimaSetService.queryProxyXimaSet(params,
				startNo, pageSize);
		return new GridPanel(count, list, true);
	}

	@RequestMapping("/del/{pxsid}")
	@ResponseBody
	public Object delProxyXimaSet(@PathVariable Long pxsid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(pxsid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			if (proxyXimaSetService.deleteProxyXimaSet(pxsid)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping(value = "/save")
	@ResponseBody
	public Object saveProxyXimaSet(@ModelAttribute ProxyXimaSet proxyXimaSet,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Timestamp date = new Timestamp(new Date().getTime());
			if (!StringUtils.isNotBlank(ObjectUtils.toString(proxyXimaSet))) {
				return new ExtReturn(false, "代理设置信息不能为空！");
			}
			if (null == proxyXimaSet.getScale()
					|| proxyXimaSet.getScale().floatValue() <= 0
					|| proxyXimaSet.getScale().floatValue() > 100) {
				return new ExtReturn(false, "返水比例值非法，请重新输入[范围:0~100]！");
			}
			if (StringUtils.isNotBlank(ObjectUtils.toString(proxyXimaSet
					.getPxsid()))) {
				// 更新
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("pxsid", proxyXimaSet.getPxsid());
				List<ProxyXimaSet> pxsList = proxyXimaSetService
						.queryProxyXimaSet(params, 0, 20);
				if (null == pxsList || pxsList.size() <= 0) {
					return new ExtReturn(false, "记录不存在，请刷新后再试！");
				}
				pxsList.get(0).setScale(proxyXimaSet.getScale());
				proxyXimaSet = pxsList.get(0);
			} else {
				// 新增
				// 代理账号有效性过滤
				if (StringUtils.isBlank(proxyXimaSet.getAccount())) {
					return new ExtReturn(false, "代理账号不能为空！");
				}
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("account", proxyXimaSet.getAccount());
				params.put("accounttype", 1); // 帐号类型 0 普通用户 1代理用户
				List<UserInfo> proxyUserList = userInfoService.queryUserinfo(
						params, 0, 0);
				if (null == proxyUserList || proxyUserList.size() <= 0) {
					return new ExtReturn(false, "代理账号无效，请重新输入！");
				}
				// 代理洗码设置唯一性过滤
				params = new HashMap<String, Object>();
				params.put("account", proxyXimaSet.getAccount());
				List<ProxyXimaSet> pxsList = proxyXimaSetService
						.queryProxyXimaSet(params, 0, 0);
				if (null != pxsList && pxsList.size() > 0) {
					return new ExtReturn(false, "该代理账号["
							+ proxyXimaSet.getAccount() + "]已有洗码设置信息，请勿重复设置！");
				}
				proxyXimaSet.setUiid(proxyUserList.get(0).getUiid());
				proxyXimaSet.setName(proxyUserList.get(0).getUname());
			}

			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(
					key, c);
			// 返水比例值转换
			proxyXimaSet.setScale(proxyXimaSet.getScale()/100);
			// 操作者信息
			proxyXimaSet.setUpdatetime(date);
			proxyXimaSet.setUpdateuserid(systemUser.getUserId());
			proxyXimaSet.setUpdateusername(systemUser.getRealName());
			if (proxyXimaSetService.saveOrUpdateProxyXimaSet(proxyXimaSet)) {
				return new ExtReturn(true, "保存成功！");
			} else {
				return new ExtReturn(false, "保存失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

}
