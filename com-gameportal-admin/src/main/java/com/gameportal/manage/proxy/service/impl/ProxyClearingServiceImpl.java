package com.gameportal.manage.proxy.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.member.dao.MemberInfoDao;
import com.gameportal.manage.member.model.ProxyXimaFlag;
import com.gameportal.manage.proxy.dao.ProxyClearingFlagDao;
import com.gameportal.manage.proxy.dao.ProxyClearingLogDao;
import com.gameportal.manage.proxy.model.ProxyClearingEntity;
import com.gameportal.manage.proxy.model.ProxyClearingLog;
import com.gameportal.manage.proxy.model.ProxyClearingLogTotal;
import com.gameportal.manage.proxy.model.ProxyReportEntity;
import com.gameportal.manage.proxy.service.IProxyClearingService;
import com.gameportal.manage.proxy.service.IProxyInfoService;
import com.gameportal.manage.user.dao.AccountMoneyDao;
import com.gameportal.manage.user.model.AccountMoney;
import com.gameportal.manage.util.DateUtil2;

/**
 * 代理结算业务逻辑
 * @author Administrator
 *
 */
@Service("proxyClearingService")
public class ProxyClearingServiceImpl implements IProxyClearingService{
	
	public static final Logger logger = Logger.getLogger(ProxyClearingServiceImpl.class);
	
	@Resource(name = "proxyClearingLogDao")
	private ProxyClearingLogDao proxyClearingLogDao;
	
	@Resource(name = "proxyClearingFlagDao")
	private ProxyClearingFlagDao proxyClearingFlagDao;
	
	@Resource(name = "accountMoneyDao")
	private AccountMoneyDao accountMoneyDao;
	
	@Resource(name = "memberInfoDao")
	private MemberInfoDao memberInfoDao = null;
	
	@Resource(name = "proxyInfoServiceImpl")
	private IProxyInfoService iProxyInfoService = null;

	@Override
	public Long count(Map<String, Object> params) {
		return proxyClearingLogDao.getRecordCount(params);
	}

	@Override
	public List<ProxyClearingLog> getList(Map<String, Object> params,
			int thisPage, int pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(thisPage))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			thisPage = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", thisPage);
		params.put("pageSize", pageSize);
		return proxyClearingLogDao.getList(params);
	}

	@Override
	public ProxyClearingLog getObject(Map<String, Object> params) {
		return proxyClearingLogDao.getObject(params);
	}

	@Override
	public boolean save(ProxyClearingLog entity) {
		return StringUtils.isNotBlank(ObjectUtils.toString(proxyClearingLogDao.save(entity))) ? true
				: false;
	}

	@Override
	public boolean update(ProxyClearingLog entity) {
		return proxyClearingLogDao.update(entity);
	}

	@Override
	public boolean delete(Long clearingid) {
		return proxyClearingLogDao.delete(clearingid);
	}

	@Override
	public String clearing(Map<String, Object> params) throws Exception {
		/**
		 * 结算信息数组
		 * 0：用户ID
		 * 1：占成比例
		 * 2：洗码比例
		 * 3：有效投足额
		 * 4：本月盈亏
		 * 5：本月洗码总额
		 * 6：本月总优惠
		 */
		String[] clearingData =(String[])params.get("clearingInfo");
		int selectMonth = Integer.valueOf(params.get("selectMonth").toString());
		int selectYear = Integer.valueOf(params.get("selectYear").toString());
		for(String cdate : clearingData){
			String[] h = cdate.split("#");
			Map<String, Object> map = new HashMap<String, Object>();
			String uiid = h[0];
			map.put("clearingflaguiid", uiid);
			map.put("clearingflagmonth", selectMonth);
			map.put("clearingflagyear", selectYear);
			List<ProxyClearingEntity> listFlag = proxyClearingFlagDao.getList(map);//查询结算标示记录
			if(null != listFlag && listFlag.size() > 0){
				logger.info("已结算的代理ID->"+uiid+"->"+listFlag.get(0).toString());
				continue;
			}else{
				int ctype = Integer.valueOf(h[7]);//获取结算类型
				params.put("clearingType", ctype);
				String code = proxyjs(h,params,0);
				if(!"0".equals(code)){
					throw new Exception("NEW->代理结算失败->CODE："+code+"->代理ID:"+uiid);
				}
				ProxyClearingEntity entity = new ProxyClearingEntity();
				entity.setClearingflagmonth(String.valueOf(selectMonth));
				entity.setClearingflagyear(String.valueOf(selectYear));
				entity.setClearingflagtype(Integer.valueOf(params.get("clearingType").toString()));
				entity.setClearingflaguiid(Integer.valueOf(uiid));
				entity.setClearingflagtime(DateUtil2.format2(new Date()));
				if(!proxyClearingFlagDao.savebool(entity)){//添加代理标示
					throw new Exception("NEW->更新代理结算标示失败。");
				}
			}
		}
		return "0";
	}
	
	@Override
	public String xima(Map<String, Object> params) throws Exception {
		/**
		 * 结算信息数组
		 * 0：用户ID
		 * 1：占成比例
		 * 2：洗码比例
		 * 3：有效投足额
		 * 4：本月盈亏
		 * 5：本月洗码总额
		 * 6：本月总优惠
		 * 7：代理自助洗码开启状态0未开启，1开启
		 */
		String[] clearingData =(String[])params.get("clearingInfo");
		String jstime = params.get("jstime").toString();
		for(String cdate : clearingData){
			String[] h = cdate.split("#");
			Map<String, Object> map = new HashMap<String, Object>();
			String uiid = h[0];
			map.put("flaguiid", uiid);
			map.put("flagtime", jstime);
			List<ProxyXimaFlag> listFlag = memberInfoDao.selectProxyXimaFlag(map);
			if(null != listFlag && listFlag.size() > 0){
				logger.info("已结算的代理ID->"+uiid+"->"+listFlag.get(0).toString());
				continue;
			}else{
				String code = proxyjs(h,params,1);
				if(!"0".equals(code)){
					throw new Exception("NEW->代理结算失败->CODE："+code+"->代理ID:"+uiid);
				}
				ProxyXimaFlag entity = new ProxyXimaFlag();
				entity.setFlaguiid(Integer.valueOf(uiid));
				entity.setFlagtime(jstime);
				if(!memberInfoDao.insertProxyXimaFlag(entity)){//添加代理标示
					throw new Exception("NEW->更新代理结算标示失败。");
				}
			}
		}
		return "0";
	}
	
	/**
	 * params参数结算信息数组
	 * 0：用户ID
	 * 1：占成比例
	 * 2：洗码比例
	 * 3：有效投足额
	 * 4：本月盈亏
	 * 5：本月洗码总额
	 * 6：本月总优惠
	 * 7：代理自助洗码开启状态0未开启，1开启
	 * jstype 结算类型0月结1天结
	 */
	private String proxyjs(String[] params,Map<String, Object> value,int jstype)throws Exception{
		ProxyClearingLog entity = new ProxyClearingLog();
		/**
		 * 0：输值结算按月
		 * 1：按月洗码
		 * 2：按天洗码
		 */
		int ctype = Integer.valueOf(value.get("clearingType").toString());
		int isximaFlag = Integer.valueOf(params[7]);//自助洗码状态
		if(jstype == 0){//月结
			if(ctype == 0 || ctype==2){//输值结算
				double winLoss = Double.valueOf(params[4]);
				if(winLoss < 0){//当代理的盈亏为负记录数据
					Map<String, Object> logMap = new HashMap<String, Object>();
					logMap.put("uiid", params[0]);
					logMap.put("clearingStatus", 2);
					List<ProxyClearingLog> listLog = proxyClearingLogDao.getList(logMap);
					if(null != listLog && listLog.size() > 0){//将累计的负数减去
						for(ProxyClearingLog obj : listLog){
							//winLoss -= Double.valueOf(obj.getFinalamountTotal());
							winLoss -= Double.valueOf(obj.getRealPL());
						}
					}
					entity.setUiid(Integer.valueOf(params[0]));
					entity.setClearingScale(params[1]);
					entity.setClearingAmount("0.00");
					//entity.setRealPL(String.valueOf(winLoss));
					String win = com.gameportal.manage.util.StringUtils.convertNumber(winLoss-(Double.valueOf(params[5])+Double.valueOf(params[6])));
					entity.setRealPL(win);
					entity.setFinalamountTotal(String.valueOf(winLoss));
					entity.setValidBetAmountTotal(params[3]);
					entity.setXimaAmount(params[5]);
					entity.setPreferentialTotal(params[6]);
					entity.setClearingTime(DateUtil2.format2(new Date()));
					entity.setClearingStartTime(value.get("startDate").toString());//结算开始时间
					entity.setClearingEndTime(value.get("endDate").toString());//结算结束时间
					entity.setClearingType(ctype);
					entity.setClearingStatus(2);//设置为记录状态
					entity.setClearingRemark("代理佣金为负用于累计下个月");
					entity.setUpclient("");
					entity.setUptime(DateUtil2.format2(new Date()));
					entity.setUpuser("");
					if(!save(entity)){
						return "-1";
					}
					return "0";
				}
			}
		}
		double amount = 0.00;//洗码金额
		double realPL = 0.00;//实际盈亏
		if(ctype == 0 || (ctype==2 && jstype == 0)){//输值
			/**
			 * 实际盈亏=总盈亏-(本月总洗码+本月总优惠)
			 */
			realPL = Double.valueOf(params[4])-(Double.valueOf(params[5])+Double.valueOf(params[6]));
			//realPL = Double.valueOf(params[4]);
			if(realPL>0){
				amount = realPL * Double.valueOf(params[1]);
			}
			entity.setClearingScale(params[1]);
		}
		if((ctype == 2 && jstype==1)  || ctype == 1){//洗码
			/**
			 * 实际盈亏
			 */
			//realPL = Double.valueOf(params[3])-(Double.valueOf(params[5])+Double.valueOf(params[6]));
			realPL = Double.valueOf(params[3]);
			amount = realPL * Double.valueOf(params[2]);
			entity.setClearingScale(params[2]);
		}
		entity.setUiid(Integer.valueOf(params[0]));
		entity.setClearingAmount(String.valueOf(amount));
		entity.setRealPL(com.gameportal.manage.util.StringUtils.convertNumber(realPL));
		entity.setFinalamountTotal(params[4]);
		entity.setValidBetAmountTotal(params[3]);
		entity.setXimaAmount(params[5]);
		entity.setPreferentialTotal(params[6]);
		entity.setClearingTime(DateUtil2.format2(new Date()));
		entity.setClearingStartTime(value.get("startDate").toString());//结算开始时间
		entity.setClearingEndTime(value.get("endDate").toString());//结算结束时间
		entity.setClearingType(ctype);
		if(jstype == 1){
			if(ctype==2 && isximaFlag == 1){//表示洗码按天结算
				entity.setClearingStatus(4);
			}
		}
		if(jstype == 0){
			if(amount > 0){
				entity.setClearingStatus(0);
			}else{
				//表示为负盈利
				entity.setClearingStatus(2);
			}
		}
		
		
		entity.setClearingRemark("结算代理佣金");
		entity.setUpclient("");
		entity.setUptime(DateUtil2.format2(new Date()));
		entity.setUpuser("");
		if(!save(entity)){
			return "-1";
		}
		if(ctype == 0 || (ctype==2 && jstype == 0)){
			//清除之前的记录
			Map<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("uiid", params[0]);
			logMap.put("clearingStatus", 2);
			List<ProxyClearingLog> listLog = proxyClearingLogDao.getList(logMap);
			if(null != listLog  && listLog.size() > 0){
				ProxyClearingLog up = new ProxyClearingLog();
				up.setUiid(Integer.valueOf(params[0]));
				if(proxyClearingLogDao.updateStatus(up) <= 0){
					throw new Exception("更新结算记录发生异常.");
				}
			}
		}
		return "0";
	}
	
	public static void main(String[] args) {
		System.out.println(-2-(-1));
	}

	@Override
	public String recorded(ProxyClearingLog entity, Map<String, Object> params)
			throws Exception {
		String uiid = params.get("uiid").toString();
		double rAmount = Double.valueOf(params.get("rAmount").toString());//入账金额
		if(rAmount == Double.valueOf(entity.getClearingAmount())){
			entity.setClearingStatus(1);
			entity.setUpclient(params.get("clientIP").toString());
			entity.setUptime(DateUtil2.format2(new Date()));
			entity.setUpuser(params.get("upusers").toString());
			entity.setClearingRemark(params.get("rRemark").toString());
			if(!update(entity)){
				throw new Exception("系统错误入账失败。");
			}
			params.clear();
			params.put("uiid", uiid);
			AccountMoney accountMoney = accountMoneyDao.getMoneyInfo(params);
			accountMoney.setTotalamount(new BigDecimal(rAmount));
			accountMoney.setUpdateDate(new Date());
			if(!accountMoneyDao.updateTotalamount(accountMoney)){
				throw new Exception("系统错误入账失败：更新用户钱包失败。");
			}
		}else{
			double mo = Double.valueOf(entity.getClearingAmount()) - rAmount;//将金额的一部分入账
			entity.setClearingAmount(String.valueOf(mo));
			entity.setUpclient(params.get("clientIP").toString());
			entity.setUptime(DateUtil2.format2(new Date()));
			entity.setUpuser(params.get("upusers").toString());
			entity.setClearingRemark("已经入账："+rAmount+"元");
			if(!update(entity)){
				throw new Exception("入账"+rAmount+"元失败。");
			}
			entity.setClearingid(null);
			//新添加一条记录
			entity.setClearingAmount(String.valueOf(rAmount));
			entity.setUpclient(params.get("clientIP").toString());
			entity.setUptime(DateUtil2.format2(new Date()));
			entity.setUpuser(params.get("upusers").toString());
			entity.setClearingRemark(params.get("rRemark").toString());
			entity.setClearingStatus(1);
			if(!save(entity)){
				throw new Exception("保存部分入账记录失败。");
			}
			params.clear();
			params.put("uiid", uiid);
			AccountMoney accountMoney = accountMoneyDao.getMoneyInfo(params);
			accountMoney.setTotalamount(new BigDecimal(rAmount));
			accountMoney.setUpdateDate(new Date());
			if(!accountMoneyDao.updateTotalamount(accountMoney)){
				throw new Exception("系统错误入账失败：更新用户钱包失败。");
			}
		}
		return "0";
	}

	@Override
	public ProxyClearingLogTotal clearMoneyTotal(Map<String, Object> params) {
		return proxyClearingLogDao.getSumClearMoney(params);
	}

	@Override
	public String clearing2(Map<String, Object> params) throws Exception {
		/**
		 * 结算信息数组
		 * 0：用户ID
		 * 1：占成比例
		 * 2：洗码比例
		 * 3：有效投足额
		 * 4：本月盈亏
		 * 5：本月洗码总额
		 * 6：本月总优惠
		 */
		String[] clearingData =(String[])params.get("clearingInfo");
		int selectMonth = Integer.valueOf(params.get("selectMonth").toString());
		int selectYear = Integer.valueOf(params.get("selectYear").toString());
		for(String cdate : clearingData){
			String[] h = cdate.split("#");
			Map<String, Object> map = new HashMap<String, Object>();
			String uiid = h[0];
			int ctype = Integer.valueOf(h[7]);//获取结算类型
			map.put("clearingflaguiid", uiid);
			map.put("clearingflagmonth", selectMonth);
			map.put("clearingflagyear", selectYear);
			List<ProxyClearingEntity> listFlag = proxyClearingFlagDao.getList(map);//查询结算标示记录
			if(null != listFlag && listFlag.size() > 0){
				logger.info("已结算的代理ID->"+uiid+"->"+listFlag.get(0).toString());
				continue;
			}else{
				Map<String, Object> reportMap = new HashMap<String, Object>();
				reportMap.put("startdate", params.get("startDate").toString().substring(0, 10));
				reportMap.put("enddate", params.get("endDate").toString().substring(0, 10));
				reportMap.put("proxyuid", uiid);
				ProxyReportEntity report = iProxyInfoService.getProxyFrom(reportMap);
				if(null == report){
					logger.info("没有查询到结算数据ID->"+uiid+"->"+listFlag.get(0).toString());
					continue;
				}
				ProxyClearingLog entityLog = new ProxyClearingLog();
				entityLog.setUiid(Integer.valueOf(uiid));
				if(ctype == 1){//按月洗吗=按代理下线投注额给结算佣金
					entityLog.setClearingScale(h[2]);
					//洗码结算
					double ximacommission = (Double.valueOf(report.getValidBetAmountTotal())
							-Double.valueOf(report.getXimaAmountTotal())
							-Double.valueOf(report.getPreferentialTotal()))*Double.valueOf(h[2]);
					entityLog.setClearingAmount(com.gameportal.manage.util.StringUtils.convertNumber(ximacommission));
					entityLog.setClearingStatus(0);//设置为记录状态
				}else{
					double rmoney = Double.valueOf(report.getCommissionAmount())+Double.valueOf(report.getRecordAmount());
					entityLog.setClearingScale(h[1]);
					entityLog.setClearingAmount(String.valueOf(rmoney));
					entityLog.setClearingStatus(0);
					if(rmoney < 0){
						//当佣金为负盈利的时候将数据设置为记录状态拥挤记录下月数据
						entityLog.setClearingStatus(2);
					}
				}
				entityLog.setRealPL(report.getRealPLs());
				entityLog.setFinalamountTotal(String.valueOf(-Double.valueOf(report.getWinlossTotal())));
				entityLog.setValidBetAmountTotal(report.getBetAmountTotal());
				entityLog.setXimaAmount(report.getXimaAmountTotal());
				entityLog.setPreferentialTotal(report.getPreferentialTotal());
				entityLog.setClearingTime(DateUtil2.format2(new Date()));
				entityLog.setClearingStartTime(params.get("startDate").toString());//结算开始时间
				entityLog.setClearingEndTime(params.get("endDate").toString());//结算结束时间
				entityLog.setClearingType(ctype);
				entityLog.setClearingRemark("代理佣金结算");
				entityLog.setUpclient("");
				entityLog.setUptime(DateUtil2.format2(new Date()));
				entityLog.setUpuser("");
				if(!save(entityLog)){
					throw new Exception("NEW->代理结算失败->代理ID:"+uiid);
				}
				ProxyClearingEntity entity = new ProxyClearingEntity();
				entity.setClearingflagmonth(String.valueOf(selectMonth));
				entity.setClearingflagyear(String.valueOf(selectYear));
				entity.setClearingflagtype(ctype);
				entity.setClearingflaguiid(Integer.valueOf(uiid));
				entity.setClearingflagtime(DateUtil2.format2(new Date()));
				if(!proxyClearingFlagDao.savebool(entity)){//添加代理标示
					throw new Exception("NEW->更新代理结算标示失败。");
				}
			}
			if(ctype != 1){//将之前的记录撤除
				//清除之前的记录
				Map<String, Object> logMap = new HashMap<String, Object>();
				logMap.put("uiid", uiid);
				logMap.put("clearingStatus", 2);
				logMap.put("updateDate", DateUtil2.format2(new Date()));
				List<ProxyClearingLog> listLog = proxyClearingLogDao.getList(logMap);
				if(null != listLog  && listLog.size() > 0){
					ProxyClearingLog up = new ProxyClearingLog();
					up.setUiid(Integer.valueOf(uiid));
					if(proxyClearingLogDao.updateStatus(up) <= 0){
						throw new Exception("更新结算记录发生异常.");
					}
				}
			}
		}
		return "0";
	}

}
