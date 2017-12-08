package com.gameportal.persistence;

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
 * 会员信息DAO
 * @author leron
 *
 */
public interface MemberInfoMapper {
	
	/**
	 * 查询会员信息
	 * @param params
	 * @return
	 */
	public List<MemberInfo> selectMemberInfo(Map<String, Object> params);
	
	/**
	 * 查询当前下线人数
	 * @param params
	 * @return
	 */
	public Long selectDownCount(Map<String, Object> params);
	
	/**
	 * 查询代理推广地址
	 * @param params
	 * @return
	 */
	public List<ProxyDomain> selectProxyUrl(Map<String, Object> params);
	
	/**
	 * 统计本月总盈亏
	 * @param params
	 * @return
	 */
	public String getProxyLoss(Map<String, Object> params);
	
	/**
	 * 查询代理下线玩家所有优惠加洗码
	 * @param params
	 * @return
	 */
	public String getProxyPreferential(Map<String, Object> params);
	
	/**
	 * 查询代理占成
	 * @param params
	 * @return
	 */
	public List<ProxySet> getScaleResult(Map<String, Object> params);
	
	/**
	 * 下线本月报表
	 * @param page
	 * @return
	 */
	public List<MemberInfoReport> findlistPageMonthReport(Page page);
	
	/**
	 * 下线本月报表总计
	 * @param params
	 * @return
	 */
	public DownUserReportTotal downUserMonthReportTotal(Map<String, Object> params);
	
	/**
	 * 下线会员查询
	 * @param page
	 * @return
	 */
	public List<BetLogTotal> findlistPageDownUser(Page page);
	
	/**
	 * 会员投注明细
	 * @param page
	 * @return
	 */
	public List<BetLog> findlistPageDownUserDetail(Page page);
	
	/**
	 * 下线洗码明细
	 * @param page
	 * @return
	 */
	public List<ProxyUserXimaLog> findlistPageMemberXimaLog(Page page);
	
	/**
	 * 查询用户洗码设置比例
	 * @param params
	 * @return
	 */
	public UserXimaSet findUserXimaSet(Map<String, Object> params);
	
	/**
	 * 修改会员洗码比例
	 * @param userXimaSet
	 * @return
	 */
	public int updateXimaScale(UserXimaSet userXimaSet);
	
	/**
	 * 新增会员洗码比例
	 * @param userXimaSet
	 * @return
	 */
	public int insertUserXimaSet(UserXimaSet userXimaSet);
	
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
	 * 会员每日投注统计
	 * @param params
	 * @return
	 */
	public BetLogTotal userBetDailyStats(Map<String, Object> params);
	
	/**
	 * 查询下线代理
	 * @param params
	 * @return
	 */
	public List<MemberInfo> findlistPageDownProxy(Page page);
}
