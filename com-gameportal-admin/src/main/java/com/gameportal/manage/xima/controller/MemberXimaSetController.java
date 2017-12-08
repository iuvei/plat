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

import com.gameportal.manage.gameplatform.model.GamePlatform;
import com.gameportal.manage.gameplatform.service.IGamePlatformService;
import com.gameportal.manage.listener.SystemFieldsCache;
import com.gameportal.manage.member.model.XimaFlag;
import com.gameportal.manage.member.service.IXimaFlagService;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.DateUtil;
import com.gameportal.manage.util.WebConstants;
import com.gameportal.manage.xima.model.MemberXimaSet;
import com.gameportal.manage.xima.service.IMemberXimaSetService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/manage/memberximaset")
public class MemberXimaSetController {

	private static final Logger logger = Logger
			.getLogger(MemberXimaSetController.class);

	@Resource(name = "memberXimaSetServiceImpl")
	private IMemberXimaSetService memberXimaSetService = null;
	@Resource(name = "gamePlatformServiceImpl")
	private IGamePlatformService gamePlatformService = null;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	@Resource(name="ximaFlagService")
	private IXimaFlagService  ximaFlagService;

	public MemberXimaSetController() {
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
		// 游戏平台集合(状态 0 关闭 1开启)
		JSONObject gameplatMap = new JSONObject();
		List<GamePlatform> gpList = gamePlatformService.queryGamePlatform(null,
				null, 1, 0, 0);
		if (null != gpList && gpList.size() > 0) {
			for (GamePlatform gp : gpList) {
				gameplatMap.put(gp.getGpid(), gp.getGpname());
			}
		}
		gameplatMap.put("", "全部");
		JSONObject  json = JSONObject.fromObject(SystemFieldsCache.fields.get("memGrade").toString());
		json.put("", "全部");
		request.setAttribute("memGradeMap", json.toString());
		request.setAttribute("gameplatMap", gameplatMap.toString());
		return "manage/xima/memberximaset";
	}

	@RequestMapping(value = "/queryMemberximaset")
	public @ResponseBody
	Object queryMemberXimaSet(
			@RequestParam(value = "gpid", required = false) String gpid,
			@RequestParam(value = "grade", required = false) String grade,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gpid", gpid);
		params.put("grade", grade);
		Long count = memberXimaSetService.queryMemberXimaSetCount(params);
		List<MemberXimaSet> list = memberXimaSetService.queryMemberXimaSet(
				params, startNo, pageSize);
		return new GridPanel(count, list, true);
	}

	@RequestMapping("/del/{mxsid}")
	@ResponseBody
	public Object delMemberXimaSet(@PathVariable Long mxsid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(mxsid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			if (memberXimaSetService.deleteMemberXimaSet(mxsid)) {
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
	public Object save(@ModelAttribute MemberXimaSet mxs,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Timestamp date = new Timestamp(new Date().getTime());
			if (!StringUtils.isNotBlank(ObjectUtils.toString(mxs))) {
				return new ExtReturn(false, "会员洗码设置不能为空！");
			}
			if (null == mxs.getGrade() || mxs.getGrade() <= 0) {
				return new ExtReturn(false, "会员星级值非法，请重新输入[范围:1~100]！");
			} 
//			else if (null == mxs.getScale()
//					|| mxs.getScale().floatValue() <= 0
//					|| mxs.getScale().floatValue() > 100) {
//				return new ExtReturn(false, "返水比例值非法，请重新输入[范围:0~100]！");
//			}
//			mxs.setScale(mxs.getScale().divide(new BigDecimal(100)));

			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(
					key, c);
			if (StringUtils.isNotBlank(ObjectUtils.toString(mxs.getMxsid()))) {
				mxs.setUpdatetime(date);
				mxs.setUpdateuserid(systemUser.getUserId());
				mxs.setUpdateusername(systemUser.getRealName());
			} else {
				mxs.setUpdatetime(date);
				mxs.setUpdateuserid(systemUser.getUserId());
				mxs.setUpdateusername(systemUser.getRealName());
			}
			if (memberXimaSetService.saveOrUpdateMemberXimaSet(mxs)) {
				return new ExtReturn(true, "保存成功！");
			} else {
				return new ExtReturn(false, "保存失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping(value = "/list")
	public String list(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		request.setAttribute("startDate", DateUtil.getStrByDate(DateUtil.addDay(new Date(),-30), "yyyy-MM-dd"));
		request.setAttribute("endDate", DateUtil.getStrByDate(new Date(), "yyyy-MM-dd"));
		return "manage/xima/historylist";
	}
	
	@RequestMapping(value = "/historylist")
	@ResponseBody
	public Object historylist(
			@RequestParam(value = "flagaccount", required = false) String flagaccount,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response, Object ximaf) {
		Map<String, Object> map = new HashMap<>();
		if(StringUtils.isNotEmpty(flagaccount)){
			map.put("flagaccount", flagaccount);
		}
		if(StringUtils.isNotEmpty(startDate)){
			map.put("startDate", startDate);
		}else{
			map.put("startDate", DateUtil.getStrByDate(DateUtil.addDay(new Date(),-30), "yyyy-MM-dd"));			
		}
		if(StringUtils.isNotEmpty(endDate)){
			map.put("endDate", endDate);
		}else{
			map.put("endDate", DateUtil.getStrByDate(new Date(), "yyyy-MM-dd"));
		}
		Long count = ximaFlagService.getCount(map);
		map.put("sortColumns", "updatetime desc");
		List<XimaFlag> list = ximaFlagService.getList(map, startNo, pageSize);
		return new GridPanel(count, list, true);
	}
}
