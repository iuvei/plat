package com.gameportal.manage.sitesettings.controller;

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
import com.gameportal.manage.sitesettings.model.WebBulletinEntity;
import com.gameportal.manage.sitesettings.service.IWebBulletinService;
import com.gameportal.manage.util.DateUtil2;

@Controller
@RequestMapping(value = "/manage/wbulletin")
public class WebBulletinController {

	public static final Logger logger = Logger
			.getLogger(WebBulletinController.class);

	@Resource(name = "webBulletinService")
	private IWebBulletinService webBulletinService;
	public WebBulletinController() {
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
		return "manage/sitesettings/webbulletin";
	}
	
	@RequestMapping(value = "/queryWebBulletin")
	public @ResponseBody
	Object queryWebBulletin(
			@RequestParam(value = "btitle", required = false) String btitle,
			@RequestParam(value = "pname", required = false) String pname,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(btitle)){
				params.put("btitle", btitle);
			}
			Long count = webBulletinService.getCount(params);
			params.put("sortColumns", "status desc,edittime desc");
			List<WebBulletinEntity> list = webBulletinService.getList(params, thisPage, pageSize);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping(value = "/save")
	public @ResponseBody
	Object save(
			@RequestParam(value = "bid", required = false) String bid,
			@RequestParam(value = "btitle", required = false) String btitle,
			@RequestParam(value = "bcontext", required = false) String bcontext,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "blocation", required = false) String blocation,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(bid)){
				params.put("bid", bid);
				WebBulletinEntity entity = webBulletinService.getObject(params);
				if(null == entity){
					return new ExtReturn(false, "您编辑的数据不存在，请刷新后重试！");
				}
				entity.setBcontext(bcontext);
				entity.setBlocation(blocation);
				entity.setBtitle(btitle);
				entity.setEdittime(DateUtil2.format2(new Date()));
				entity.setStatus(status);
				if(webBulletinService.update(entity)){
					return new ExtReturn(true, "编辑公告信息成功。");
				}else{
					return new ExtReturn(false, "编辑公告信息失败。");
				}
			}else{
				WebBulletinEntity entity = new WebBulletinEntity();
				entity.setBid(0L);
				entity.setBcontext(bcontext);
				entity.setStatus(status);
				entity.setBtitle(btitle);
				entity.setBlocation(blocation);
				entity.setEdittime(DateUtil2.format2(new Date()));
				if(webBulletinService.save(entity)){
					return new ExtReturn(true, "添加公告信息成功。");
				}else{
					return new ExtReturn(false, "添加公告信息失败。");
				}
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping("/del/{bid}")
	@ResponseBody
	public Object delProxyXimaSet(@PathVariable Long bid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(bid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			if (webBulletinService.delete(bid)) {
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
