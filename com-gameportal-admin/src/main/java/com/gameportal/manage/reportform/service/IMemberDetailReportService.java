package com.gameportal.manage.reportform.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.member.model.GameTransfer;
import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.xima.model.MemberXimaMain;

/**
 * 会员明细报表业务接口
 * @author Administrator
 *
 */
public interface IMemberDetailReportService {

	/**
	 * 查询钱包余额
	 * @param map 条件
	 * @return
	 */
	public Map<String,Object> getAccountMoney(Map<String, Object> map);
	
	/**
	 * 存款/取款/优惠列表
	 * @param map  条件	
	 * @param startNo 当前页
	 * @param pageSize 每页行数
	 * @return
	 */
	public List<PayOrder> getPayMoneyList(Map<String, Object> map,Integer startNo,Integer pageSize);
	
	/**
	 * 存款/取款/优惠总计
	 * @param map 条件
	 * @return
	 */
	public Long getPayMoneyCount(Map<String, Object> map);
	
	/**
	 * 转账列表
	 * @param map  条件	
	 * @param startNo 当前页
	 * @param pageSize 每页行数
	 * @return
	 */
	public List<GameTransfer> getTransferList(Map<String, Object> map,Integer startNo,Integer pageSize);
	
	/**
	 * 转账总计
	 * @param map 条件
	 * @return
	 */
	public Long getTransferCount(Map<String, Object> map);
	
	/**
	 * 会员洗码列表
	 * @param map  条件	
	 * @param startNo 当前页
	 * @param pageSize 每页行数
	 * @return
	 */
	public List<MemberXimaMain> getMemberXimaList(Map<String, Object> map,Integer startNo,Integer pageSize);
	
	/**
	 * 会员洗码总计
	 * @param map 条件
	 * @return
	 */
	public Long getMemberXimaCount(Map<String, Object> map);
}
