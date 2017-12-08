package com.gameportal.manage.proxy.controller;

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

import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.sitesettings.model.WebProxyApply;
import com.gameportal.manage.sitesettings.service.IWebProxyApplyService;
import com.gameportal.manage.util.DateUtil2;

/**
 * 代理申请记录
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/manage/proxyapply")
public class WebProxyApplyController {

	public static final Logger logger = Logger
			.getLogger(WebProxyApplyController.class);

	@Resource(name = "webProxyApplyService")
	private IWebProxyApplyService webProxyApplyService;
	public WebProxyApplyController() {
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
		return "manage/proxy/webproxyapply";
	}
	
	@RequestMapping(value = "/queryWebProxyApply")
	public @ResponseBody
	Object queryWebProxyApply(
			@RequestParam(value = "truename", required = false) String truename,
			@RequestParam(value = "pname", required = false) String pname,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(truename)){
				params.put("truename", truename);
			}
			Long count = webProxyApplyService.getCount(params);
			params.put("sortColumns", "applytime DESC");
			List<WebProxyApply> list = webProxyApplyService.getList(params, thisPage, pageSize);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping(value = "/save")
	public @ResponseBody
	Object save(
			@RequestParam(value = "aid", required = false) Long aid,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "status", required = false) Integer status,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if(!StringUtils.isNotBlank(ObjectUtils.toString(aid))){
				return new ExtReturn(false, "必填参数不能为空。");
			}
			if(status == 0){
				return new ExtReturn(false, "请选择审核状态，不能选择待审核状态。");
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("aid",aid);
			WebProxyApply entity = webProxyApplyService.getObject(params);
			if(entity == null){
				return new ExtReturn(false, "您审核的代理申请数据不存在，请刷新后重试。");
			}
			entity.setDescription(description);
			entity.setStatus(status);
			entity.setApplytime(DateUtil2.format2(new Date()));
			if(webProxyApplyService.update(entity)){
				return new ExtReturn(true, "审核成功。");
			}else{
				return new ExtReturn(false, "审核失败。");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping("/del/{aid}")
	@ResponseBody
	public Object delProxyXimaSet(@PathVariable Long aid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(aid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			if (webProxyApplyService.delete(aid)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
}
