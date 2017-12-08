package com.gameportal.manage.sitesettings.controller;

import java.io.PrintWriter;
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
import org.apache.xalan.xsltc.compiler.sym;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.sitesettings.model.WebAdEntity;
import com.gameportal.manage.sitesettings.service.IWebAdService;
import com.gameportal.manage.util.DateUtil2;
import com.gameportal.manage.util.Upload;
import com.gameportal.manage.util.WebConstants;

@Controller
@RequestMapping(value = "/manage/webad")
public class WebAdController {

	public static final Logger logger = Logger
			.getLogger(WebAdController.class);

	@Resource(name = "webAdService")
	private IWebAdService webAdService;
	public WebAdController() {
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
		return "manage/sitesettings/webad";
	}
	
	@RequestMapping(value = "/queryWebAd")
	public @ResponseBody
	Object queryWebAd(
			@RequestParam(value = "adtitle", required = false) String adtitle,
			@RequestParam(value = "pname", required = false) String pname,
			@RequestParam(value = "start", required = false) Integer thisPage,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(adtitle)){
				params.put("adtitle", adtitle);
			}
			Long count = webAdService.getCount(params);
			params.put("sortColumns", "status desc,edittime desc");
			List<WebAdEntity> list = webAdService.getList(params, thisPage, pageSize);
			return new GridPanel(count, list, true);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping(value = "/save")
	@ResponseBody
	public Object save(
			@ModelAttribute WebAdEntity webAdEntity,
			@RequestParam(value = "adid", required = false) String adid,
			HttpServletRequest request, HttpServletResponse response) {
//			String adImages = "";
//			if(!StringUtils.isNotBlank(bid)){
//				try {
//					String path = request.getSession().getServletContext().getRealPath("/");
//					// 转型为MultipartHttpRequest：     
//			        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;     
//			        // 获得文件：     
//			        MultipartFile file = multipartRequest.getFile("photofile");
//			        String fileName = file.getOriginalFilename();
//			        if(!StringUtils.isNotBlank(fileName)){
//			        	return new ExtReturn(true, "请选择您要上传的广告图片！");
//			        }
//			        String suffix = fileName.substring(fileName.indexOf("."));
//			        System.out.println("suffix:"+suffix);
//			        if(!".png".equals(suffix) && !".jpg".equals(suffix) && !".gif".equals(suffix)){
//			        	return new ExtReturn(true, "系统只支持png、jpg、gif图片文件！");
//			        }
//			        adImages = Upload.getMin()+suffix;//从新命名图片名称
//			        Upload.getInstance().saveUpload(file.getInputStream(), adImages, path, "images/");
//				} catch (Exception e) {
//					logger.error("Exception: ", e);
//					return new ExceptionReturn(e);
//				}
//			}
		    try {
				Map<String, Object> params = new HashMap<String, Object>();
				if(StringUtils.isNotBlank(adid)){
					params.put("adid", adid);
					WebAdEntity entity = webAdService.getObject(params);
					if(null == entity){
						return new ExtReturn(false, "您编辑的数据不存在，请刷新后重试！");
					}
					entity = webAdEntity;
					entity.setEdittime(DateUtil2.format2(new Date()));
					logger.info(">>>>>>>>>>>>>>>>>>"+entity.toString());
					if(webAdService.update(entity)){
						return new ExtReturn(true, "编辑网站广告信息成功");
					}else{
						return new ExtReturn(false, "编辑网站广告信息失败。");
					}
				}else{
					WebAdEntity entity = new WebAdEntity();
					entity = webAdEntity;
					entity.setAdid(0L);
					entity.setEdittime(DateUtil2.format2(new Date()));
					logger.info(">>>>>>>>>>>>>>>>>>"+entity.toString());
					if(webAdService.save(entity)){
						return new ExtReturn(true, "新增网站广告信息成功。");
					}else{
						return new ExtReturn(false, "新增网站广告信息失败。");
					}
				}
			} catch (Exception e) {
				logger.error("Exception: ", e);
				return new ExceptionReturn(e);
			}
	}
	
	public static void main(String[] args) {
		String f = "BTN_LuckyWitch1.png";
		System.out.println(f.substring(f.indexOf(".")));
	}
	
	@RequestMapping("/del/{bid}")
	@ResponseBody
	public Object delProxyXimaSet(@PathVariable Long bid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(bid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			if (webAdService.delete(bid)) {
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
