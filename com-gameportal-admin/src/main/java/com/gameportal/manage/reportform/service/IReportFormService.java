package com.gameportal.manage.reportform.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.member.model.DailyReportDetail;
import com.gameportal.manage.reportform.model.ReportForm;

/**
 * 报表统计业务层接口
 * @author Administrator
 *
 */
public interface IReportFormService {

	/**
	 * 报表统计集合
	 * @param params  条件
	 * @param startNo 当前页
	 * @param pageSize 每页总数
	 * @return
	 */
	public List<ReportForm> getReportResult(Map<String, Object> params,Integer startNo,Integer pageSize);
	
	/**
	 * 报价统计总数
	 * @param params 条件
	 * @return
	 */
	public Long getReportCount(Map<String, Object> params);
	
	/**
	 * 查询当天首次总金额
	 * @param nowDate 日期(YY-MM-DD)
	 * @return
	 */
	public String getFirstPayMoney(String nowDate);
	
	/**
	 * 查询当天投注总额
	 * @param nowDate 日期(YY-MM-DD)
	 * @return
	 */
	public String getRealBetMoney(String nowDate);
	
	/**
	 * 查询报表做导出
	 * @param params 条件
	 * @return
	 */
	public List<ReportForm> getReportResult(Map<String, Object> params);
	
	/**
	 * 查询报表做导出(历史)
	 * @param params 条件
	 * @return
	 */
	public List<ReportForm> getHistoryReportResult(Map<String, Object> params);
	
	/**
	 * 查询注册人数集合
	 * @param params 查询条件
	 * @param startNo 当前页
	 * @param pageSize 每页数量
	 * @return
	 */
	public List<Map<String, Object>> getRegisterNumberResult(Map<String, Object> params,Integer startNo,Integer pageSize);
	
	/**
	 *  查询注册人数总数
	 * @param params 条件
	 * @return
	 */
	public Long getRegisterNumberCount(Map<String, Object> params);
	
	/**
	 * 查询首冲人数集合
	 * @param params  查询条件
	 * @param startNo 当前页
	 * @param pageSize 每页数量
	 * @return
	 */
	public List<Map<String, Object>> getFirstPayNumberResult(Map<String, Object> params,Integer startNo,Integer pageSize);
	
	/**
	 * 查询首冲人数总数
	 * @param params 查询条件
	 * @return
	 */
	public Long getFirstPayNumberCount(Map<String, Object> params);
	
	
	/**
	 * 实体投注额集合
	 * @param params  查询条件
	 * @param startNo 当前页
	 * @param pageSize 每页数量
	 * @return
	 */
	public List<Map<String, Object>> getBetMoneyResult(Map<String, Object> params,Integer startNo,Integer pageSize);
	
	/**
	 * 实体投注额总数
	 * @param params 查询条件
	 * @return
	 */
	public Long getBetMoneyCount(Map<String, Object> params);
	
	
	/**
	 * 会员/代理优惠集合
	 * @param params  查询条件
	 * @param startNo 当前页
	 * @param pageSize 每页数量
	 * @return
	 */
	public List<Map<String, Object>> getMemberCouponResult(Map<String, Object> params,Integer startNo,Integer pageSize);
	
	/**
	 * 会员/代理优惠总数
	 * @param params 查询条件
	 * @return
	 */
	public Long getMemberCouponCount(Map<String, Object> params);
	
	/**
	 * 存款/取款集合
	 * @param params  查询条件
	 * @param startNo 当前页
	 * @param pageSize 每页数量
	 * @return
	 */
	public List<Map<String, Object>> getPayMoneyResult(Map<String, Object> params,Integer startNo,Integer pageSize);
	
	
	/**
	 * 游戏输赢统计
	 * @param params  查询条件
	 * @param startNo 当前页
	 * @param pageSize 每页数量
	 * @return
	 */
	public List<Map<String, Object>> getwinorlessReport(Map<String, Object> params,Integer startNo,Integer pageSize);
	
	/**
	 * 存款/取款总数
	 * @param params 查询条件
	 * @return
	 */
	public Long getPayMoneyCount(Map<String, Object> params);

	/**
	 * 从平台投注统计表中查询选择日期的投注统计
	 * @param map 存选择的日期
	 * @param startNo 起始页码
	 * @param pageSize 一页的数量
	 * @return
	 */
	public List<Map<String, Object>> getPlatformBetStats(Map<String, Object> map, Integer startNo, Integer pageSize);
	
	/**
	 * 查询活跃人数。
	 * @param params
	 * @return
	 */
	public Long selectActiveCount(Map<String, Object> params);
	
	/**
	 * 查询正常登录人数。
	 * @param params
	 * @return
	 */
	public Long selectLoginNumberCount(Map<String,Object> params);
	
	/**
	 * 统计每日报表数据。
	 * @param report
	 */
	public void insertStatDailyReport(DailyReportDetail report);
}
   