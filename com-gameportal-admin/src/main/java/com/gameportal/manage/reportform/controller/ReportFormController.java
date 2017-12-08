package com.gameportal.manage.reportform.controller;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.reportform.model.ReportForm;
import com.gameportal.manage.reportform.service.IReportFormService;
import com.gameportal.manage.util.DateUtil;
import com.gameportal.manage.util.ExcelUtil;
/**
 * 报表统计控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/manage/reportForm")
public class ReportFormController {
	
	@Resource(name="reportFormService")
	private IReportFormService reportFormService; 
	
	public static final Logger logger = Logger
			.getLogger(ReportFormController.class);

	public ReportFormController(){
		super();
	}
	
	/**
	 * 跳转页面
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
		return "manage/reportform/reportForm";
	}
	
	/**
	 * 平台报表统计
	 * @param startDate  开始时间
	 * @param endDate	   截止时间
	 * @param startNo	   当前页数
	 * @param pageSize   每页数量
	 * @return
	 */
	@RequestMapping(value = "/queryReportResult")
	@ResponseBody
	public Object getReportResult(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize){
		
		Map<String,Object> map=new HashMap<String,Object>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isEmpty(startDate)&&StringUtils.isEmpty(endDate)){  //默认查询前5天的数据
	        Calendar c = Calendar.getInstance();  
	        c.add(Calendar.DATE, - 5);  
	        Date monday = c.getTime();
	        startDate = sdf.format(monday);
	        endDate=sdf.format(new Date());
		}
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		Long count = reportFormService.getReportCount(map);  //报表统计总数
		List<ReportForm> list= reportFormService.getReportResult(map,startNo,pageSize);//报表统计分页集合
		Long start=System.currentTimeMillis();
		for (ReportForm reportForm : list) { 
			//统计当前首冲总金额
			String firstPayMoney=reportFormService.getFirstPayMoney(reportForm.getReporttime());
			if(firstPayMoney==null){
				firstPayMoney="0.00";
			}
			reportForm.setFirstPayTotalMoney(firstPayMoney);
			//统计当前有效投注额
			String realbetMoney=reportFormService.getRealBetMoney(reportForm.getReporttime());
			if(realbetMoney==null){
				realbetMoney="0.00";
			}
			reportForm.setRealBetMoney(realbetMoney);
		}
		Long end=System.currentTimeMillis();
		System.out.println("运行时间:"+(end-start)+"毫秒");
		return new GridPanel(count, list, true);
	}

	/**
	 * 导出指定时间的报表
	 * @param request   请求对象
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param response  响应对象
	 * @throws Exception
	 */
    @RequestMapping(value="toDownloadReport")
    @ResponseBody
    public void toDownloadData(
    		@RequestParam(value = "reportStartDate", required = false) String reportStartDate,
    		@RequestParam(value = "reportEndDate", required = false) String reportEndDate,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
    	logger.info("报表导出开始->1");
        OutputStream fOut = null;
        try {
        	Map<String,Object> map=new HashMap<String,Object>();
    		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    		if(StringUtils.isEmpty(reportStartDate)&&StringUtils.isEmpty(reportEndDate)){  //默认查询前7天的数据
    	        Calendar c = Calendar.getInstance();  
    	        c.add(Calendar.DATE, - 7);  
    	        Date monday = c.getTime();
    	        reportStartDate = sdf.format(monday);
    	        reportEndDate=sdf.format(new Date());
    		}
    		map.put("startDate", reportStartDate);
    		map.put("endDate", reportEndDate);
            //response.setContentType("application/octet-stream");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");//设置类型
            String  fileName = reportStartDate+"-"+reportEndDate+"-ReportFormData.xls";
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            logger.info("报表导出开始----------------------------------------------------------------------------------------->2");
         
            List<ReportForm> reportList=reportFormService.getReportResult(map);
            if(reportList!=null && reportList.size()>0){
            	ReportForm report=new ReportForm();   //添加总计
            	Integer registerNumber=0;//注册人数
                Integer firstPayNumber=0;//首冲人数
                //Long activeNumber=0l;//注册人数
                //Long loginNumber=0l;//登录人数
            	double firstPayTotalMoney=0.00; //首冲总金额
            	double realBetMoney=0.00;//实际投注额
            	double memberCoupon=0.00;//会员优惠
            	double proxyCoupon=0.00;//代理优惠
            	double payMoney=0.00; //存款金额
            	Integer payMoneyPerson=0;//充值人数
            	Integer payMoneyCount=0;//充值笔数
            	double pickUpMoney=0.00;//提款金额
            	Integer pickUpMoneyPerson=0;//提款人数
            	Integer pickUpMoneyCount=0;//提款笔数
            	double memberXimaMoney=0.00;//会员洗码金额
            	double proxyXimaMoney=0.00;//代理洗码金额
            	double proxyClearMoney=0.00;//代理佣金结算金额
            	double payOrderXimaMoney=0.00;//手动洗码金额
            	for (ReportForm reportForm : reportList) {
        			String firstPayMoney=reportFormService.getFirstPayMoney(reportForm.getReporttime());
        			//统计首冲金额
        			if(firstPayMoney==null){
        				firstPayMoney="0.00";
        			}
        			reportForm.setFirstPayTotalMoney(firstPayMoney);
        			//统计当前有效投注额
        			String realbetMoney=reportFormService.getRealBetMoney(reportForm.getReporttime());
        			if(realbetMoney==null){
        				realbetMoney="0.00";
        			}
        			map.clear();
    				map.put("startDate", reportForm.getReporttime());
    				map.put("endDate", reportForm.getReporttime());
    				// 活跃人数(有玩过游戏的会员人数)
    				//long activeCount = reportFormService.selectActiveCount(map);
    				//reportForm.setActiveCount(activeCount);
    				
    				// 查询正常登录人数
    				//long loginCount = reportFormService.selectLoginNumberCount(map);
    				//reportForm.setLoginCount(loginCount);
    				
        			reportForm.setRealBetMoney(realbetMoney);
            		registerNumber+=reportForm.getRegisterNumber();
            		firstPayNumber+=reportForm.getFirstPayNumber();
            		//activeNumber += reportForm.getActiveCount();
            		//loginNumber +=reportForm.getLoginCount();
            		payMoneyPerson+=reportForm.getPayMoneyPerson();
            		payMoneyCount+=reportForm.getPayMoneyCount();
            		pickUpMoneyPerson+=reportForm.getPickUpMoneyPerson();
            		pickUpMoneyCount+=reportForm.getPickUpMoneyCount();
            		firstPayTotalMoney+=Double.valueOf(firstPayMoney);
            		realBetMoney+=Double.valueOf(reportForm.getRealBetMoney());
            		memberCoupon+=Double.valueOf(reportForm.getMemberCoupon());
            		proxyCoupon+=Double.valueOf(reportForm.getProxyCoupon());
            		payMoney+=Double.valueOf(reportForm.getPayMoney());
            		pickUpMoney+=Double.valueOf(reportForm.getPickUpMoney());
            		memberXimaMoney+=Double.valueOf(reportForm.getMemberXimaMoney()==null?"0":reportForm.getMemberXimaMoney());
            		proxyXimaMoney+=Double.valueOf(reportForm.getProxyXimaMoney());
            		proxyClearMoney+=Double.valueOf(reportForm.getProxyClearMoney());
            		payOrderXimaMoney+=Double.valueOf(reportForm.getPayOrderXimaMoney());
    			}
            	report.setReporttime("总计:");
            	report.setRegisterNumber(registerNumber);
            	report.setFirstPayNumber(firstPayNumber);
            	//report.setActiveCount(activeNumber);
            	//report.setLoginCount(loginNumber);
            	report.setFirstPayTotalMoney(""+firstPayTotalMoney);
            	report.setRealBetMoney(""+realBetMoney);
            	report.setMemberCoupon(""+memberCoupon);
            	report.setProxyCoupon(""+proxyCoupon);
            	report.setPayMoney(""+payMoney);
            	report.setPickUpMoney(""+pickUpMoney);
        		report.setMemberXimaMoney(""+memberXimaMoney);
        		report.setProxyXimaMoney(""+proxyXimaMoney);
        		report.setProxyClearMoney(""+proxyClearMoney);
        		report.setPayOrderXimaMoney(""+payOrderXimaMoney);
        		report.setPayMoneyPerson(payMoneyPerson);
        		report.setPayMoneyCount(payMoneyCount);
        		report.setPickUpMoneyPerson(pickUpMoneyPerson);
        		report.setPickUpMoneyCount(pickUpMoneyCount);
            	report.setRegisterNumber(registerNumber);
            	report.setFirstPayNumber(firstPayNumber);
            	report.setRegisterNumber(registerNumber);
            	report.setFirstPayNumber(firstPayNumber);
            	reportList.add(report);
            }
            //excel表头
            LinkedHashMap<String, String> viewMap = new LinkedHashMap<String, String>();
            viewMap.put("reporttime", "日期");
            viewMap.put("registerNumber", "注册人数");
            viewMap.put("firstPayNumber", "首冲人数");
            viewMap.put("activeCount", "投注人数人数");
            viewMap.put("loginCount", "登录人数");
            viewMap.put("firstPayTotalMoney", "首冲总金额");
            viewMap.put("realBetMoney", "实际投注额");
            viewMap.put("memberCoupon", "会员优惠");
            viewMap.put("proxyCoupon", "代理优惠");
            viewMap.put("memberXimaMoney", "会员洗码金额");
            viewMap.put("proxyXimaMoney", "代理洗码金额");
            viewMap.put("proxyClearMoney", "代理佣金结算金额");
            viewMap.put("payOrderXimaMoney", "手动洗码金额");
            viewMap.put("payMoney", "充值金额");
            viewMap.put("payMoneyPerson", "充值人数");
            viewMap.put("payMoneyCount", "充值笔数");
            viewMap.put("pickUpMoney", "提款金额");
            viewMap.put("pickUpMoneyPerson", "提款人数");
            viewMap.put("pickUpMoneyCount", "提款笔数");
            String path = request.getSession().getServletContext().getRealPath("/")+"uploadFiles/";
            Workbook workBook = ExcelUtil.generateSingleWorkBook(fileName.toString(),reportList, viewMap);
            // 以流的形式下载文件=
            //fOut = new FileOutputStream(new File(path+fileName.toString()));
            fOut = response.getOutputStream();
            workBook.write(fOut);
        } catch (Exception e) {
            logger.error("每日报表导出失败："+e.getMessage(), e);
        } finally {
            if (fOut != null) {
                fOut.flush();
                fOut.close();
            }
        }
    }
    
	
	/**
	 * 注册人数统计
	 * @param nowDate    当天时间
	 * @param startNo	   当前页数
	 * @param pageSize   每页数量
	 * @return
	 */
	@RequestMapping(value = "/queryRegisterResult")
	@ResponseBody
	public Object getRegisterResult(
			@RequestParam(value = "reportdate", required = false) String nowDate,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize){
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("nowDate", nowDate);
		Long count = reportFormService.getRegisterNumberCount(map);
		List<Map<String, Object>> list= reportFormService.getRegisterNumberResult(map,startNo,pageSize);
		return new GridPanel(count, list, true);
	}
	
	/**
	 * 首冲人数统计
	 * @param nowDate    当天时间
	 * @param startNo	   当前页数
	 * @param pageSize   每页数量
	 * @return
	 */
	@RequestMapping(value = "/queryFirstPayResult")
	@ResponseBody
	public Object getFirstPayResult(
			@RequestParam(value = "reportdate", required = false) String nowDate,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize){
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("nowDate", nowDate);
		Long count = reportFormService.getFirstPayNumberCount(map);
		List<Map<String, Object>> list= reportFormService.getFirstPayNumberResult(map,startNo,pageSize);
		return new GridPanel(count, list, true);
	}
	
	
	/**
	 * 实际投注额统计
	 * @param nowDate    当天时间
	 * @param startNo	   当前页数
	 * @param pageSize   每页数量
	 * @return
	 */
	@RequestMapping(value = "/queryBetMoneyResult")
	@ResponseBody
	public Object getBetMoneyResult(
			@RequestParam(value = "reportdate", required = false) String nowDate,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("nowDate", nowDate);
		List<Map<String, Object>> list= reportFormService.getPlatformBetStats(map,startNo,pageSize);
		if(list == null || list.size() == 0){
			list= reportFormService.getBetMoneyResult(map,startNo,pageSize);
		}
//		Long count = reportFormService.getBetMoneyCount(map);
		return new GridPanel(7L, list, true);
	}
	
	/**
	 * 会员优惠统计
	 * @param nowDate    当天时间
	 * @param startNo	   当前页数
	 * @param pageSize   每页数量
	 * @return
	 */
	@RequestMapping(value = "/queryMemberCouponResult")
	@ResponseBody
	public Object getMemberCouponResult(
			@RequestParam(value = "reportdate", required = false) String nowDate,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize){
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("nowDate", nowDate);
		map.put("accountType", 0);
		Long count = reportFormService.getMemberCouponCount(map);
		List<Map<String, Object>> list= reportFormService.getMemberCouponResult(map,startNo,pageSize);
		return new GridPanel(count, list, true);
	}
	
	/**
	 * 代理优惠统计
	 * @param nowDate    当天时间
	 * @param startNo	   当前页数
	 * @param pageSize   每页数量
	 * @return
	 */
	@RequestMapping(value = "/queryProxyCouponResult")
	@ResponseBody
	public Object getProxyCouponResult(
			@RequestParam(value = "reportdate", required = false) String nowDate,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize){
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("nowDate", nowDate);
		map.put("accountType", 1);
		Long count = reportFormService.getMemberCouponCount(map);
		List<Map<String, Object>> list= reportFormService.getMemberCouponResult(map,startNo,pageSize);
		return new GridPanel(count, list, true);
	}
	
	/**
	 * 存款统计
	 * @param nowDate    当天时间
	 * @param startNo	   当前页数
	 * @param pageSize   每页数量
	 * @return
	 */
	@RequestMapping(value = "/queryPayMoneyResult")
	@ResponseBody
	public Object getPayMoneyResult(
			@RequestParam(value = "reportdate", required = false) String nowDate,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize){
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("nowDate", nowDate);
		map.put("paytyple", 0);
		Long count = reportFormService.getPayMoneyCount(map);
		List<Map<String, Object>> list= reportFormService.getPayMoneyResult(map,startNo,pageSize);
		return new GridPanel(count, list, true);
	}
	
	
	/**
	 * 取款统计
	 * @param nowDate    当天时间
	 * @param startNo	   当前页数
	 * @param pageSize   每页数量
	 * @return
	 */
	@RequestMapping(value = "/queryPickMoneyResult")
	@ResponseBody
	public Object getPickMoneyResult(
			@RequestParam(value = "reportdate", required = false) String nowDate,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize){
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("nowDate", nowDate);
		map.put("paytyple", 1);
		Long count = reportFormService.getPayMoneyCount(map);
		List<Map<String, Object>> list= reportFormService.getPayMoneyResult(map,startNo,pageSize);
		return new GridPanel(count, list, true);
	}
	
	
	/**
	 * 跳转页面
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/winorless")
	public String winorless(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		request.setAttribute("today", DateUtil.getStrByDate(new Date(),"yyyy-MM-dd"));
		return "manage/reportform/winorless";
	}
	
	/**
	 * 输赢排行榜
	 * @param nowDate    当天时间
	 * @param startNo	   当前页数
	 * @param pageSize   每页数量
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getwinorlessReport")
	@ResponseBody
	public Object getwinorlessReport(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "paccount", required = false) String paccount,
			@RequestParam(value = "sortField", required = false) final Integer sortField,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize){
		Map<String,Object> map=new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(startDate)){
			map.put("startDate", startDate);
		}
		if(StringUtils.isNotEmpty(endDate)){
			map.put("endDate", endDate);
		}
		if(StringUtils.isNotEmpty(account)){
			map.put("account", account);
		}
		if(StringUtils.isNotEmpty(paccount)){
			map.put("paccount", paccount);
		}
		List<Map<String, Object>> list= reportFormService.getwinorlessReport(map,startNo,pageSize);
		Collections.sort(list, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				Map<String, Object> om1 = (Map<String, Object>)o1;
				Map<String, Object> om2 = (Map<String, Object>)o2;
				if(sortField ==1){
					if(Double.valueOf(om1.get("deposit").toString())>Double.valueOf(om2.get("deposit").toString())){
						return -1;
					}else if(Double.valueOf(om1.get("deposit").toString())<Double.valueOf(om2.get("deposit").toString())){
						return 1;
					}else{
						return 0;
					}
				}else if(sortField ==2){
					if(Double.valueOf(om1.get("deposit").toString())>Double.valueOf(om2.get("deposit").toString())){
						return 1;
					}else if(Double.valueOf(om1.get("deposit").toString())<Double.valueOf(om2.get("deposit").toString())){
						return -1;
					}else{
						return 0;
					}
				}else if(sortField ==3){
					if(Double.valueOf(om1.get("withdrawal").toString())>Double.valueOf(om2.get("withdrawal").toString())){
						return -1;
					}else if(Double.valueOf(om1.get("withdrawal").toString())<Double.valueOf(om2.get("withdrawal").toString())){
						return 1;
					}else{
						return 0;
					}
				}else if(sortField ==4){
					if(Double.valueOf(om1.get("withdrawal").toString())>Double.valueOf(om2.get("withdrawal").toString())){
						return 1;
					}else if(Double.valueOf(om1.get("withdrawal").toString())<Double.valueOf(om2.get("withdrawal").toString())){
						return -1;
					}else{
						return 0;
					}
				}else if(sortField ==5){
					if(Double.valueOf(om1.get("preferential").toString())>Double.valueOf(om2.get("preferential").toString())){
						return -1;
					}else if(Double.valueOf(om1.get("preferential").toString())<Double.valueOf(om2.get("preferential").toString())){
						return 1;
					}else{
						return 0;
					}
				}else if(sortField ==6){
					if(Double.valueOf(om1.get("preferential").toString())>Double.valueOf(om2.get("preferential").toString())){
						return 1;
					}else if(Double.valueOf(om1.get("preferential").toString())<Double.valueOf(om2.get("preferential").toString())){
						return -1;
					}else{
						return 0;
					}
				}
				return 0;
			}
		});
		return new GridPanel(1L, list, true);
	}
	
	/**
	 * 导出会员输赢排行表
	 * @param startDate
	 * @param endDate
	 * @param account
	 * @param paccount
	 * @param request
	 * @param response
	 * @throws Exception
	 */
    @RequestMapping(value="toDownloadWinOrLessReport")
    @ResponseBody
    public void toDownloadWinOrLessReport(
    		@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "paccount", required = false) String paccount,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
        OutputStream fOut = null;
        try {
        	response.setContentType("application/vnd.ms-excel;charset=UTF-8");//设置类型
        	Map<String,Object> map=new HashMap<String,Object>();
        	if(StringUtils.isNotEmpty(startDate)){
    			map.put("startDate", startDate);
    		}
    		if(StringUtils.isNotEmpty(endDate)){
    			map.put("endDate", endDate);
    		}
    		if(StringUtils.isNotEmpty(account)){
    			map.put("account", account);
    		}
    		if(StringUtils.isNotEmpty(paccount)){
    			map.put("paccount", paccount);
    		}
    		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            //response.setContentType("application/octet-stream");
            String  fileName = startDate+"-"+endDate+"-winorless.xls";
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            //数据源
            List<Map<String, Object>> list= reportFormService.getwinorlessReport(map,0,2000);
            List<ReportForm> reportList = new ArrayList<>();
            for(Map<String, Object> rm :list){
            	ReportForm report=new ReportForm();
            	report.setProxyCoupon(String.valueOf(rm.get("uaccount")));
            	report.setPayMoney(String.valueOf(rm.get("deposit")));
            	report.setPickUpMoney(String.valueOf(rm.get("withdrawal")));
            	report.setMemberCoupon(String.valueOf(rm.get("preferential")));
            	reportList.add(report);
            }
          
            //excel表头
            LinkedHashMap<String, String> viewMap = new LinkedHashMap<String, String>();
            viewMap.put("proxyCoupon", "会员账号");
            viewMap.put("payMoney", "充值总额");
            viewMap.put("pickUpMoney", "提款总额");
            viewMap.put("memberCoupon", "优惠总额");
            Workbook workBook = ExcelUtil.generateSingleWorkBook(fileName.toString(),reportList, viewMap);
            // 以流的形式下载文件
            fOut = response.getOutputStream();
            workBook.write(fOut);
        } catch (Exception e) {
            logger.error("输赢排行报表导出失败："+e.getMessage(), e);
        } finally {
            if (fOut != null) {
                fOut.flush();
                fOut.close();
            }
        }
    }
}
