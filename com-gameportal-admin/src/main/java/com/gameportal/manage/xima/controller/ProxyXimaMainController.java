package com.gameportal.manage.xima.controller;

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

import com.gameportal.manage.gameplatform.model.GamePlatform;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.user.model.UserInfo;
import com.gameportal.manage.user.service.IUserInfoService;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.DateUtil;
import com.gameportal.manage.util.WebConstants;
import com.gameportal.manage.xima.model.MemberXimaMain;
import com.gameportal.manage.xima.model.ProxyXimaDetail;
import com.gameportal.manage.xima.model.ProxyXimaMain;
import com.gameportal.manage.xima.service.IProxyXimaDetailService;
import com.gameportal.manage.xima.service.IProxyXimaMainService;

@Controller
@RequestMapping(value = "/manage/proxyximamain")
public class ProxyXimaMainController {

	private static final Logger logger = Logger
			.getLogger(ProxyXimaMainController.class);

	@Resource(name = "proxyXimaMainServiceImpl")
	private IProxyXimaMainService proxyXimaMainService = null;
	@Resource(name = "proxyXimaDetailServiceImpl")
	private IProxyXimaDetailService proxyXimaDetailService = null;
	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService = null;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;

	public ProxyXimaMainController() {
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
		// locked 锁定状态 0未锁定 1锁定
		JSONObject lockedMap = new JSONObject();
		lockedMap.put("0", "未锁定");
		lockedMap.put("1", "锁定");
		request.setAttribute("lockedMap", lockedMap.toString());
		// opttype 操作类型 0自助洗码，1洗码清零，2强制洗码
		JSONObject opttypeMap = new JSONObject();
		opttypeMap.put("0", "自助洗码");
		opttypeMap.put("1", "洗码清零");
		opttypeMap.put("2", "强制洗码");
		request.setAttribute("opttypeMap", opttypeMap.toString());
		return "manage/xima/proxyximamain";
	}

	@RequestMapping(value = "/queryProxyximamain")
	public @ResponseBody
	Object queryProxyXimaMain(
			@RequestParam(value = "account", required = false) String account,

			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(account)) {
			params.put("account", account);
		}
		Long count = proxyXimaMainService.queryProxyXimaMainCount(params);
		List<ProxyXimaMain> list = proxyXimaMainService.queryProxyXimaMain(
				params, startNo, pageSize);
		return new GridPanel(count, list, true);
	}

	@RequestMapping("/del/{pxmid}")
	@ResponseBody
	public Object delProxyXimaMain(@PathVariable Long pxmid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(pxmid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pxmid", pxmid);
			List<ProxyXimaMain> list = proxyXimaMainService.queryProxyXimaMain(
					params, 0, 0);
			if (null != list && list.size() > 0) {
				params = new HashMap<String, Object>();
				params.put("uiid", list.get(0).getUiid());
				List<ProxyXimaDetail> detailList = proxyXimaDetailService
						.queryProxyXimaDetail(params, 0, 0);
				if (null != detailList && detailList.size() > 0) {
					return new ExtReturn(false, "该代理有洗码明细记录，不能删除该代理洗码总记录！");
				}
			}
			if (proxyXimaMainService.deleteProxyXimaMain(pxmid)) {
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
	public Object saveProxyXimaMain(
			@ModelAttribute ProxyXimaMain proxyXimaMain,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Timestamp date = new Timestamp(new Date().getTime());
			if (!StringUtils.isNotBlank(ObjectUtils.toString(proxyXimaMain))) {
				return new ExtReturn(false, "代理洗码总记录不能为空！");
			}
			if (StringUtils.isBlank(proxyXimaMain.getAccount())) {
				return new ExtReturn(false, "代理账号不能为空！");
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("account", proxyXimaMain.getAccount());
			List<UserInfo> userList = userInfoService.queryUserinfo(params,
					null, null);
			if (null == userList || userList.size() == 0) {
				return new ExtReturn(false, "不存在[" + proxyXimaMain.getAccount()
						+ "]对应代理，请填写有效的代理账号！");
			}
			proxyXimaMain.setUiid(userList.get(0).getUiid());
			proxyXimaMain.setName(userList.get(0).getUname());
			proxyXimaMain.setYmdstart(new java.sql.Date(userList.get(0)
					.getCreateDate().getTime()));
			proxyXimaMain.setYmdend(proxyXimaMain.getYmdstart());
			proxyXimaMain.setTotal(0L);
			proxyXimaMain.setLocked(0);// 锁定状态 0未锁定，1锁定
			proxyXimaMain.setUpdatetime(date);

			if (proxyXimaMainService.saveOrUpdateProxyXimaMain(proxyXimaMain)) {
				return new ExtReturn(true, "保存成功！");
			} else {
				return new ExtReturn(false, "保存失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}


	@RequestMapping("/clear/{pxmid}")
	@ResponseBody
	public Object clearProxyXimaMain(
			@PathVariable Long pxmid,
			@RequestParam(value = "ymdstart", required = false) String ymdstart,
			@RequestParam(value = "ymdend", required = false) String ymdend,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Timestamp date = new Timestamp(new Date().getTime());
			if (!StringUtils.isNotBlank(ObjectUtils.toString(pxmid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(
					key, c);

			// 获取洗码总记录
			ProxyXimaMain pxm = null;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pxmid", pxmid);
			List<ProxyXimaMain> list = proxyXimaMainService
					.queryProxyXimaMain(params, 0, 0);
			if (null == list || list.size() <= 0) {
				return new ExtReturn(false, "该记录不存在，请刷新后再试！");
			} else {
				pxm = list.get(0);
			}
			// 最后一次洗码时间过滤
			if (DateUtil.getStrYMDByDate(pxm.getYmdend()).equals(
					DateUtil.getStrYMDByDate(new Date()))) {
				return new ExtReturn(false, "今天已进行过洗码操作，请明天后再试！");
			}
			// 锁定状态 0未锁定 1锁定
			if (1 == pxm.getLocked()) {
				return new ExtReturn(false, "该记录已被锁定，请先解锁后再操作！");
			}
			// 获取洗码开始日期
			Date ymdstartDate = null;
			if (StringUtils.isBlank(ymdstart)) {
				ymdstartDate = pxm.getYmdend();
			} else {
				ymdstartDate = DateUtil.getDateByStr(ymdstart);
			}
			// 获取洗码结束日期
			Date ymdendDate = null;
			if (StringUtils.isBlank(ymdend)) {
				ymdendDate = new Date();
			} else {
				ymdendDate = DateUtil.getDateByStr(ymdend);
			}
			// 洗码清零（保存洗码明细记录）
			if (false == proxyXimaMainService.clearXima(pxm, ymdstartDate,
					ymdendDate, systemUser)) {
				return new ExtReturn(false, "操作失败！");
			}
			// 保存洗总记录
			pxm.setUpdatetime(date);
			pxm.setYmdend(new java.sql.Date(ymdendDate.getTime()));
			if (proxyXimaMainService.saveOrUpdateProxyXimaMain(pxm)) {
				return new ExtReturn(true, "操作成功！");
			} else {
				return new ExtReturn(false, "操作失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/force/{pxmid}")
	@ResponseBody
	public Object forceProxyXimaMain(
			@PathVariable Long pxmid,
			@RequestParam(value = "ymdstart", required = false) String ymdstart,
			@RequestParam(value = "ymdend", required = false) String ymdend,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Timestamp date = new Timestamp(new Date().getTime());
			if (!StringUtils.isNotBlank(ObjectUtils.toString(pxmid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(
					key, c);

			// 获取洗码总记录
			ProxyXimaMain pxm = null;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pxmid", pxmid);
			List<ProxyXimaMain> list = proxyXimaMainService
					.queryProxyXimaMain(params, 0, 0);
			if (null == list || list.size() <= 0) {
				return new ExtReturn(false, "该记录不存在，请刷新后再试！");
			} else {
				pxm = list.get(0);
			}
			// 锁定状态 0未锁定 1锁定
			if (1 == pxm.getLocked()) {
				return new ExtReturn(false, "该记录已被锁定，请先解锁后再操作！");
			}
			// 获取洗码开始日期
			Date ymdstartDate = null;
			if (StringUtils.isBlank(ymdstart)) {
				ymdstartDate = pxm.getYmdend();
			} else {
				ymdstartDate = DateUtil.getDateByStr(ymdstart);
			}
			// 获取洗码结束日期
			Date ymdendDate = null;
			if (StringUtils.isBlank(ymdend)) {
				ymdendDate = new Date();
			} else {
				ymdendDate = DateUtil.getDateByStr(ymdend);
			}
			// 洗码清零（保存洗码明细记录）
			if (false == proxyXimaMainService.forceXima(pxm, ymdstartDate,
					ymdendDate, systemUser)) {
				return new ExtReturn(false, "操作失败！");
			}
			// 保存洗总记录
			pxm.setUpdatetime(date);
			pxm.setYmdend(new java.sql.Date(ymdendDate.getTime()));
			if (proxyXimaMainService.saveOrUpdateProxyXimaMain(pxm)) {
				return new ExtReturn(true, "操作成功！");
			} else {
				return new ExtReturn(false, "操作失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/enable/{pxmid}")
	@ResponseBody
	public Object enableProxyXimaMain(@PathVariable Long pxmid,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(pxmid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			Timestamp date = new Timestamp(new Date().getTime());
			// 获取洗码总记录
			ProxyXimaMain mxm = null;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pxmid", pxmid);
			List<ProxyXimaMain> list = proxyXimaMainService
					.queryProxyXimaMain(params, 0, 0);
			if (null == list || list.size() <= 0) {
				return new ExtReturn(false, "该记录不存在，请刷新后再试！");
			} else {
				mxm = list.get(0);
			}
			if (0 == mxm.getLocked()) {// locked 锁定状态 0未锁定 1锁定
				return new ExtReturn(false, "该记录已经是[未锁定]状态，不用解锁！");
			}
			mxm.setLocked(0);
			mxm.setUpdatetime(date);
			if (proxyXimaMainService.saveOrUpdateProxyXimaMain(mxm)) {
				return new ExtReturn(true, "解锁成功！");
			} else {
				return new ExtReturn(false, "解锁失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/disable/{pxmid}")
	@ResponseBody
	public Object disableProxyXimaMain(@PathVariable Long pxmid,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(pxmid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			Timestamp date = new Timestamp(new Date().getTime());
			// 获取洗码总记录
			ProxyXimaMain mxm = null;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pxmid", pxmid);
			List<ProxyXimaMain> list = proxyXimaMainService
					.queryProxyXimaMain(params, 0, 0);
			if (null == list || list.size() <= 0) {
				return new ExtReturn(false, "该记录不存在，请刷新后再试！");
			} else {
				mxm = list.get(0);
			}
			if (1 == mxm.getLocked()) {// locked 锁定状态 0未锁定 1锁定
				return new ExtReturn(false, "该记录已经是[锁定]状态，不用锁定！");
			}
			mxm.setLocked(1);
			mxm.setUpdatetime(date);
			if (proxyXimaMainService.saveOrUpdateProxyXimaMain(mxm)) {
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
