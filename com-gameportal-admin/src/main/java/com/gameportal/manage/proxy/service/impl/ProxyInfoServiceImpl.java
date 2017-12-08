package com.gameportal.manage.proxy.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.fcrecord.dao.FcRecordDao;
import com.gameportal.manage.proxy.dao.ProxyInfoDao;
import com.gameportal.manage.proxy.model.ProxyInfo;
import com.gameportal.manage.proxy.model.ProxyReportEntity;
import com.gameportal.manage.proxy.service.IProxyInfoService;

@Service
public class ProxyInfoServiceImpl implements IProxyInfoService {

	@Resource(name = "proxyInfoDao")
	private  ProxyInfoDao  proxyInfoDao = null;
	@Resource(name = "fcRecordDao")
	private FcRecordDao fcRecordDao;
	private Logger logger = Logger.getLogger(ProxyInfoServiceImpl.class);// 日志对象
	
	public ProxyInfoServiceImpl() {
		super();
	}
	
	@Override
	public ProxyInfo queryProxyInfoById(Long pyid) {
		return (ProxyInfo)proxyInfoDao.findById(pyid);
	}

	@Override
	public List<ProxyInfo> queryProxyInfo(Long pyid, Long  parentid,String name,
			Integer startNo, Integer pageSize) {
		return queryProxyInfo( pyid,   parentid, name, null,  startNo,  pageSize);
	}
	

	@Override
	public List<ProxyInfo> queryProxyInfo(Long pyid, Long  parentid, String name,
			Integer status, Integer startNo, Integer pageSize) {
		Map params = new HashMap();
		if(StringUtils.isNotBlank(ObjectUtils.toString(pyid))){
			params.put("pyid", pyid);
		}
		
		if(StringUtils.isNotBlank(ObjectUtils.toString(name))){
			params.put("name", name);
		}
		
		if(StringUtils.isNotBlank(ObjectUtils.toString(status))){
			params.put("status", status);
		}
		
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		return proxyInfoDao.queryForPager(params, startNo, pageSize);
	}

	@Override
	public Long queryProxyInfoCount(Long pyid, Long  parentid, String name) {
		return queryProxyInfoCount( pyid,   parentid, name,  null, null,  null,  null,
				null,  null);
	}

	@Override
	public Long queryProxyInfoCount(Long pyid, Long  parentid, String name, Integer status) {
		return queryProxyInfoCount( pyid,   parentid, name,  null, null,  null,  null,
				null,  status);
	}

	@Override
	public Long queryProxyInfoCount(Long pyid, Long  parentid, String name, String domainname,
			String platformkey, String ciphercode, String returnUrl,
			String noticeUrl, Integer status) {
		Map<String, Object> params = new HashMap<>();
		if(StringUtils.isNotBlank(ObjectUtils.toString(pyid))){
			params.put("pyid", pyid);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(parentid))){
			params.put("parentid", parentid);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(name))){
			params.put("name", name);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(domainname))){
			params.put("domainname", domainname);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(platformkey))){
			params.put("platformkey", platformkey);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(ciphercode))){
			params.put("ciphercode", ciphercode);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(returnUrl))){
			params.put("returnUrl", returnUrl);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(noticeUrl))){
			params.put("noticeUrl", noticeUrl);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(status))){
			params.put("status", status);
		}
		return proxyInfoDao.getRecordCount(params);
	}

	@Override
	public ProxyInfo saveProxyInfo(ProxyInfo proxyInfo) throws Exception {
//		proxyInfo = (ProxyInfo) proxyInfoDao.save(proxyInfo);
//		return StringUtils.isNotBlank(ObjectUtils.toString(proxyInfo
//				.getPyid())) ? proxyInfo : null;
		return null;
	}

	@Override
	public boolean saveOrUpdateProxyInfo(ProxyInfo proxyInfo) throws Exception {
		return proxyInfoDao.saveOrUpdate(proxyInfo);
	}

	@Override
	public boolean deleteProxyInfo(Long pyid) throws Exception {
		return proxyInfoDao.delete(pyid);
	}

	@Override
	public List<ProxyInfo> queryProxyInfo(Map<String, Object> params,
			Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 20;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return proxyInfoDao.getList(params);
	}

	@Override
	public Long queryProxyInfoCount(Map<String, Object> params) {
		return proxyInfoDao.getRecordCount(params);
	}

	@Override
	public ProxyReportEntity getProxyFrom(Map<String, Object> params) {
		//获取代理资料
		ProxyReportEntity userMsg = proxyInfoDao.getUserMsg(params);
		try {
			//获取代理推广信息
			ProxyReportEntity spreadInfo = proxyInfoDao.getProxySpreadInfo(params);
			if(null != spreadInfo){
				userMsg.setLowecCount(spreadInfo.getLowecCount());
				userMsg.setActiveUser(spreadInfo.getActiveUser());
				userMsg.setPayusercount(spreadInfo.getPayusercount());
				userMsg.setPaycount(spreadInfo.getPaycount());
				userMsg.setPayAmountTotal(spreadInfo.getPayAmountTotal());
				userMsg.setAtmusercount(spreadInfo.getAtmusercount());
				userMsg.setAtmcount(spreadInfo.getAtmcount());
				userMsg.setAtmAmountTotal(spreadInfo.getAtmAmountTotal());
				userMsg.setPreferentialTotal(spreadInfo.getPreferentialTotal());
				userMsg.setSdximaAmount(spreadInfo.getSdximaAmount());
				userMsg.setBuckleAmount(spreadInfo.getBuckleAmount());
				userMsg.setProxyXimaAmount(spreadInfo.getProxyXimaAmount());
				userMsg.setSysXimaAmount(spreadInfo.getSysXimaAmount());
				userMsg.setRecordAmount(spreadInfo.getRecordAmount());
			}
			// 查询首存金额。
			params.put("pname", userMsg.getProxyaccount());
			Long fcAmount = fcRecordDao.sumFcMoney(params);
			userMsg.setFcCount(String.valueOf(fcRecordDao.countFcRecord(params)));
			userMsg.setFcAmount(String.valueOf(fcAmount));
			params.put("paytyple", 0);
			params.put("status", 3);
			// 查询首充会员存款总数。
			userMsg.setFcTotalAmount(String.valueOf(proxyInfoDao.selectProxyFcPayOrderTotal(params)-fcAmount));
		} catch (Exception e) {
			logger.error("代理推广信息获取失败", e);
		}
		//总洗码=手动洗码（赠送）+ 代理下线洗码（代理给下线用户分配洗码金额）+系统洗码（官方给用户洗码）
		double ximatotal = Double.valueOf(userMsg.getSdximaAmount())+Double.valueOf(userMsg.getProxyXimaAmount());
		userMsg.setXimaAmountTotal(String.valueOf(ximatotal));
		try {
			ProxyReportEntity betInfo = proxyInfoDao.getProxyBetInfo(params);
			if(null != betInfo){
				userMsg.setBetTotel(betInfo.getBetTotel());
				userMsg.setBetAmountTotal(betInfo.getBetAmountTotal());
				userMsg.setValidBetAmountTotal(betInfo.getValidBetAmountTotal());
				userMsg.setWinlossTotal(betInfo.getWinlossTotal());
			}
		} catch (Exception e) {
			logger.error("代理游戏数据获取失败", e);
		}
		//计算代理实际盈亏
		double winloss = -Double.valueOf(userMsg.getWinlossTotal());
//		winloss = winloss+Double.valueOf(userMsg.getBuckleAmount());//输赢+上扣款的金额
		
		double realYK = 0.00;
		//优惠金额
		double youhui = Double.valueOf(userMsg.getPreferentialTotal());
		if((ximatotal+youhui) >Double.valueOf(userMsg.getBuckleAmount())){
			// 总优惠+总洗码大于总扣款
			//实际盈亏=输赢-(总洗码+总优惠)
			realYK = winloss-(ximatotal+youhui);
		}else{
			// 总优惠+总洗码小于总扣款
			realYK =  -Double.valueOf(userMsg.getWinlossTotal());
		}
		userMsg.setRealPLs(com.gameportal.manage.util.StringUtils.convertNumber(realYK));
		//佣金=实际盈亏*占成比例
		double commission = realYK * Double.valueOf(userMsg.getReturnscale());
		userMsg.setCommissionAmount(com.gameportal.manage.util.StringUtils.convertNumber(commission));
		return userMsg;
	}
	
	@Override
	public ProxyReportEntity getProxyClearing(Map<String, Object> params) {
		ProxyReportEntity userMsg = new ProxyReportEntity();
		try {
			//获取代理推广信息
			ProxyReportEntity spreadInfo = proxyInfoDao.getProxySpreadInfo(params);
			if(null != spreadInfo){
				userMsg.setLowecCount(spreadInfo.getLowecCount());
				userMsg.setActiveUser(spreadInfo.getActiveUser());
				userMsg.setPayusercount(spreadInfo.getPayusercount());
				userMsg.setPaycount(spreadInfo.getPaycount());
				userMsg.setPayAmountTotal(spreadInfo.getPayAmountTotal());
				userMsg.setAtmusercount(spreadInfo.getAtmusercount());
				userMsg.setAtmcount(spreadInfo.getAtmcount());
				userMsg.setAtmAmountTotal(spreadInfo.getAtmAmountTotal());
				userMsg.setPreferentialTotal(spreadInfo.getPreferentialTotal());
				userMsg.setSdximaAmount(spreadInfo.getSdximaAmount());
				userMsg.setBuckleAmount(spreadInfo.getBuckleAmount());
				userMsg.setProxyXimaAmount(spreadInfo.getProxyXimaAmount());
				userMsg.setSysXimaAmount(spreadInfo.getSysXimaAmount());
				userMsg.setRecordAmount(spreadInfo.getRecordAmount());
			}
		} catch (Exception e) {
			logger.error("代理推广信息获取失败", e);
		}
		//总洗码=手动洗码（赠送）+ 代理下线洗码（代理给下线用户分配洗码金额）+系统洗码（官方给用户洗码）
		double ximatotal = Double.valueOf(userMsg.getSdximaAmount())+Double.valueOf(userMsg.getProxyXimaAmount());
		userMsg.setXimaAmountTotal(String.valueOf(ximatotal));
		try {
			ProxyReportEntity betInfo = proxyInfoDao.getProxyBetInfo(params);
			if(null != betInfo){
				userMsg.setBetTotel(betInfo.getBetTotel());
				userMsg.setBetAmountTotal(betInfo.getBetAmountTotal());
				userMsg.setValidBetAmountTotal(betInfo.getValidBetAmountTotal());
				userMsg.setWinlossTotal(betInfo.getWinlossTotal());
			}
		} catch (Exception e) {
			logger.error("代理游戏数据获取失败", e);
		}
		//计算代理实际盈亏
		double winloss = -Double.valueOf(userMsg.getWinlossTotal());
		winloss = winloss+Double.valueOf(userMsg.getBuckleAmount());//输赢+上扣款的金额
		
		//实际盈亏=输赢-(总洗码+总优惠)
		double realYK = 0.00;
		if(winloss > 0){
			//正数相减
			realYK = winloss-(ximatotal+Double.valueOf(userMsg.getPreferentialTotal()));
		}else{
			//负数相加
			realYK = winloss+(ximatotal+Double.valueOf(userMsg.getPreferentialTotal()));
		}
		userMsg.setRealPLs(String.valueOf(realYK));
		//佣金=实际盈亏*占成比例
		double commission = realYK * Double.valueOf(userMsg.getReturnscale());
		userMsg.setCommissionAmount(com.gameportal.manage.util.StringUtils.convertNumber(commission));
		return userMsg;
	}

	
	public static void main(String[] args) {
		double winloss = -Double.valueOf("500");
		winloss = winloss+Double.valueOf(0);//输赢+上扣款的金额
		double youhui = 100;
		double ximatotal = 0;
		//实际盈亏=输赢-(总洗码+总优惠)
		double realYK = 0.00;
		realYK = winloss-(ximatotal+youhui);
		System.out.println(realYK);
	}

}
