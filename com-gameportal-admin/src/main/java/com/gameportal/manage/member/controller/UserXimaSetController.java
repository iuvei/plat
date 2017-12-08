package com.gameportal.manage.member.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.member.model.MemberInfo;
import com.gameportal.manage.member.model.UserXimaSet;
import com.gameportal.manage.member.service.IMemberInfoService;
import com.gameportal.manage.member.service.IUserXimaSetService;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.proxy.model.ProxySet;
import com.gameportal.manage.util.DateUtil2;

/**
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/manage/uxmset")
public class UserXimaSetController {
	
	public static final Logger logger = Logger.getLogger(UserXimaSetController.class);

	@Resource(name = "userXimaSetService")
	private IUserXimaSetService userXimaSetService;
	
	@Resource(name = "memberInfoServiceImpl")
	private IMemberInfoService memberInfoService = null;
	
	public UserXimaSetController() {
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
		return "manage/xima/userximaset";
	}
	
	@RequestMapping(value = "/queryUserXimaSet")
	public @ResponseBody
	Object queryUserXimaSet(
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(account)){
				params.put("account", account);
			}
			params.put("sortColumns", "cs.settime DESC");
			Long count = userXimaSetService.count(params);
			List<UserXimaSet> list = userXimaSetService.getList(params, startNo==null?0:startNo, pageSize==null?20:pageSize);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping(value = "/save")
	public @ResponseBody
	Object save(
			@RequestParam(value = "xmid", required = false) String xmid,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "ximascale", required = false) String ximascale,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(xmid)){
				params.put("xmid", xmid);
				UserXimaSet userXimaSet = userXimaSetService.getObject(params);
				userXimaSet.setXimascale(ximascale);
				userXimaSet.setSettime(DateUtil2.format2(new Date()));
				if(userXimaSetService.update(userXimaSet)){
					return new ExtReturn(true, "更新用户洗码比例成功。");
				}else{
					return new ExtReturn(false, "更新用户洗码比例失败。");
				}
			}else{
				params.put("account", account);
				MemberInfo memberInfo = memberInfoService.qeuryMemberInfo(params);
				if(null == memberInfo){
					return new ExtReturn(false, "您要添加的用户不存在，请检查后重新操作。");
				}
				params.clear();
				params.put("uiid", memberInfo.getUiid());
				if(null != userXimaSetService.getObject(params)){
					return new ExtReturn(false, "您要添加的用户已经存在。");
				}
				UserXimaSet userXimaSet = new UserXimaSet();
				userXimaSet.setUiid(memberInfo.getUiid().intValue());
				userXimaSet.setProxyid(0);
				userXimaSet.setStatus(1);
				userXimaSet.setSettime(DateUtil2.format2(new Date()));
				userXimaSet.setXimascale(ximascale);
				if(userXimaSetService.save(userXimaSet)){
					return new ExtReturn(true, "添加用户洗码比例成功。");
				}else{
					return new ExtReturn(false, "添加用户洗码比例失败。");
				}
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping("/binduserximaset/{id}/{status}")
	@ResponseBody
	public Object binduserximaset(@PathVariable Long id,@PathVariable Integer status) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(id))) {
				return new ExtReturn(false, "会员洗码数据主键不能为空！");
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("xmid", id);
			UserXimaSet entity = userXimaSetService.getObject(params);
			if(null == entity || "".equals(entity)){
				return new ExtReturn(false, "您要修改的会员洗码数据不存在，请刷新后重试！");
			}
			if(status==1){
				entity.setStatus(0);
			}else{
				entity.setStatus(1);
			}
			if (userXimaSetService.update(entity)) {
				return new ExtReturn(true, "修改会员洗码数据状态成功！");
			} else {
				return new ExtReturn(false, "修改会员洗码数据状态失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping("/deluserximaset/{id}")
	@ResponseBody
	public Object deluserximaset(@PathVariable Long id) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(id))) {
				return new ExtReturn(false, "会员洗码数据主键不能为空！");
			}
			if (userXimaSetService.delete(id)) {
				return new ExtReturn(true, "删除会员洗码数据成功！");
			} else {
				return new ExtReturn(false, "删除会员洗码数据失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
}
