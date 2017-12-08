package com.gameportal.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gameportal.domain.ProxyReportEntity;
import com.gameportal.persistence.ProxyReportEntityMapper;
import com.gameportal.service.IProxyReportEntityService;
import com.gameportal.util.StringUtils;

/**
 * 代理结算详情业务类
 * 
 * @author sum
 *
 */
@Service("proxyReportEntityService")
public class ProxyReportEntityServiceImpl implements IProxyReportEntityService {

	private static Logger logger = Logger.getLogger(ProxyReportEntityServiceImpl.class);

	@Autowired
	private ProxyReportEntityMapper proxyReportEntityMapper;

	@Override
	public ProxyReportEntity getProxyMsg(Map<String, Object> params) {
		List<ProxyReportEntity> list = proxyReportEntityMapper.getUserMsg(params);
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public ProxyReportEntity getProxyFrom(Map<String, Object> params) {
		// 获取代理资料
		ProxyReportEntity userMsg = getProxyMsg(params);
		try {
			// 获取代理推广信息
			ProxyReportEntity spreadInfo = proxyReportEntityMapper.getProxySpreadInfo(params);
			if (null != spreadInfo) {
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
		// 总洗码=手动洗码（赠送）+ 代理下线洗码（代理给下线用户分配洗码金额）+系统洗码（官方给用户洗码）
		double ximatotal = Double.valueOf(userMsg.getSdximaAmount()) + Double.valueOf(userMsg.getProxyXimaAmount());
		userMsg.setXimaAmountTotal(String.valueOf(ximatotal));
		try {
			ProxyReportEntity betInfo = proxyReportEntityMapper.getProxyBetInfo(params);
			if (null != betInfo) {
				userMsg.setBetTotel(betInfo.getBetTotel());
				userMsg.setBetAmountTotal(betInfo.getBetAmountTotal());
				userMsg.setValidBetAmountTotal(betInfo.getValidBetAmountTotal());
				userMsg.setWinlossTotal(betInfo.getWinlossTotal());
			}
		} catch (Exception e) {
			logger.error("代理游戏数据获取失败", e);
		}
		// 计算代理实际盈亏
		double winloss = -Double.valueOf(userMsg.getWinlossTotal());
		//winloss = winloss + Double.valueOf(userMsg.getBuckleAmount());// 输赢+上扣款的金额
		double realYK =0.00;
		double youhui = Double.valueOf(userMsg.getPreferentialTotal());
		if((ximatotal+youhui) >Double.valueOf(userMsg.getBuckleAmount())){
			// 总优惠+总洗码大于总扣款
			//实际盈亏=输赢-(总洗码+总优惠)
			realYK = winloss-(ximatotal+youhui);
		}else{
			// 总优惠+总洗码小于总扣款
			realYK = -Double.valueOf(userMsg.getWinlossTotal());
		}
		userMsg.setRealPLs(StringUtils.convertNumber(realYK));
		// 佣金=实际盈亏*占成比例
		if(userMsg.getReturnscale() ==null){
			userMsg.setCommissionAmount(StringUtils.convertNumber(0));
		}else{
			userMsg.setCommissionAmount(StringUtils.convertNumber(realYK * Double.valueOf(userMsg.getReturnscale())));
		}
		
		return userMsg;
	}
}
