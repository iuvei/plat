package com.gameportal.manage.mail.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.mail.model.EmailLog;
import com.gameportal.manage.mail.service.IEmailLogService;
import com.gameportal.manage.member.controller.MemberInfoController;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.util.WebConstants;
import com.gameportal.portal.mail.model.MailSenderInfo;

@Controller
@RequestMapping(value = "/manage/email")
public class EmailController {
	
	public static final Logger logger = Logger.getLogger(MemberInfoController.class);

	@Resource(name = "emailLogService")
	private IEmailLogService emailLogService;
	
	@Resource(name = "redisServiceImpl")
	private IRedisService redisService=null;
	
	/**
	 * 待发送邮件数据列表
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		return "manage/mail/waitlist";
	}
	
	/**
	 * 获取邮件列表
	 * @param startNo
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/querySendlist")
	public @ResponseBody
	Object querySendList(
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			List<String>  redisList = redisService.getKeys("*"+WebConstants.E_MAIL_QUEUE);
			logger.info("邮件rediskey列表："+redisList);
			List<MailSenderInfo> list = null;
			long count = 0;
			if(null != redisList && redisList.size() > 0){
				count = redisList.size();
				list = new ArrayList<MailSenderInfo>();
				for(String str : redisList){
					logger.info("邮件发送key->"+str);
					Class<Object> c = null;
					MailSenderInfo mailSenderInfo = (MailSenderInfo)redisService.getRedisResult(str, c);
					if (StringUtils.isNotBlank(ObjectUtils.toString(mailSenderInfo))) {
						mailSenderInfo.setRediskey(str);
						list.add(mailSenderInfo);
					}
				}
			}
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping("/deleteWaitEmail")
	@ResponseBody
	public Object deleteWaitEmail(@RequestParam(value = "key", required = false) String key,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(key))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			if(redisService.keyExists(key)){
				if(redisService.delete(key)){
					return new ExtReturn(true, "删除待发送邮件成功！");
				}else{
					return new ExtReturn(false, "删除待发送邮件失败！");
				}
			}else{
				return new ExtReturn(true, "该待发送邮件已被删除！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	
	/*****************************查询全部邮件*************************/
	@RequestMapping(value = "/allindex")
	public String allindex(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		JSONObject obj = new JSONObject();
		obj.put("", "全部");
		obj.put("0", "待发送");
		obj.put("1", "验证成功 ");
		obj.put("2", "发送失败");
		obj.put("3", "待验证");
		obj.put("4", "验证失败");
		request.setAttribute("emailstatus", obj.toString());
		return "manage/mail/alllist";
	}
	
	/**
	 * 获取所有邮件数据
	 * @param startNo
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryAll")
	public @ResponseBody
	Object queryAll(
			@RequestParam(value = "host", required = false) String serverhost,
			@RequestParam(value = "fromAddress", required = false) String fromAddress,
			@RequestParam(value = "toAddress", required = false) String toAddress,
			@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(serverhost)){
				params.put("mailServerHost", serverhost);
			}
			if(StringUtils.isNotBlank(fromAddress)){
				params.put("fromAddress", fromAddress);
			}
			if(StringUtils.isNotBlank(toAddress)){
				params.put("toAddress", toAddress);
			}
			if(StringUtils.isNotBlank(starttime)){
				params.put("starttime", starttime);
			}
			if(StringUtils.isNotBlank(endtime)){
				params.put("endtime", endtime);
			}
			if(StringUtils.isNotBlank(ObjectUtils.toString(status))){
				params.put("status", status);
			}
			params.put("sortColumns", "createDate desc");
			Long count = emailLogService.getCount(params);
			List<EmailLog> list = emailLogService.getlList(params, startNo==null?0:startNo, pageSize==null?30:pageSize);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam(value = "mailid", required = false) Long key,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(key))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			
			if(emailLogService.delete(key)){
				return new ExtReturn(true, "删除邮件数据成功！");
			}else{
				return new ExtReturn(false, "删除邮件数据失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
}
