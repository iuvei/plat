package com.gameportal.manage.gameplatform.controller;

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

import com.gameportal.manage.gameplatform.model.GamePlatform;
import com.gameportal.manage.gameplatform.service.IGamePlatformService;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;

/**
 * @ClassName: GamePlatformController
 * @Description: TODO(游戏平台控制类)
 * @author shejia@gz-mstc.com
 * @date 2014-4-10 下午2:51:41
 */
@Controller
@RequestMapping(value = "/manage/gameplatform")
public class GamePlatformController {
	@Resource(name = "gamePlatformServiceImpl")
	private IGamePlatformService iGamePlatformService = null;
	public static final Logger logger = Logger
			.getLogger(GamePlatformController.class);

	public GamePlatformController() {
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
		return "manage/gameplatform/gamePlatform";
	}

	@RequestMapping(value = "/queryGamePlatform")
	public @ResponseBody
	Object queryGamePlatform(
			@RequestParam(value = "gpid", required = false) Long gpid,
			@RequestParam(value = "gpname", required = false) String gpname,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Long count = iGamePlatformService.queryGamePlatformCount(gpid, gpname);
		List<GamePlatform> list = iGamePlatformService.queryGamePlatform(gpid, gpname, startNo, pageSize);
		return new GridPanel(count, list, true);
	}
	
	@RequestMapping(value = "/saveGamePlatform")
	@ResponseBody
	public Object saveGamePlatform(@ModelAttribute GamePlatform gamePlatform,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Date date = new Date();
			if (!StringUtils.isNotBlank(ObjectUtils.toString(gamePlatform))) {
				return new ExtReturn(false, "游戏平台不能为空！");
			}
			if (!StringUtils.isNotBlank(gamePlatform.getGpname())) {
				return new ExtReturn(false, "游戏平台名称不能为空！");
			}
			
			if (StringUtils.isNotBlank(ObjectUtils.toString(gamePlatform
					.getGpid()))) {
				gamePlatform.setUpdateDate(date);
			} else {
				gamePlatform.setStatus(1);
				gamePlatform.setCreateDate(date);
				gamePlatform.setUpdateDate(date);
			}
			if (iGamePlatformService.saveOrUpdateGamePlatform(gamePlatform)) {
				return new ExtReturn(true, "保存成功！");
			} else {
				return new ExtReturn(false, "保存失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping("/delGamePlatform/{gpid}")
	@ResponseBody
	public Object delGamePlatform(@PathVariable Long gpid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(gpid))) {
				return new ExtReturn(false, "游戏平台主键不能为空！");
			}
			if (iGamePlatformService.deleteGamePlatform(gpid)) {
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
