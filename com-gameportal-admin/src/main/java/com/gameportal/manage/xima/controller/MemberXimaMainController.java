package com.gameportal.manage.xima.controller;

import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.betlog.model.BetLogTotal;
import com.gameportal.manage.betlog.service.IBetLogService;
import com.gameportal.manage.gameplatform.model.GamePlatform;
import com.gameportal.manage.gameplatform.service.IGamePlatformService;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.user.model.UserInfo;
import com.gameportal.manage.user.service.IUserInfoService;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.DateUtil;
import com.gameportal.manage.util.DateUtil2;
import com.gameportal.manage.util.ExcelUtil;
import com.gameportal.manage.util.WebConstants;
import com.gameportal.manage.xima.model.MemberXimaDetail;
import com.gameportal.manage.xima.model.MemberXimaMain;
import com.gameportal.manage.xima.service.IMemberXimaDetailService;
import com.gameportal.manage.xima.service.IMemberXimaMainService;

@Controller
@RequestMapping(value = "/manage/memberximamain")
public class MemberXimaMainController {

	private static final Logger logger = Logger
			.getLogger(MemberXimaMainController.class);

	@Resource(name = "memberXimaMainServiceImpl")
	private IMemberXimaMainService memberXimaMainService = null;
	@Resource(name = "memberXimaDetailServiceImpl")
	private IMemberXimaDetailService memberXimaDetailService = null;
	@Resource(name = "gamePlatformServiceImpl")
	private IGamePlatformService gamePlatformService = null;
	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService = null;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	@Resource(name = "betLogServiceImpl")
	private IBetLogService betLogService = null;
	public MemberXimaMainController() {
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
		// 游戏平台集合(状态 0 关闭 1开启)
		//{xtype:'checkbox',boxLabel : '输值结算',name : "clearingType",inputValue : '1',checked : true
		JSONObject gameplatMap = new JSONObject();
		List<GamePlatform> gpList = gamePlatformService.queryGamePlatform(null,
				null, 1, null, null);
		StringBuffer pt = new StringBuffer();
		if (null != gpList && gpList.size() > 0) {
			for (GamePlatform gp : gpList) {
				if(!"PT".equals(gp.getGpname())){
					pt.append("{");
					pt.append("xtype:").append("'checkbox',");
					pt.append("name:").append("'memberClearingType',");
					pt.append("boxLabel:").append("'").append(gp.getGpname()).append("',");
					pt.append("inputValue:'").append(gp.getGpid()).append("#").append(gp.getGpname()).append("',");
					pt.append("checked:").append("true");
					pt.append("}").append(",'&nbsp;',");
					
					gameplatMap.put(gp.getGpid(), gp.getGpname());
				}
			}
		}
		request.setAttribute("gameplatMap", gameplatMap.toString());
		request.setAttribute("pt", pt.toString().substring(0, pt.toString().length()-1));
		// locked 锁定状态 0未锁定 1锁定
		JSONObject lockedMap = new JSONObject();
		lockedMap.put("0", "未锁定");
		lockedMap.put("1", "锁定");
		request.setAttribute("lockedMap", lockedMap.toString());
		// opttype 操作类型 0自助洗码，1洗码清零，2强制洗码
		JSONObject opttypeMap = new JSONObject();
		opttypeMap.put("0", "自助洗码");
		opttypeMap.put("1", "洗码清零");
		opttypeMap.put("2", "强制洗码");
		request.setAttribute("opttypeMap", opttypeMap.toString());
		return "manage/xima/memberximamain";
	}

	@RequestMapping(value = "/queryMemberximamain")
	public @ResponseBody
	Object queryMemberXimaMain(
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(account)) {
			params.put("account", account);
		}
		if (null != startDate) {
			params.put("ymdstart", startDate);
		}
		if (null != endDate) {
			params.put("ymdend", endDate);
		}
		params.put("sortColumns", "updatetime DESC");
		Long count = memberXimaMainService.queryMemberXimaMainCount(params);
		List<MemberXimaMain> list = memberXimaMainService.queryMemberXimaMain(
				params, startNo, pageSize);
		return new GridPanel(count, list, true);
	}
	
	
	/**
	 * 未入账数据
	 * @param account
	 * @param startDate
	 * @param endDate
	 * @param startNo
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryNoAccount")
	public @ResponseBody
	Object queryNoAccount(
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(account)) {
			params.put("account", account);
		}
		if (null != startDate) {
			params.put("ymdstart", startDate);
		}
		if (null != endDate) {
			params.put("ymdend", endDate);
		}
		params.put("locked", "0");
		params.put("sortColumns", "updatetime DESC");
		Long count = memberXimaMainService.queryMemberXimaMainCount(params);
		List<MemberXimaMain> list = memberXimaMainService.queryMemberXimaMain(
				params, startNo, pageSize);
		return new GridPanel(count, list, true);
	}
	
	
	/**
	 * 会员洗码记录管理导出
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
    							@RequestParam(value = "account", required = false) String account,
    							@RequestParam(value = "startDate", required = false) String startDate,
    							@RequestParam(value = "endDate", required = false) String endDate) throws Exception {
        OutputStream fOut = null;
        try {
    		Map<String, Object> params = new HashMap<String, Object>();
    		if (StringUtils.isNotBlank(account)) {
    			params.put("account", account);
    		}
    		if (null != startDate) {
    			params.put("ymdstart", startDate);
    		}
    		if (null != endDate) {
    			params.put("ymdend", endDate);
    		}
    		params.put("sortColumns", "updatetime DESC");
    		//----------------------------------------------------
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");//设置类型
            String  fileName = "memberXimaMain.xls";
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            //数据源
            List<MemberXimaMain> memberXimaMainList=memberXimaMainService.queryMemberXimaMain(params);
            for (MemberXimaMain memberXimaMain : memberXimaMainList) {
            	if(memberXimaMain.getLocked()!=null){
	            	if(memberXimaMain.getLocked()== 1){
	            		memberXimaMain.setLockedname("已入账");
					}else if(memberXimaMain.getLocked() == 2){
						memberXimaMain.setLockedname("审核失败");
					}else{
						memberXimaMain.setLockedname("未入账");
					}
            	}else{
            		memberXimaMain.setLockedname("未入账");
            	}
			}
            //excel表头
            LinkedHashMap<String, String> viewMap = new LinkedHashMap<String, String>();
            viewMap.put("lockedname", "状态");
            viewMap.put("gpid", "游戏平台");
            viewMap.put("account", "会员账号");
            viewMap.put("name", "会员名称");
            viewMap.put("total", "反水总金额(元)");
            viewMap.put("ymdstart", "洗码开始时间");
            viewMap.put("ymdend", "洗码结束时间");
            viewMap.put("updatetime", "更新时间");
            Workbook workBook = ExcelUtil.generateSingleWorkBook(fileName.toString(),memberXimaMainList, viewMap);
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

	@RequestMapping("/del/{mxmid}")
	@ResponseBody
	public Object delMemberXimaMain(@PathVariable Long mxmid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(mxmid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("mxmid", mxmid);
			List<MemberXimaMain> list = memberXimaMainService
					.queryMemberXimaMain(params, 0, 0);
			if (null != list && list.size() > 0) {
				params = new HashMap<String, Object>();
				params.put("uiid", list.get(0).getUiid());
				List<MemberXimaDetail> detailList = memberXimaDetailService
						.queryMemberXimaDetail(params, 0, 0);
				if (null != detailList && detailList.size() > 0) {
					return new ExtReturn(false, "该会员有洗码明细记录，不能删除该会员洗码总记录！");
				}
			}
			if (memberXimaMainService.deleteMemberXimaMain(mxmid)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	public static void main(String[] args) {
		String u = "dxdxlx6688";
		System.out.println(u.substring(4, u.length()));
	}

	@RequestMapping(value = "/save")
	@ResponseBody
	public Object saveMemberXimaMain(
			@RequestParam(value = "accounts", required = false) String account,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "gameplat", required = false) String gameplat,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			String today = DateUtil2.format(new Date());
			if(!StringUtils.isNotBlank(startTime)){
				return new ExtReturn(false, "请选择您要结算的日期。");
			}
			if(startTime.equals(today)){
				return new ExtReturn(false, "您选择的结算日期为："+today+",不能结算当天数据,请明天结束今天数据。");
			}
			if(!StringUtils.isNotBlank(account)){
				return new ExtReturn(false, "没有可结算用户数据。");
			}
			String[] accounts = account.split(",");
			if(null == accounts || accounts.length == 0){
				return new ExtReturn(false, "请至少选择一个用户进行结算。");
			}
			//过滤重复数据
			List<String> accountList = new ArrayList<String>();
			for(int i = 0; i < accounts.length; i++){
	        	if(accountList.size() ==0){
					accountList.add(accounts[i]);
				}else{
					boolean isAdd = false;
					for(int j=0;j<accountList.size();j++){
						String a = accountList.get(j);
						String ja = accounts[i];
						if(a.equals(ja)){
							isAdd = false;
							break;
						}
						isAdd = true;
					}
					if(isAdd){
						accountList.add(accounts[i]);
						isAdd = false;
					}
				}
	        }
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("account", accountList);
			String[] pcode = gameplat.split(",");
			StringBuffer sb = new StringBuffer();
			
			for(int i = 0;i < pcode.length ;i++){
				String[] k_v = pcode[i].split("#");
				sb.append("'").append(k_v[1]).append("'");
				if((i+1) < pcode.length){
					sb.append(",");
				}
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(
					key, c);
			params.put("optuiid", systemUser.getUserId());
			params.put("optuname", systemUser.getRealName());
			params.put("ymdstart", startTime+" 00:00:00");
			params.put("ymdend", startTime+ " 23:59:59");
			params.put("jstimes", startTime);
			params.put("gameplat", gameplat);
			params.put("platformcodeparams", "("+sb.toString()+")");
			String result = memberXimaMainService.saveOrUpdateMemberXimaMain(params);
			if(null != result){
				
			}
			return new ExtReturn(true, "保存成功，洗码金额还未能到玩家钱包，请通过审核后洗码金额才会添加到玩家钱包。");
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/clear/{mxmid}")
	@ResponseBody
	public Object clearMemberXimaMain(
			@PathVariable Long mxmid,
			@RequestParam(value = "ymdstart", required = false) String ymdstart,
			@RequestParam(value = "ymdend", required = false) String ymdend,
			HttpServletRequest request, HttpServletResponse response) {
//		try {
//			Timestamp date = new Timestamp(new Date().getTime());
//			if (!StringUtils.isNotBlank(ObjectUtils.toString(mxmid))) {
//				return new ExtReturn(false, "主键不能为空！");
//			}
//			String vuid = CookieUtil.getOrCreateVuid(request, response);
//			String key = vuid + WebConstants.FRAMEWORK_USER;
//			Class<Object> c = null;
//			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(
//					key, c);
//
//			// 获取洗码总记录
//			MemberXimaMain mxm = null;
//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("mxmid", mxmid);
//			List<MemberXimaMain> list = memberXimaMainService
//					.queryMemberXimaMain(params, 0, 0);
//			if (null == list || list.size() <= 0) {
//				return new ExtReturn(false, "该记录不存在，请刷新后再试！");
//			} else {
//				mxm = list.get(0);
//			}
//			// 最后一次洗码时间过滤
////			if (DateUtil.getStrYMDByDate(mxm.getYmdend()).equals(
////					DateUtil.getStrYMDByDate(new Date()))) {
////				return new ExtReturn(false, "今天已进行过洗码操作，请明天后再试！");
////			}
//			// 锁定状态 0未锁定 1锁定
//			if (1 == mxm.getLocked()) {
//				return new ExtReturn(false, "该记录已被锁定，请先解锁后再操作！");
//			}
//			// 游戏平台记录
//			GamePlatform gp = gamePlatformService.queryGamePlatformById(mxm
//					.getGpid());
//			if (null == gp) {
//				return new ExtReturn(false, "数据异常[游戏平台记录不存在[gpid="
//						+ mxm.getGpid() + "]]，请稍后再试！");
//			} else {
//				mxm.setGpname(gp.getGpname());
//			}
//			// 获取洗码开始日期
//			Date ymdstartDate = null;
//			if (StringUtils.isBlank(ymdstart)) {
//				ymdstartDate = mxm.getYmdend();
//			} else {
//				ymdstartDate = DateUtil.getDateByStr(ymdstart);
//			}
//			// 获取洗码结束日期
//			Date ymdendDate = null;
//			if (StringUtils.isBlank(ymdend)) {
//				ymdendDate = new Date();
//			} else {
//				ymdendDate = DateUtil.getDateByStr(ymdend);
//			}
//			// 洗码清零（保存洗码明细记录）
//			if (false == memberXimaMainService.clearXima(mxm, ymdstartDate,
//					ymdendDate, systemUser)) {
//				return new ExtReturn(false, "操作失败！");
//			}
//			// 保存洗总记录
//			mxm.setUpdatetime(date);
//			mxm.setYmdend(new java.sql.Date(ymdendDate.getTime()));
//			if (true) {
//				return new ExtReturn(true, "操作成功！");
//			} else {
//				return new ExtReturn(false, "操作失败！");
//			}
//		} catch (Exception e) {
//			logger.error("Exception: ", e);
//			return new ExceptionReturn(e);
//		}
		return null;
	}

	@RequestMapping("/force/{mxmid}")
	@ResponseBody
	public Object forceMemberXimaMain(
			@PathVariable Long mxmid,
			@RequestParam(value = "ymdstart", required = false) String ymdstart,
			@RequestParam(value = "ymdend", required = false) String ymdend,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Timestamp date = new Timestamp(new Date().getTime());
			if (!StringUtils.isNotBlank(ObjectUtils.toString(mxmid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_USER;
			Class<Object> c = null;
			SystemUser systemUser = (SystemUser) iRedisService.getRedisResult(
					key, c);

			// 获取洗码总记录
			MemberXimaMain mxm = null;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("mxmid", mxmid);
			List<MemberXimaMain> list = memberXimaMainService
					.queryMemberXimaMain(params, 0, 0);
			if (null == list || list.size() <= 0) {
				return new ExtReturn(false, "该记录不存在，请刷新后再试！");
			} else {
				mxm = list.get(0);
			}
			// 锁定状态 0未锁定 1锁定
			if (1 == mxm.getLocked()) {
				return new ExtReturn(false, "该记录已被锁定，请先解锁后再操作！");
			}
//			// 游戏平台记录
//			GamePlatform gp = gamePlatformService.queryGamePlatformById(mxm
//					.getGpid());
//			if (null == gp) {
//				return new ExtReturn(false, "数据异常[游戏平台记录不存在[gpid="
//						+ mxm.getGpid() + "]]，请稍后再试！");
//			} else {
//				mxm.setGpname(gp.getGpname());
//			}
			// 获取洗码开始日期
			Date ymdstartDate = null;
			if (StringUtils.isBlank(ymdstart)) {
			//	ymdstartDate = mxm.getYmdend();
			} else {
				ymdstartDate = DateUtil.getDateByStr(ymdstart);
			}
			// 获取洗码结束日期
			Date ymdendDate = null;
			if (StringUtils.isBlank(ymdend)) {
				ymdendDate = new Date();
			} else {
				ymdendDate = DateUtil.getDateByStr(ymdend);
			}
			// 洗码清零（保存洗码明细记录）
			if (false == memberXimaMainService.forceXima(mxm, ymdstartDate,
					ymdendDate, systemUser)) {
				return new ExtReturn(false, "操作失败！");
			}
			// 保存洗总记录
			//mxm.setUpdatetime(date);
			//mxm.setYmdend(new java.sql.Date(ymdendDate.getTime()));
			if (true) {
				return new ExtReturn(true, "操作成功！");
			} else {
				return new ExtReturn(false, "操作失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 审核通过洗码
	 * @param mxmid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/enable/{mxmid}")
	@ResponseBody
	public Object enableMemberXimaMain(@PathVariable Long mxmid,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(mxmid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			
			// 获取洗码总记录
			MemberXimaMain mxm = null;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("mxmid", mxmid);
			List<MemberXimaMain> list = memberXimaMainService
					.queryMemberXimaMain(params, 0, 1);
			if (null == list || list.size() <= 0) {
				return new ExtReturn(false, "该记录不存在，请刷新后再试！");
			} else {
				mxm = list.get(0);
			}
			if (1 == mxm.getLocked()) {// locked 锁定状态 0未锁定 1锁定
				return new ExtReturn(false, "该记录已经是[已通过]状态，不能重复审核！");
			}
			mxm.setLocked(1);
			mxm.setUpdatetime(DateUtil.getStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			memberXimaMainService.checkBy(mxm);
			if (true) {
				return new ExtReturn(true, "审核通过成功，洗码金额已经加入玩家钱包！");
			} else {
				return new ExtReturn(false, "审核通过失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 批量审核通过洗码
	 * @param mxmid id集合
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/batchEnable")
	@ResponseBody
	public Object enableMemberXimaMain(@RequestParam(value = "mxmid", required = false)String mxmid,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(mxmid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			String [] mxmids=mxmid.split(",");
			for (String mxid : mxmids) {
				// 获取洗码总记录
				MemberXimaMain mxm = null;
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("mxmid", mxid);
				List<MemberXimaMain> list = memberXimaMainService
						.queryMemberXimaMain(params, 0, 1);
				if (null == list || list.size() <= 0) {
					return new ExtReturn(false, "该记录不存在，请刷新后再试！");
				} else {
					mxm = list.get(0);
				}
				if (1 == mxm.getLocked()) {// locked 锁定状态 0未锁定 1锁定
					return new ExtReturn(false, "该记录已经是[已通过]状态，不能重复审核！");
				}
				mxm.setLocked(1);
				mxm.setUpdatetime(DateUtil.getStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
				memberXimaMainService.checkBy(mxm);
			}
			if (true) {
				return new ExtReturn(true, "批量审核通过成功，洗码金额已经加入玩家钱包！");
			} else {
				return new ExtReturn(false, "批量审核通过失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	@RequestMapping("/disable/{mxmid}")
	@ResponseBody
	public Object disableMemberXimaMain(@PathVariable Long mxmid,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(mxmid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			
			// 获取洗码总记录
			MemberXimaMain mxm = null;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("mxmid", mxmid);
			List<MemberXimaMain> list = memberXimaMainService
					.queryMemberXimaMain(params, 0, 1);
			if (null == list || list.size() <= 0) {
				return new ExtReturn(false, "该记录不存在，请刷新后再试！");
			} else {
				mxm = list.get(0);
			}
			if (1 == mxm.getLocked()) {// locked 锁定状态 0未锁定 1锁定
				return new ExtReturn(false, "该记录已经是[审核失败]状态，不能重复审核！");
			}
			mxm.setLocked(2);
			mxm.setUpdatetime(DateUtil.getStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			memberXimaMainService.checkBy(mxm);
			return new ExtReturn(true, "审核失败，数据状态已被改变！");
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping(value = "/queryBetCountLog")
	public @ResponseBody
	Object queryBetCountLog(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "gamecode", required = false) String gamecode,
			@RequestParam(value = "platformcode", required = false) String gamename,
			//@RequestParam(value = "start", required = false) Integer startNo,
			//@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
			Map<String,Object> map=new HashMap<String,Object>();
			 Date date = new Date();
			 String day = startDate;//yyyy-MM-dd
		   if(!StringUtils.isBlank(startDate)){
			   map.put("starttime", startDate +" 00:00:00");
			   map.put("jstime", day);
		   }else{
			   map.put("starttime", DateUtil2.format(date) + " 00:00:00");
			   map.put("jstime", DateUtil2.format(date));
		   }
		   if(!StringUtils.isBlank(endDate)){
			   map.put("endtime", endDate + " 23:59:59");
			   map.put("jstime", day);
		   }else{
			   map.put("endtime", DateUtil2.format(date) + " 23:59:59");
			   map.put("jstime", DateUtil2.format(date));
		   }
		   if(!StringUtils.isBlank(gamecode)){
			   map.put("gamecode", gamecode);
		   }
		   if(StringUtils.isNotBlank(account)){
			   map.put("account", account);
		   }
		   if(!StringUtils.isBlank(gamename)){
			   map.put("platformcode", "("+gamename+")");
			   map.put("platname", "("+gamename+")");
		   }else{
			   map.put("platformcode", "('AG','AGIN','BBIN','MG')");
			   map.put("platname", "('AG','AGIN','BBIN','MG')");
		   }
		   map.put("groupColumns", "bet.account,bet.platformcode,bet.gamecode");
		   map.put("flag", "1");//只查询可以洗码的注单
		   System.out.println("开始时间:"+map.get("starttime"));
		   System.out.println("结束时间:"+map.get("endtime"));
		   //Long count = betLogService.selectXimaBetTotalCount(map);
		   Long count = 0L;
		   List<BetLogTotal> list = betLogService.selectXimaBetTotal(map, 0, 0);
		   if(list != null){
			   count = Long.valueOf(list.size());
		   }
		   return new GridPanel(count, list, true);
	}
	
	/**
	 * PT洗码
	 * @param startDate
	 * @param endDate
	 * @param gamecode
	 * @param gamename
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryBetCountLogPT")
	public @ResponseBody
	Object queryBetCountLogPT(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "gamecode", required = false) String gamecode,
			@RequestParam(value = "platformcode", required = false) String gamename,
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
			Map<String,Object> map=new HashMap<String,Object>();
			 Date date = new Date();
			 String day = startDate;//yyyy-MM-dd
		   if(!StringUtils.isBlank(startDate)){
			   map.put("starttime", startDate +" 00:00:00");
			   map.put("jstime", day);
		   }else{
			   map.put("starttime", DateUtil2.format(date) + " 00:00:00");
			   map.put("jstime", DateUtil2.format(date));
		   }
		   if(!StringUtils.isBlank(endDate)){
			   map.put("endtime", endDate + " 23:59:59");
			   map.put("jstime", day);
		   }else{
			   map.put("endtime", DateUtil2.format(date) + " 23:59:59");
			   map.put("jstime", DateUtil2.format(date));
		   }
		   if(!StringUtils.isBlank(gamecode)){
			   map.put("gamecode", gamecode);
		   }
		   if(!StringUtils.isBlank(gamename)){
			   map.put("platformcode", "("+gamename+")");
			   map.put("platname", "("+gamename+")");
		   }else{
			   map.put("platformcode", "('PT')");
			   map.put("platname", "('PT')");
		   }
		   
		   if(StringUtils.isNotBlank(account)){
			   map.put("account", account);
		   }
		   map.put("groupColumns", "bet.account");
		   map.put("flag", "1");//只查询可以洗码的注单
		   System.out.println("开始时间:"+map.get("starttime"));
		   System.out.println("结束时间:"+map.get("endtime"));
		   Long count = betLogService.selectXimaBetTotalCount(map);
		   map.put("limit", true);
		   map.put("thisPage", startNo);
		   map.put("pageSize", pageSize);
		   List<BetLogTotal> list = betLogService.selectXimaBetTotal(map, 0, 0);
		   return new GridPanel(count, list, true);
	}

}
