package com.gameportal.manage.member.controller;

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

import com.gameportal.manage.member.model.MemberGrade;
import com.gameportal.manage.member.service.IMemberGradeService;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.redis.service.IRedisService;

/**
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/manage/memberGrade")
public class MemberGradeController {
	@Resource(name = "memberGradeServiceImpl")
	private IMemberGradeService memberGradeService = null;
	
	@Resource(name = "redisServiceImpl")
	private IRedisService redisService=null;
	public static final Logger logger = Logger
			.getLogger(MemberGradeController.class);

	public MemberGradeController() {
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
		return "manage/member/memberGrade";
	}

	@RequestMapping(value = "/queryMemberGrade")
	public @ResponseBody
	Object queryMemberGrade(
			@ModelAttribute MemberGrade memberGrade,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try{
				Map<String,Object> map=new HashMap<String,Object>();
				Long count = memberGradeService.queryMemberGradeCount(map);
				List<MemberGrade> list = memberGradeService.queryMemberGrade(map, startNo==null?0:startNo, pageSize==null?20:pageSize);
				return new GridPanel(count, list, true);
			  
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping(value = "/saveMemberGrade")
	@ResponseBody
	public Object saveMemberGrade(
			@ModelAttribute MemberGrade memberGrade,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			
			if (memberGradeService.saveMemberGrade(memberGrade)) {
				return new ExtReturn(true, "保存成功！");
			} else {
				return new ExtReturn(false, "保存失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping("/delMemberGrade/{uiid}")
	@ResponseBody
	public Object delMemberGrade(@PathVariable Long uiid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(uiid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			if (memberGradeService.deleteMemberGrade(uiid)) {
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
