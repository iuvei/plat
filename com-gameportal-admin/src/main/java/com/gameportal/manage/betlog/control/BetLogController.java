package com.gameportal.manage.betlog.control;

import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.betlog.model.BetLog;
import com.gameportal.manage.betlog.model.BetLogTotal;
import com.gameportal.manage.betlog.service.IBetLogService;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.util.DateUtil2;
import com.gameportal.manage.util.ExcelUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/manage/betlog")
public class BetLogController {
	@Resource(name = "betLogServiceImpl")
	private IBetLogService betLogService = null;
	public static final Logger logger = Logger
			.getLogger(BetLogController.class);

	public BetLogController() {
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
		String today = DateUtil2.convert2Str(new Date(), "yyyy-MM-dd")+" 23:59:59";
		request.setAttribute("today", today);
		return "manage/betlog/betlog";
	}
	
	@RequestMapping(value = "/queryBetLog")
	public @ResponseBody
	Object queryBetLog(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "uiid", required = false) Integer uiid,
			@RequestParam(value = "gamecode", required = false) String gamecode,
			@RequestParam(value = "gamename", required = false) String gamename,
			@RequestParam(value = "betno", required = false) String betno,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		    Map<String,Object> map=new HashMap<String,Object>();
		   
		   if(!StringUtils.isBlank(startDate)){
			   map.put("startDate", startDate);
		   }else{
			   map.put("startDate", DateUtil2.format(new Date()) + " 00:00:00");
		   }
		   if(!StringUtils.isBlank(endDate)){
			   map.put("endDate", endDate);
		   }else{
			   map.put("endDate", DateUtil2.format(new Date()) + " 23:59:59");
		   }
		   if(!StringUtils.isBlank(account)){
			   map.put("account", account);
		   }
		   if(!StringUtils.isBlank(gamecode)){
			   map.put("gamecode", gamecode);
		   }
		   if(StringUtils.isNotEmpty(betno)){
			   map.put("betno", betno);
		   }
		   if(!StringUtils.isBlank(gamename)){
			   map.put("platformcode", "("+gamename+")");
		   }
		   map.put("sortColumns", "betdate DESC");
		Long count = betLogService.queryForCount(map);
		List<BetLog> list = betLogService.queryForList(map, startNo, pageSize);
		return new GridPanel(count, list, true);
	}
	
	/**
	 * 注单统计页
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/indexcount")
	public String indexcount(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		String today = DateUtil2.convert2Str(new Date(), "yyyy-MM-dd")+" 23:59:59";
		JSONObject json = new JSONObject();
		json.put("", "全部");
		json.put("0", "不洗码");
		json.put("1", "可洗码");
		request.setAttribute("ximaFlag", json.toString());
		request.setAttribute("today", today);
		return "manage/betlog/betlogCount";
	}
	
	@RequestMapping(value = "/queryBetCountLog")
	public @ResponseBody
	Object queryBetCountLog(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "uiid", required = false) Integer uiid,
			@RequestParam(value = "gamecode", required = false) String gamecode,
			@RequestParam(value = "platformcode", required = false) String gamename,
			@RequestParam(value = "flag", required = false) String flag,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
			Map<String,Object> map=new HashMap<String,Object>();
			 Date date = new Date();
		   if(!StringUtils.isBlank(startDate)){
			   map.put("starttime", startDate);
		   }else{
			   map.put("starttime", DateUtil2.format(date) + " 00:00:00");
		   }
		   if(!StringUtils.isBlank(endDate)){
			   map.put("endtime", endDate);
		   }else{
			   map.put("endtime", DateUtil2.format(date) + " 23:59:59");
		   }
		   if(!StringUtils.isBlank(account)){
			   map.put("account", account);
		   }
		   if(!StringUtils.isBlank(gamecode)){
			   map.put("gamecode", gamecode);
		   }
		   if(!StringUtils.isBlank(gamename)){
			   map.put("platformcode", "("+gamename+")");
		   }
		   if(StringUtils.isNotEmpty(flag)){
			   map.put("flag", flag);
		   }
		   Long count = betLogService.selectBetTotalCount(map);
		   List<BetLogTotal> list = betLogService.selectBetTotal(map, startNo, pageSize);
		   return new GridPanel(count, list, true);
	}
	
	/**
	 * 游戏注单统计导出
	 * @param request   请求对象
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param response  响应对象
	 * @throws Exception
	 */
    @RequestMapping(value="/toDownloadReport")
    @ResponseBody
    public void toDownloadData(HttpServletRequest request,
    						   HttpServletResponse response,			
    						   @RequestParam(value = "startDate", required = false) String startDate,
    						   @RequestParam(value = "endDate", required = false) String endDate,
    						   @RequestParam(value = "account", required = false) String account,
    						   @RequestParam(value = "uiid", required = false) Integer uiid,
    						   @RequestParam(value = "gamecode", required = false) String gamecode,
    						   @RequestParam(value = "flag", required = false) String flag,
    						   @RequestParam(value = "platformcode", required = false) String gamename) throws Exception {
        OutputStream fOut = null;
        try {
    	   Map<String,Object> map=new HashMap<String,Object>();
		   Date date = new Date();
		   if(!StringUtils.isBlank(startDate)){
			   map.put("starttime", startDate);
		   }else{
			   map.put("starttime", DateUtil2.format(date) + " 00:00:00");
		   }
		   if(!StringUtils.isBlank(endDate)){
			   map.put("endtime", endDate);
		   }else{
			   map.put("endtime", DateUtil2.format(date) + " 23:59:59");
		   }
		   if(!StringUtils.isBlank(account)){
			   map.put("account", account);
		   }
		   if(!StringUtils.isBlank(gamecode)){
			   map.put("gamecode", gamecode);
		   }
		   if(StringUtils.isNotEmpty(flag)){
			   map.put("flag", flag);
		   }
		   if(!StringUtils.isBlank(gamename)){
			   map.put("platformcode", "("+gamename+")");
		   }
   		   //----------------------------------------------------
           response.setContentType("application/vnd.ms-excel;charset=UTF-8");//设置类型
           String  fileName = "betLog.xls";
           // 清空response
           response.reset();
           // 设置response的Header
           response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            //数据源
            List<BetLogTotal> betLogTotalList=betLogService.selectBetTotalForReport(map);
            //excel表头
            LinkedHashMap<String, String> viewMap = new LinkedHashMap<String, String>();
            viewMap.put("platformcode", "游戏平台");
            viewMap.put("gamename", "游戏名称");
            viewMap.put("account", "会员账号");
            viewMap.put("uname", "会员名称");
            viewMap.put("betTotel", "注单数量");
            viewMap.put("betAmountTotal", "投注金额");
            viewMap.put("validBetAmountTotal", "有效投注额");
            viewMap.put("profitamountTotal", "派彩金额");
            viewMap.put("betdate", "日期");
            Workbook workBook = ExcelUtil.generateSingleWorkBook(fileName.toString(),betLogTotalList, viewMap);
            // 以流的形式下载文件=
            fOut = response.getOutputStream();
            workBook.write(fOut);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fOut != null) {
                fOut.flush();
                fOut.close();
            }
        }
    }

}
