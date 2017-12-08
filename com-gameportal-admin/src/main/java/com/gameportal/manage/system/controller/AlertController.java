package com.gameportal.manage.system.controller;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.pay.service.IPayOrderService;
import com.gameportal.manage.util.DateConvertEditor;

/**
 * 提示控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/manage")
public class AlertController {
	
	/**
	 * 查询存款订单对象
	 */
	@Resource(name = "payOrderServiceImpl")
	private IPayOrderService payOrderService = null;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DateConvertEditor());
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
	
	/**
	 * 提示信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "alert")
	@ResponseBody
	public String alert(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		Map<String, Integer> result = payOrderService.getAlertCount();
		if(null == result){
			obj.put("newWithdraw", "0");	//有新的提款
			obj.put("notadd", "0");			//有加款未处理
			obj.put("notdeduct", "0");		//有扣款未处理
			obj.put("recharge", "0");		//有充值未处理
			obj.put("newlogin", "0");		//有玩家登陆
		}else{
			obj.put("newWithdraw", result.get("newWithdraw"));	//有新的提款
			obj.put("notadd", result.get("notadd"));			//有加款未处理
			obj.put("notdeduct", result.get("notdeduct"));		//有扣款未处理
			obj.put("recharge", result.get("recharge"));		//有充值未处理
			obj.put("newlogin", "0");		//有玩家登陆
			
		}
		
		return obj.toString();
	}
}
