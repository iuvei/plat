package com.gameportal.manage.payplatform.controller;

import java.util.Date;
import java.util.List;

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

import com.gameportal.manage.payplatform.model.PayPlatform;
import com.gameportal.manage.payplatform.service.IPayPlatformService;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;

/**
 * @ClassName: PayPlatformController
 * @Description: TODO(游戏平台控制类)
 * @author shejia@gz-mstc.com
 * @date 2014-4-10 下午2:51:41
 */
@Controller
@RequestMapping(value = "/manage/payplatform")
public class PayPlatformController {
	@Resource(name = "payPlatformServiceImpl")
	private IPayPlatformService iPayPlatformService = null;
	public static final Logger logger = Logger
			.getLogger(PayPlatformController.class);

	public PayPlatformController() {
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
		return "manage/payplatform/payPlatform";
	}

	@RequestMapping(value = "/queryPayPlatform")
	public @ResponseBody
	Object queryPayPlatform(
			@RequestParam(value = "ppid", required = false) Long ppid,
			@RequestParam(value = "pname", required = false) String pname,
			@RequestParam(value = "start", required = false) Integer startNo,String channelType,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Long count = iPayPlatformService.queryPayPlatformCount(ppid, pname,channelType);
		List<PayPlatform> list = iPayPlatformService.queryPayPlatform(ppid, pname, channelType,startNo, pageSize);
		return new GridPanel(count, list, true);
	}
	
	@RequestMapping(value = "/savePayPlatform")
	@ResponseBody
	public Object savePayPlatform(@ModelAttribute PayPlatform payPlatform,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Date date = new Date();
			if (!StringUtils.isNotBlank(ObjectUtils.toString(payPlatform))) {
				return new ExtReturn(false, "支付平台不能为空！");
			}
			if (!StringUtils.isNotBlank(payPlatform.getPname())) {
				return new ExtReturn(false, "支付平台名称不能为空！");
			}
			
			if (StringUtils.isNotBlank(ObjectUtils.toString(payPlatform
					.getPpid()))) {
				payPlatform.setUpdateDate(date);
			} else {
				payPlatform.setStatus(1);
				payPlatform.setCreateDate(date);
				payPlatform.setUpdateDate(date);
			}
			if (iPayPlatformService.saveOrUpdatePayPlatform(payPlatform)) {
				return new ExtReturn(true, "保存成功！");
			} else {
				return new ExtReturn(false, "保存失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping("/delPayPlatform/{ppid}")
	@ResponseBody
	public Object delPayPlatform(@PathVariable Long ppid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(ppid))) {
				return new ExtReturn(false, "支付平台主键不能为空！");
			}
			if (iPayPlatformService.deletePayPlatform(ppid)) {
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
