package com.gameportal.service;

import java.util.List;
import java.util.Map;

import com.gameportal.domain.BetLog;
import com.gameportal.domain.BetLogTotal;
import com.gameportal.domain.DownUserReportTotal;
import com.gameportal.domain.MemberInfo;
import com.gameportal.domain.MemberInfoReport;
import com.gameportal.domain.Page;
import com.gameportal.domain.ProxyDomain;
import com.gameportal.domain.ProxySet;
import com.gameportal.domain.ProxyUserXimaLog;
import com.gameportal.domain.UserXimaSet;

/**
 * 会员信息业务接口
 * @author Administrator
 *
 */
public interface IMemberInfoService {
	
	/**
	 * 查询会员信息数据
	 * @param page
	 * @return
	 */
	public MemberInfo queryMemberInfo(Map<String, Object> params);
	
	/**
	 * 查询当前下线人数
	 * @param params
	 * @return
	 */
	public Long queryDownCount(Map<String, Object> params);
	
	/**
	 * 查询代理推广地址
	 * @param pamras
	 * @return
	 */
	public ProxyDomain queryProxyUrl(Map<String, Object> pamras);
	
	/**
	 * 本月总盈亏
	 * @param params
	 * @return
	 */
	public String queryProxyLoss(Map<String, Object> params);
	
	/**
	 * 查询代理下线玩家所有优惠加洗码
	 * @param params
	 * @return
	 */
	public String queryProxyPreferential(Map<String, Object> params);
	
	/**
	 * 查询代理占成比例
	 * @param params
	 * @return
	 */
	public ProxySet queryScaleResult(Map<String, Object> params);
	
	/**
	 * 查询下线本月报表
	 * @param page
	 * @return
	 */
	public List<MemberInfoReport> queryMemberInfoReport(Page page);
	
	/**
	 * 下线本月报表总计
	 * @param params
	 * @return
	 */
	public DownUserReportTotal queryMemberInfoReportTotal(Map<String, Object> params);
	
	/**
	 * 下线会员查询
	 * @param page
	 * @return
	 */
	public List<BetLogTotal> queryDownUser(Page page);
	
	/**
	 * 会员投注明细
	 * @param page
	 * @return
	 */
	public List<BetLog> queryDownUserBetLog(Page page);
	
	/**
	 * 下线洗码明细
	 * @param page
	 * @return
	 */
	public List<ProxyUserXimaLog> queryDownUserXimaLog(Page page);
	
	/**
	 * 查询会员洗码比例设置
	 * @param params
	 * @return
	 */
	public UserXimaSet queryUserXimaSet(Map<String, Object> params);
	
	/**
	 * 修改用户洗码比例
	 * @param userXimaSet
	 * @return
	 */
	public boolean updateUserXimaSet(UserXimaSet userXimaSet) throws Exception;
	
	/**
	 * 新增用户洗码比例
	 * @param userXimaSet
	 * @return
	 */
	public boolean insertUserXimaSet(UserXimaSet userXimaSet);
	
	/**
	 * 查询单个会员游戏输赢
	 * @param map
	 * @return
	 */
	public MemberInfoReport querySingleWinorLess(Map<String, Object> map);
	
	/**
	 * 每日玩家统计
	 * @param params
	 * @return
	 */
	public List<MemberInfo> getPlayerByDate(Map<String, Object> params);
	
	/**
	 * 会员每日投注统计
	 * @param params
	 * @return
	 */
	public BetLogTotal userBetDailyStats(Map<String, Object> params);
	
	/**
	 * 每日交易统计
	 * @param params
	 * @return
	 */
	public List<MemberInfo> getTransByDate(Map<String, Object> params);
	
	/**
	 * 会员每日交易统计
	 * @param params
	 * @return
	 */
	public DownUserReportTotal queryUserDailyReport(Map<String, Object> params);
	
	/**
	 * 查询下线代理
	 * @param params
	 * @return
	 */
	public List<MemberInfo> findlistPageDownProxy(Page page);
}
