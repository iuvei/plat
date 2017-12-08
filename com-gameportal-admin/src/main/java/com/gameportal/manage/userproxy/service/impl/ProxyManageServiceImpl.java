package com.gameportal.manage.userproxy.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gameportal.manage.betlog.dao.BetLogDao;
import com.gameportal.manage.betlog.model.BetLogTotal;
import com.gameportal.manage.proxy.dao.ProxyClearingLogDao;
import com.gameportal.manage.proxy.dao.ProxyUserXimaDao;
import com.gameportal.manage.proxy.model.ProxyClearingLog;
import com.gameportal.manage.proxy.model.ProxyUserXimaLog;
import com.gameportal.manage.userproxy.service.IProxyManageService;
import com.gameportal.manage.util.DateUtil2;
@Service("proxyManageService")
public class ProxyManageServiceImpl implements IProxyManageService{

	@Resource(name = "betLogDao")
	private BetLogDao betLogDao = null;
	
	@Resource(name = "proxyClearingLogDao")
	private ProxyClearingLogDao proxyClearingLogDao;
	
	@Resource(name = "proxyUserXimaDao")
	private ProxyUserXimaDao proxyUserXimaDao;
	
	@Override
	public List<BetLogTotal> selectProxyDownUser(Map<String, Object> params,
			int thisPage, int pageSize) {
		params.put("limit", true);
		params.put("thisPage", thisPage);
		params.put("pageSize", pageSize);
		return betLogDao.selectProxyDownUser(params, thisPage, pageSize);
	}

	@Override
	public Long selectProxyDownUserCount(Map<String, Object> params) {
		return betLogDao.selectProxyDownUserCount(params);
	}

	@Override
	public String ximajs(Map<String, Object> params) throws Exception{
		Long logid = (Long)params.get("clearingid");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("clearingid", logid);
		map.put("clearingStatus", 4);
		ProxyClearingLog proxyClearingLog = proxyClearingLogDao.getObject(map);
		if(proxyClearingLog == null){
			return "400";//数据不存在或不是可洗码状态
		}
		Long puiid = (Long)params.get("puiid");//代理ID
		String datetime = params.get("jsdate").toString()+" 00:00:00";
		String endDate = datetime.substring(0,10)+ " 23:59:59";
		map.clear();
		map.put("uiid", puiid);
		map.put("startDate", datetime);
		map.put("endDate", endDate);
		map.put("flag", 1);
		List<BetLogTotal> list = betLogDao.selectProxyDownUserXima(map);
		if(null == list || list.size() <= 0){
			return "404";//没有找到可洗码的下线
		}
		double moneyTotal = 0.00;//结算给下线的总金额
		for(BetLogTotal obj : list){
			double yhmoney = Double.valueOf(obj.getPreferentialTotal());//优惠金额
			double validBetAmountTotal = Double.valueOf(obj.getValidBetAmountTotal());//有效金额
			double scale = Double.valueOf(obj.getXimascale());//洗码比例
			double realBetMoney = validBetAmountTotal;//实际投注金额=有效金额-优惠金额
			double ximaMoney = realBetMoney * scale;
			ProxyUserXimaLog proxyUserXimaLog = new ProxyUserXimaLog();
			proxyUserXimaLog.setUiid(obj.getUiid());
			proxyUserXimaLog.setAccount(obj.getAccount());
			proxyUserXimaLog.setUname(obj.getUname());
			proxyUserXimaLog.setPuiid(puiid.intValue());
			proxyUserXimaLog.setJsstarttime(datetime);
			proxyUserXimaLog.setJsendtime(endDate);
			proxyUserXimaLog.setXimamoney(com.gameportal.manage.util.StringUtils.convertNumber(ximaMoney));
			proxyUserXimaLog.setYhmoney(obj.getPreferentialTotal());
			proxyUserXimaLog.setValidmoney(obj.getValidBetAmountTotal());
			proxyUserXimaLog.setXimatime(DateUtil2.format2(new Date()));
			proxyUserXimaLog.setXimascale(obj.getXimascale());
			proxyUserXimaLog.setStatus(0);
			if(!proxyUserXimaDao.save(proxyUserXimaLog)){
				throw new Exception("自助洗码异常，添加洗码记录失败。");
			}
			moneyTotal += ximaMoney;
		}
		double mTotal = Double.valueOf(proxyClearingLog.getClearingAmount());//未分配前的洗码金额
		ProxyClearingLog addProxyClearingLog = proxyClearingLog;
		addProxyClearingLog.setClearingAmount(com.gameportal.manage.util.StringUtils.convertNumber(moneyTotal));
		addProxyClearingLog.setClearingStatus(6);//洗码记录
		addProxyClearingLog.setClearingTime(DateUtil2.format2(new Date()));
		addProxyClearingLog.setClearingRemark("结算给下线的总洗码金额");
		if(!proxyClearingLogDao.save(addProxyClearingLog)){
			throw new Exception("自助洗码异常，添加总洗码记录失败。");
		}
		Double camoney = mTotal - moneyTotal;//分配后的余额
		proxyClearingLog.setClearingAmount(camoney.toString());
		proxyClearingLog.setClearingStatus(5);
		proxyClearingLog.setClearingRemark("已分配"+moneyTotal+"元，给下线会员。");
		if(!proxyClearingLogDao.update(proxyClearingLog)){
			throw new Exception("自助洗码异常，更新洗码主记录失败。");
		}
		return "0";
	}

}
