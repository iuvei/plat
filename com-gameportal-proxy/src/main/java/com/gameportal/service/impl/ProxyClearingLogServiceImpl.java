package com.gameportal.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gameportal.domain.BetLogTotal;
import com.gameportal.domain.Page;
import com.gameportal.domain.ProxyClearingLog;
import com.gameportal.domain.ProxyUserXimaLog;
import com.gameportal.persistence.ProxyClearingLogMapper;
import com.gameportal.service.BaseService;
import com.gameportal.service.IProxyClearingLogService;
import com.gameportal.util.DateUtil2;
import com.gameportal.util.StringUtils;

/**
 * 代理结算记录
 * @author leron
 *
 */
@SuppressWarnings("all")
@Service("proxyClearingLogService")
public class ProxyClearingLogServiceImpl extends BaseService implements
		IProxyClearingLogService {

	@Autowired
	private ProxyClearingLogMapper clearingLogMapper;
	
	@Override
	public List<ProxyClearingLog> queryProxyClearingLog(Page page) {
		return clearingLogMapper.findlistPage(page);
	}
	
	@Override
	public ProxyClearingLog queryProxyClearingLogTotal(
			Map<String, Object> params) {
		return clearingLogMapper.proxyClearingLogTotal(params);
	}
	
	@Override
	public  String saveXima(Map<String, Object> params) throws Exception{
		    Long logid = (Long)params.get("clearingid");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("clearingid", logid);
			map.put("clearingStatus", 4);
			ProxyClearingLog proxyClearingLog=clearingLogMapper.findByMap(params);
			if(proxyClearingLog==null){
				return "400";//数据不存在或不是可洗码状态
			}
			Integer clearingId=proxyClearingLog.getClearingid();
			Long puiid = (Long)params.get("puiid");//代理ID
			String datetime = params.get("jsdate").toString()+" 00:00:00";
			String endDate = datetime.substring(0,10)+ " 23:59:59";
			map.clear();
			map.put("uiid", puiid);
			map.put("startDate", datetime);
			map.put("endDate", endDate);
			map.put("flag", 1);
			List<BetLogTotal> betList=clearingLogMapper.selectProxyDownUserXima(map);
			if(null == betList || betList.size() <= 0){
				return "404";//没有找到可洗码的下线
			}
			double moneyTotal = 0.00;//结算给下线的总金额
			for(BetLogTotal obj : betList){
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
				proxyUserXimaLog.setXimamoney(StringUtils.convertNumber(ximaMoney));
				proxyUserXimaLog.setYhmoney(obj.getPreferentialTotal());
				proxyUserXimaLog.setValidmoney(obj.getValidBetAmountTotal());
				proxyUserXimaLog.setXimatime(DateUtil2.format2(new Date()));
				proxyUserXimaLog.setXimascale(obj.getXimascale());
				proxyUserXimaLog.setStatus(0);
				Long logids=clearingLogMapper.insertProxyUserXimaLog(proxyUserXimaLog);
				if(logids==null || logids<=0){
					throw new Exception("自助洗码异常，添加洗码记录失败。");
				}
				moneyTotal += ximaMoney;
			}
			double mTotal = Double.valueOf(proxyClearingLog.getClearingAmount());//未分配前的洗码金额
			ProxyClearingLog addProxyClearingLog = proxyClearingLog;
			addProxyClearingLog.setClearingAmount(StringUtils.convertNumber(moneyTotal));
			addProxyClearingLog.setClearingStatus(6);//洗码记录
			addProxyClearingLog.setClearingTime(DateUtil2.format2(new Date()));
			addProxyClearingLog.setClearingRemark("结算给下线的总洗码金额");
			int clearid=clearingLogMapper.insertProxyClearingLog(addProxyClearingLog);
			if(clearid<=0){
				throw new Exception("自助洗码异常，添加总洗码记录失败。");
			}
			Double camoney = mTotal - moneyTotal;//分配后的余额
			proxyClearingLog.setClearingid(clearingId);
			proxyClearingLog.setClearingAmount(camoney.toString());
			proxyClearingLog.setClearingStatus(5);
			proxyClearingLog.setClearingRemark("已分配"+moneyTotal+"元，给下线会员。");
			int count=clearingLogMapper.updateProxyClearingLog(proxyClearingLog);
			if(count<=0){
				throw new Exception("自助洗码异常，更新洗码主记录失败。");
			}
			return "0";
	}

}
