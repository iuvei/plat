package com.gameportal.web.home.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 活动控制器
 * @author Administrator
 *
 */
@Controller
public class ActivityController{

	/**
	 * 活动控制器
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/favo")
	public String index(HttpServletRequest request, HttpServletResponse response){
		return "/activity/index";
	}
}
