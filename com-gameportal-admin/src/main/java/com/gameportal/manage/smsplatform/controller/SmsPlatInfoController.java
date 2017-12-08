package com.gameportal.manage.smsplatform.controller;

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

import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.smsplatform.model.SmsPlatAccount;
import com.gameportal.manage.smsplatform.model.SmsPlatBlacklist;
import com.gameportal.manage.smsplatform.model.SmsPlatInfo;
import com.gameportal.manage.smsplatform.model.SmsPlatSendlog;
import com.gameportal.manage.smsplatform.service.ISmsPlatInfoService;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.DateUtil2;
import com.gameportal.manage.util.WebConstants;

/**
 * @ClassName: SmsPlatInfoController
 * @Description: TODO(短信平台控制类)
 * @author wbm
 * @date 2014-4-27 下午22:51:41
 */
@Controller
@RequestMapping(value = "/manage/smsplatinfo")
public class SmsPlatInfoController {
	@Resource(name = "smsPlatInfoServiceImpl")
	private ISmsPlatInfoService smsPlatInfoService = null;
	
	@Resource(name = "redisServiceImpl")
	private IRedisService redisService=null;
	
	public static final Logger logger = Logger
			.getLogger(SmsPlatInfoController.class);

	public SmsPlatInfoController() {
		super();
		// TODO Auto-generated constructor stub
	}

	@RequestMapping(value = "/index")
	public String index(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/smsplatinfo/smsPlatInfo";
	}

	@RequestMapping(value = "/querySmsPlatInfo")
	public @ResponseBody
	Object querySmsPlatInfo(
			@RequestParam(value = "spiid", required = false) Long spiid,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Long count = smsPlatInfoService.querySmsPlatInfoCount(spiid, name);
		List<SmsPlatInfo> list = smsPlatInfoService.querySmsPlatInfo(spiid, name, startNo, pageSize);
		return new GridPanel(count, list, true);
	}
	
	@RequestMapping(value = "/saveSmsPlatInfo")
	@ResponseBody
	public Object saveSmsPlatInfo(@ModelAttribute SmsPlatInfo smsPlatInfo,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Timestamp date = new Timestamp(new Date().getTime());
			if (!StringUtils.isNotBlank(ObjectUtils.toString(smsPlatInfo))) {
				return new ExtReturn(false, "短信平台不能为空！");
			}
			
			if (StringUtils.isNotBlank(ObjectUtils.toString(smsPlatInfo
					.getSpiid()))) {
				smsPlatInfo.setUpdatetime(date);
			} else {
				smsPlatInfo.setStatus(1);
				smsPlatInfo.setCreatetime(date);
				smsPlatInfo.setUpdatetime(date);
			}
			if (smsPlatInfoService.saveOrUpdateSmsPlatInfo(smsPlatInfo)) {
				return new ExtReturn(true, "保存成功！");
			} else {
				return new ExtReturn(false, "保存失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping("/delSmsPlatInfo/{spiid}")
	@ResponseBody
	public Object delSmsPlatInfo(@PathVariable Long spiid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(spiid))) {
				return new ExtReturn(false, "短信平台主键不能为空！");
			}
			if (smsPlatInfoService.deleteSmsPlatInfo(spiid)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	//*****************短信平台账号*************************
	
	@RequestMapping(value = "/account/index")
	public String indexAccount(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		List<Map<String, Object>> list = smsPlatInfoService.querySelectSmsPlatInfos();
		JSONObject map = new JSONObject();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> temp = list.get(i);
			map.put(temp.get("i"), temp.get("n"));
		}
		request.setAttribute("smsPlatMap", map.toString());
		return "manage/smsplatinfo/smsPlatAccount";
	}
	
	
	@RequestMapping(value = "/account/querySmsPlatAccount")
	public @ResponseBody
	Object querySmsPlatAccount(
			@RequestParam(value = "spaid", required = false) Long spaid,
			@RequestParam(value = "spiid", required = false) Long spiid,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Long count = smsPlatInfoService.querySmsPlatAccountCount(spaid, spiid, name);
		List<SmsPlatAccount> list = smsPlatInfoService.querySmsPlatAccount(spaid, spiid, name, status, startNo, pageSize);
		return new GridPanel(count, list, true);
	}
	
	@RequestMapping(value = "/account/saveSmsPlatAccount")
	@ResponseBody
	public Object saveSmsPlatAccount(@ModelAttribute SmsPlatAccount smsPlatAccount,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) redisService.getRedisResult(key, c);
			if (!StringUtils.isNotBlank(ObjectUtils.toString(smsPlatAccount))) {
				return new ExtReturn(false, "短信平台账号不能为空！");
			}
			
			if (StringUtils.isNotBlank(ObjectUtils.toString(smsPlatAccount.getSpaid()))) {
				smsPlatAccount.setUpdatename(systemUser.getAccount());
				smsPlatAccount.setUpdatetime(DateUtil2.format2(new Date()));
				if (smsPlatInfoService.saveOrUpdateSmsPlatAccount(smsPlatAccount)) {
					return new ExtReturn(true, "更新成功！");
				} else {
					return new ExtReturn(false, "更新失败！");
				}
			} else {
				smsPlatAccount.setCreatename(systemUser.getAccount());
				smsPlatAccount.setUpdatename(systemUser.getAccount());
				smsPlatAccount.setCreatetime(DateUtil2.format2(new Date()));
				smsPlatAccount.setUpdatetime(DateUtil2.format2(new Date()));
				if (smsPlatInfoService.saveOrUpdateSmsPlatAccount(smsPlatAccount)) {
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
	
	@RequestMapping("/account/delSmsPlatAccount/{spaid}")
	@ResponseBody
	public Object delSmsPlatAccount(@PathVariable Long spaid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(spaid))) {
				return new ExtReturn(false, "短信平台账号主键不能为空！");
			}
			if (smsPlatInfoService.deleteSmsPlatAccount(spaid)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	@RequestMapping("/account/enableSmsPlatAccount/{spaid}")
	@ResponseBody
	public Object enableSmsPlatAccount(@PathVariable Long spaid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(spaid))) {
				return new ExtReturn(false, "短信平台账号主键不能为空！");
			}
			if (smsPlatInfoService.updateSmsPlatAccountOnlyStatus(spaid, 1)) {
				return new ExtReturn(true, "启用成功！");
			} else {
				return new ExtReturn(false, "启用失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	@RequestMapping("/account/disableSmsPlatAccount/{spaid}")
	@ResponseBody
	public Object disableSmsPlatAccount(@PathVariable Long spaid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(spaid))) {
				return new ExtReturn(false, "短信平台账号主键不能为空！");
			}
			if (smsPlatInfoService.updateSmsPlatAccountOnlyStatus(spaid, 0)) {
				return new ExtReturn(true, "禁用成功！");
			} else {
				return new ExtReturn(false, "禁用失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	//*****************短信平台黑名单*************************
	
	@RequestMapping(value = "/blacklist/index")
	public String indexBlacklist(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		List<Map<String, Object>> list = smsPlatInfoService.querySelectSmsPlatInfos();
		JSONObject map = new JSONObject();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> temp = list.get(i);
			map.put(temp.get("i"), temp.get("n"));
		}
		map.put("0", "全部");
		request.setAttribute("smsPlatMap", map.toString());
		return "manage/smsplatinfo/smsPlatBlacklist";
	}


	@RequestMapping(value = "/blacklist/querySmsPlatBlacklist")
	public @ResponseBody
	Object querySmsPlatBlacklist(
			@RequestParam(value = "spbid", required = false) Long spbid,
			@RequestParam(value = "spiid", required = false) Long spiid,
			@RequestParam(value = "mobile", required = false) String mobile,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Long count = smsPlatInfoService.querySmsPlatBlacklistCount(spbid, spiid, mobile);
		List<SmsPlatBlacklist> list = smsPlatInfoService.querySmsPlatBlacklist(spbid, spiid, mobile, startNo, pageSize);
		return new GridPanel(count, list, true);
	}
	
	@RequestMapping(value = "/blacklist/saveSmsPlatBlacklist")
	@ResponseBody
	public Object saveSmsPlatBlacklist(@ModelAttribute SmsPlatBlacklist smsPlatBlacklist,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Timestamp date = new Timestamp(new Date().getTime());
			if (!StringUtils.isNotBlank(ObjectUtils.toString(smsPlatBlacklist))) {
				return new ExtReturn(false, "黑名单号码不能为空！");
			}
			
			if (StringUtils.isNotBlank(ObjectUtils.toString(smsPlatBlacklist
					.getSpiid()))) {
			} else {
				smsPlatBlacklist.setCreatetime(date);
			}
			if (smsPlatInfoService.saveOrUpdateSmsPlatBlacklist(smsPlatBlacklist)) {
				return new ExtReturn(true, "保存成功！");
			} else {
				return new ExtReturn(false, "保存失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping("/blacklist/delSmsPlatBlacklist/{spbid}")
	@ResponseBody
	public Object delSmsPlatBlacklist(@PathVariable Long spbid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(spbid))) {
				return new ExtReturn(false, "黑名单主键不能为空！");
			}
			if (smsPlatInfoService.deleteSmsPlatBlacklist(spbid)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	//*****************短信平台日志*************************
	
		@RequestMapping(value = "/smslog/index")
		public String indexsmslog(
				@RequestParam(value = "id", required = false) String id,
				HttpServletRequest request, HttpServletResponse response) {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			request.setAttribute("id", id);
			return "manage/smsplatinfo/smsPlatInfo";
		}
		
		@RequestMapping(value = "/smslog/querySmsLoglacklist")
		public @ResponseBody
		Object querySmsLoglacklist(
				@RequestParam(value = "username", required = false) String username,
				@RequestParam(value = "start", required = false) Integer startNo,
				@RequestParam(value = "limit", required = false) Integer pageSize,
				HttpServletRequest request, HttpServletResponse response) {
			try {
				Map<String, Object> params = new HashMap<String, Object>();
				if(StringUtils.isNotBlank(username)){
					params.put("username", username);
				}
				params.put("sortColumns", "smslog.sendtime desc");
				Long count = smsPlatInfoService.querySmsLogCount(params);
				List<SmsPlatSendlog> list = smsPlatInfoService.querySmsLoglist(params, startNo, pageSize);
				return new GridPanel(count, list, true);
			} catch (Exception e) {
				logger.error("查询短信日志异常：",e);
				return new ExceptionReturn(e);
			}
			
		}
}
