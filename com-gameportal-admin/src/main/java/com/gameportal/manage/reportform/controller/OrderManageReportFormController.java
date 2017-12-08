package com.gameportal.manage.reportform.controller;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.listener.SystemFieldsCache;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.reportform.model.OrderManageReportForm;
import com.gameportal.manage.reportform.service.IOrderManageReportFormService;
import com.gameportal.manage.system.service.ISystemService;
import com.gameportal.manage.util.ExcelUtil;

/**
 * 订单管理控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/manage/ordermanageReport")
public class OrderManageReportFormController {
	
	@Resource(name = "systemServiceImpl")
	private ISystemService iSystemService = null;
	@Resource(name="orderManageReportFormService")
	private IOrderManageReportFormService orderManageReportFormService;
	
	public static final Logger logger = Logger
			.getLogger(OrderManageReportFormController.class);

	public OrderManageReportFormController(){
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
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("", "全部");
		jsonObj.put("0", "作废");
		jsonObj.put("1", "发起");
		jsonObj.put("2", "处理中");
		jsonObj.put("3", "存取成功");
		jsonObj.put("4", "存取失败");
		//处理状态
		request.setAttribute("statusMap", jsonObj.toString());
		JSONObject jsonObj2 = new JSONObject();
		jsonObj2.put("", "全部");
		jsonObj2.put("0", "公司入款");
		jsonObj2.put("1", "第三方支付");
		//支付类型
		request.setAttribute("paymethods", jsonObj2.toString());
		JSONObject jsonObj3 = new JSONObject();
		jsonObj3.put("", "全部");
		jsonObj3.put("0", "存款");
		jsonObj3.put("1", "提款");
		jsonObj3.put("2", "加款");
		jsonObj3.put("3", "扣款");
		jsonObj3.put("4", "系统洗码");
		//订单类型
		request.setAttribute("paytype", jsonObj3.toString());
		return "manage/reportform/ordermanage";
	}
	
	
	/**
	 * 钱包余额
	 * @param account  	   用户账户
	 * @return
	 */
	@RequestMapping(value = "/queryAccountMoney")
	@ResponseBody
	public Object queryAccountMoney(
			@RequestParam(value = "account", required = false) String account){
		
		JSONObject result = new JSONObject();
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("account", account);
		Map<String,Object>  resultMap=orderManageReportFormService.getAccountMoney(map);
		if(resultMap==null){
			resultMap=new HashMap<String, Object>();
		}
		if(resultMap.get("totalamount")==null||resultMap.get("totalamount").equals("")){
			resultMap.put("totalamount", 0);
		}
		result.put("accountMoney", resultMap);
		return result;
	}
	
	/**
	 * 订单管理列表
	 * @param account  	   用户账户
	 * @return
	 */
	@RequestMapping(value = "/queryOrderManageResult")
	@ResponseBody
	public Object queryOrderManageResult(
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "paystatus", required = false) String paystatus,  
			@RequestParam(value = "poid", required = false) String poid,   
			@RequestParam(value = "paytype", required = false) String paytype,
			@RequestParam(value = "ordertype", required = false) String ordertype, 
			@RequestParam(value = "paymethods", required = false) String paymethods, 
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize){
		
		Map<String, Object> params=new HashMap<String, Object>();
		if (StringUtils.isNotBlank(account)) {
			params.put("account", account);
		}
		if (StringUtils.isNotBlank(startDate)) {
			params.put("startDate", startDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			params.put("endDate", endDate);
		}
		if (StringUtils.isNotBlank(paystatus)) {
			params.put("status", "("+paystatus+")");
		}
		if (StringUtils.isNotBlank(poid)) {
			params.put("poid", poid);
		}
		if (StringUtils.isNotBlank(paytype)) {
			params.put("paytype", "("+paytype+")");
		}
		if (StringUtils.isNotBlank(ordertype)) {
			params.put("ordertype", "("+ordertype+")");
		}
		if (StringUtils.isNotBlank(paymethods)) {
			params.put("paymethods", "("+paymethods+")");
		}
		params.put("sortColumns", "deposittime desc");
		Long count=orderManageReportFormService.getOrderManageCount(params);
		List<OrderManageReportForm> list=orderManageReportFormService.getOrderManageList(params, startNo, pageSize);
		return new GridPanel(count,list, true);
	}
	
	/**
	 * 订单管理报表导出
	 * @param request   请求对象
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param response  响应对象
	 * @throws Exception
	 */
    @RequestMapping(value="toDownloadReport")
    @ResponseBody
    public void depositorderToDownloadData(HttpServletRequest request,
    						   HttpServletResponse response,
    							@RequestParam(value = "account", required = false) String account,
    							@RequestParam(value = "startDate", required = false) String startDate,
    							@RequestParam(value = "endDate", required = false) String endDate,
    							@RequestParam(value = "paystatus", required = false) String paystatus,  
    							@RequestParam(value = "poid", required = false) String poid,   
    							@RequestParam(value = "paytype", required = false) String paytype,
    							@RequestParam(value = "ordertype", required = false) String ordertype, 
    							@RequestParam(value = "paymethods", required = false) String paymethods) throws Exception {
        OutputStream fOut = null;
        try {
    		Map<String, Object> params=new HashMap<String, Object>();
    		if (StringUtils.isNotBlank(account)) {
    			params.put("account", account);
    		}
    		if (StringUtils.isNotBlank(startDate)) {
    			params.put("startDate", startDate);
    		}
    		if (StringUtils.isNotBlank(endDate)) {
    			params.put("endDate", endDate);
    		}
    		if (StringUtils.isNotBlank(paystatus)) {
    			params.put("status", "("+paystatus+")");
    		}
    		if (StringUtils.isNotBlank(poid)) {
    			params.put("poid", poid);
    		}
    		if (StringUtils.isNotBlank(paytype)) {
    			params.put("paytype", "("+paytype+")");
    		}
    		if (StringUtils.isNotBlank(ordertype)) {
    			params.put("ordertype", "("+ordertype+")");
    		}
    		if (StringUtils.isNotBlank(paymethods)) {
    			params.put("paymethods", "("+paymethods+")");
    		}
    		params.put("sortColumns", "deposittime desc");
            //--------------------
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");//设置类型
            String  fileName = "depositorder.xls";
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            //数据源
	       // Map<String, String> map = iSystemService.qeuryAllFields();
            Map<String, String> map = SystemFieldsCache.fields;
            List<OrderManageReportForm> orderManageList =orderManageReportFormService.getOrderManageListForReport(params);
            for (OrderManageReportForm payOrder : orderManageList) {
				if(null !=payOrder.getStatus()){  //当前状态
					if(payOrder.getStatus()==0){
						payOrder.setStatusname("作废");
					}else if(payOrder.getStatus()==1){
						payOrder.setStatusname("发起");
					}else if(payOrder.getStatus()==2){
						payOrder.setStatusname("处理中");
					}else if(payOrder.getStatus()==3){
						payOrder.setStatusname("成功");
					}else if(payOrder.getStatus()==4){
						payOrder.setStatusname("失败");
					}
				}
				if(null !=payOrder.getPaymethods()){  //付款方式
					if(payOrder.getPaymethods()==0){
						payOrder.setPaymethodsname("公司入款");
					}else if(payOrder.getPaymethods()==1){
						payOrder.setPaymethodsname("第三方支付");
					}
				}
				if(null !=payOrder.getPaytyple()){  //交易类型
					if(payOrder.getPaytyple()==0){
						payOrder.setPaytyplename("存款");
					}else if(payOrder.getPaytyple()==1){
						payOrder.setPaytyplename("提款");
					}else if(payOrder.getPaytyple()==2){
						payOrder.setPaytyplename("加款");
					}else if(payOrder.getPaytyple()==3){
						payOrder.setPaytyplename("扣款");
					}else if(payOrder.getPaytyple()==4){
						payOrder.setPaytyplename("系统洗码");
					}
				}
				if(null !=payOrder.getOrdertype()){  //订单类型
					String ordertypeList=map.get("ordertype");
					String [] ordertypeLists=ordertypeList.substring(1, ordertypeList.length()-1).split(",");
					for (String string : ordertypeLists) {
						String [] ordername=string.split(":");
						if(payOrder.getOrdertype().toString().equals((ordername[0].substring(1,ordername[0].length()-1)))){
							payOrder.setOrdertypename(ordername[1].substring(1,ordername[1].length()-1));
						}
					}
				}
			}
            //excel表头
            LinkedHashMap<String, String> viewMap = new LinkedHashMap<String, String>();
            viewMap.put("poid", "订单ID");
            viewMap.put("uaccount", "会员账号");
            viewMap.put("urealname", "会员名称");
            viewMap.put("paytyplename", "交易类型");
            viewMap.put("ordertypename", "订单类型");
            viewMap.put("paymethodsname", "支付方式");
            viewMap.put("deposittime", "处理时间");
            viewMap.put("amount", "金额");
            viewMap.put("memberximaMoney", "官方洗码金额");
            viewMap.put("proxyclearMoney", "代理洗码金额");
            viewMap.put("proxyuserximaMoney", "代理下线洗码金额");
            viewMap.put("statusname", "处理状态");
            viewMap.put("kfremarks", "客服备注");
            viewMap.put("kfname", "操作客服");
            viewMap.put("kfopttime", "客服操作时间");
            viewMap.put("cwremarks", "财务备注");
            viewMap.put("cwname", "操作财务");
            viewMap.put("cwopttime", "财务操作时间");
            viewMap.put("beforebalance", "操作前钱包余额");
            viewMap.put("laterbalance", "操作后钱包余额");
            Workbook workBook = ExcelUtil.generateSingleWorkBook(fileName.toString(),orderManageList, viewMap);
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
