package com.gameportal.manage.member.controller;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.manage.member.model.GameTransfer;
import com.gameportal.manage.member.model.GameTransferLog;
import com.gameportal.manage.member.service.IGameTransferLogService;
import com.gameportal.manage.member.service.IGameTransferService;
import com.gameportal.manage.pay.model.PayOrderLog;
import com.gameportal.manage.pay.service.IPayOrderService;
import com.gameportal.manage.pojo.ExceptionReturn;
import com.gameportal.manage.pojo.ExtReturn;
import com.gameportal.manage.pojo.GridPanel;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.user.model.AccountMoney;
import com.gameportal.manage.user.service.IAccountMoneyService;
import com.gameportal.manage.util.DateUtil;
import com.gameportal.manage.util.DateUtil2;

import net.sf.json.JSONObject;

/**
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/manage/gameTransferLog")
public class GameTransferLogController {
	@Resource(name = "gameTransferLogServiceImpl")
	private IGameTransferLogService gameTransferLogService = null;
	
	@Resource(name="gameTransferServiceImpl")
	private IGameTransferService gameTransferService;
	
	@Resource(name = "redisServiceImpl")
	private IRedisService redisService=null;
	
	@Resource(name = "accountMoneyServiceImpl")
	private IAccountMoneyService accountMoneyService;
	
	@Resource(name = "payOrderServiceImpl")
	private IPayOrderService payOrderServiceImpl;
	
	public static final Logger logger = Logger
			.getLogger(GameTransferLogController.class);

	public GameTransferLogController() {
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
		return "manage/member/gameTransferLog";
	}

	@RequestMapping(value = "/queryGameTransferLog")
	public @ResponseBody
	Object queryGameTransferLog(
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try{
				Map<String,Object> map=new HashMap<String,Object>();
				if(StringUtils.isNotBlank(account)){
					map.put("account", account);
				}
				Long count = gameTransferLogService.queryGameTransferLogCount(map);
				map.put("sortColumns", " create_date desc");
				List<GameTransferLog> list = gameTransferLogService.queryGameTransferLog(map, startNo==null?0:startNo, pageSize==null?30:pageSize);
				return new GridPanel(count, list, true);
			  
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping(value = "/savegameTransferLog")
	@ResponseBody
	public Object savegameTransferLog(
			@ModelAttribute GameTransferLog gameTransferLog,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			
			if (gameTransferLogService.saveGameTransferLog(gameTransferLog)) {
				return new ExtReturn(true, "保存成功！");
			} else {
				return new ExtReturn(false, "保存失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping("/delgameTransferLog/{uiid}")
	@ResponseBody
	public Object delgameTransferLog(@PathVariable Long uiid) {
		try {
			if (!StringUtils.isNotBlank(ObjectUtils.toString(uiid))) {
				return new ExtReturn(false, "主键不能为空！");
			}
			if (gameTransferLogService.deleteGameTransferLog(uiid)) {
				return new ExtReturn(true, "删除成功！");
			} else {
				return new ExtReturn(false, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	@RequestMapping(value = "/transferindex")
	public String userTransferindex(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		request.setAttribute("id", id);
		JSONObject map = new JSONObject();
		map.put("", "全部");
		map.put("AGIN", "AGIN国际厅");
		map.put("PT", "PT电子");
		map.put("AG", "AG极速厅");
		map.put("BBIN", "BBIN厅");
		map.put("MG", "MG厅");
		map.put("SA", "SA电子");
		request.setAttribute("gameplat", map.toString());
		map = new JSONObject();
		map.put("", "全部");
		map.put("0", "处理中");
		map.put("1", "成功");
		map.put("2", "失败");
		request.setAttribute("logStatus", map.toString());
		return "manage/member/transferslog";
	}
	
	@RequestMapping(value = "/queryUserTransfer")
	public @ResponseBody
	Object queryUserTransfer(
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "gameplat", required = false) String gameplat,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try{
				Map<String,Object> params=new HashMap<String,Object>();
				if(StringUtils.isNotBlank(account)){
					params.put("account", account);
				}
				if(StringUtils.isNotBlank(startDate)){
					params.put("startDate", startDate);
				}
				if(StringUtils.isNotBlank(endDate)){
					params.put("endDate", endDate);
				}
				if(StringUtils.isNotBlank(gameplat)){
					params.put("gameplat",gameplat);
				}
				if(StringUtils.isNotEmpty(status)){
					params.put("status",status);
				}
				Long count = gameTransferService.getCount(params);
				params.put("sortColumns", " create_date desc");
				List<GameTransfer> list = gameTransferService.getList(params, startNo==null?0:startNo, pageSize==null?30:pageSize);
				return new GridPanel(count, list, true);
			  
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 统计转账记录
	 * @param startDate  开始时间
	 * @param endDate    结束时间
	 * @param startNo    当前页
	 * @param pageSize   每页行数
	 * @param request    请求对象
	 * @param response   响应对象
	 * @return
	 */
	@RequestMapping(value = "/queryTransferForReport")
	public @ResponseBody
	Object queryTransferForReport(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "start", required = false) Integer startNo,
			@RequestParam(value = "limit", required = false) Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		try{
				String s = " 00:00:00";
				String e = " 23:59:59";
				String date = DateUtil2.format(new Date());
				Map<String,Object> params=new HashMap<String,Object>();
				if(StringUtils.isBlank(startDate)){
					startDate=DateUtil2.getFirstDay(date) + s; //本月第一天
				}
				if(StringUtils.isBlank(endDate)){
					endDate=DateUtil2.getEndDay(date) + e; //本月最后一天
				}
				params.put("startDate", startDate);
				params.put("endDate", endDate);
				Long count = gameTransferService.getTransferCountForReport(params);
				List<Map<String, Object>> list = gameTransferService.getTransferForReport(params, startNo==null?0:startNo, pageSize==null?30:pageSize);
				return new GridPanel(count, list, true);
			  
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
	
	/**
	 * 标记成功
	 * @param uiid
	 * @return
	 */
	@RequestMapping("/mark/{gtid}/{status}")
	@ResponseBody
	public Object mark(@PathVariable Long gtid,@PathVariable Integer status) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("gtid", gtid);
			GameTransfer gameTransfer = gameTransferService.getGameTransfer(params);
			gameTransfer.setStatus(status);
			gameTransfer.setUpdateDate(new Date());
			if(status ==1){ //标记成功
				if(gameTransfer.getGpid() == 0){ // 主钱包转游戏钱包
					params= new HashMap<>();
					params.put("uiid", gameTransfer.getUuid());
					AccountMoney am = accountMoneyService.getMoneyInfo(params);
					BigDecimal amount = new BigDecimal(gameTransfer.getAmount());
					if(am != null && am.getTotalamount().compareTo(amount)>=0){
						am.setTotalamount(am.getTotalamount().subtract(amount));
						am.setUpdateDate(new Date());
						accountMoneyService.saveOrUpdateAccountMoney(am);
						
						// 新增帐变记录。
						PayOrderLog log = new PayOrderLog();
						log.setUiid(gameTransfer.getUuid());
						log.setOrderid(gameTransfer.getGpid().toString());
						log.setAmount(BigDecimal.ZERO.subtract(new BigDecimal(gameTransfer.getAmount())));
						log.setType(9);
						log.setWalletlog(gameTransfer.getOrigamount() + ">>>" + gameTransfer.getBalance());
						log.setGamelog(gameTransfer.getOtherbefore() + ">>>" + gameTransfer.getOtherafter());
						log.setRemark(gameTransfer.getGamename() + "转入" + gameTransfer.getTogamename());
						log.setCreatetime(DateUtil.getStrByDate(gameTransfer.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
						payOrderServiceImpl.insertPayLog(log);
					}
				}else{
					params= new HashMap<>();
					params.put("uiid", gameTransfer.getUuid());
					AccountMoney am = accountMoneyService.getMoneyInfo(params);
					
					am.setTotalamount(am.getTotalamount().add(new BigDecimal(gameTransfer.getAmount())));
					am.setUpdateDate(new Date());
					accountMoneyService.saveOrUpdateAccountMoney(am);
					
					// 新增帐变记录。
					PayOrderLog log = new PayOrderLog();
					log.setUiid(gameTransfer.getUuid());
					log.setOrderid(gameTransfer.getGpid().toString());
					log.setAmount(new BigDecimal(gameTransfer.getAmount()));
					log.setType(9);
					log.setWalletlog(gameTransfer.getOrigamount() + ">>>" + gameTransfer.getBalance());
					log.setGamelog(gameTransfer.getOtherbefore() + ">>>" + gameTransfer.getOtherafter());
					log.setRemark(gameTransfer.getGamename() + "转入" + gameTransfer.getTogamename());
					log.setCreatetime(DateUtil.getStrByDate(gameTransfer.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
					payOrderServiceImpl.insertPayLog(log);
				}
			}
			if (gameTransferService.updateGameTransfer(gameTransfer)) {
				return new ExtReturn(true, "操作成功！");
			} else {
				return new ExtReturn(false, "操作失败！");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
}
