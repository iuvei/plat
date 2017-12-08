package com.gameportal.manage.member.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.exception.ApiException;
import com.gameportal.manage.gameplatform.model.GameAccount;
import com.gameportal.manage.member.model.GameTransfer;
import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.user.model.UserInfo;



public interface IGameTransferService {
	/**
	 * @Title: queryGameAccountList
	 * @Description: (查看用户下的游戏平台帐号信息)
	 * @param uiid
	 * @param startNo
	 * @param pagaSize
	 * @return 设定文件
	 * @return List<GameAccount> 返回类型
	 */
	public List<GameAccount> queryGameAccountList(Long uiid, Integer startNo,
			Integer pagaSize);

	/**
	 * @Title: queryGameAccountList
	 * @Description: (查看用户下的游戏平台帐号信息带状态)
	 * @param uiid
	 * @param status
	 * @param startNo
	 * @param pagaSize
	 * @return List<GameAccount> 返回类型
	 */
	public List<GameAccount> queryGameAccountList(Long uiid, Integer status,
			Integer startNo, Integer pagaSize);

	/**
	 * @Title: savePayOrder
	 * @Description: (存储ATM存款的方法)
	 * @param payOrder
	 * @throws Exception
	 *             设定文件
	 * @return PayOrder 返回类型
	 */
	public PayOrder savePayOrder(PayOrder payOrder) throws Exception;

	public GameAccount queryGameAccountObj(Long uiid, Long gaid, Integer status);

	public String agGameTransfer(Map<String, Object> params) throws ApiException;
	
	public String bbinGameTransfer(Map<String, Object> params) throws ApiException;
	
	public String updateSAGameTransfer(Map<String, Object> params) throws ApiException;
	
	public String ptGameTransfer(Map<String, Object> params) throws ApiException;
	
	public String mgGameTransfer(Map<String, Object> params) throws ApiException;

	public boolean updateGameAccount(GameAccount gameAccount);

	public String modifyCaseout(PayOrder payOrder, UserInfo userInfo,Integer caseoutFigure);
	
	
	/**----------查询玩家转账记录----------------------*/
	public List<GameTransfer> getList(Map<String, Object> params,int thisPage,int pageSize);
	
	public Long getCount(Map<String, Object> params);

	/**
	 * 统计转账记录
	 * @param params where条件
	 * @param thisPage 当前页
	 * @param pageSize 每页行数
	 * @return
	 */
	public List<Map<String, Object>> getTransferForReport(Map<String, Object> params,int thisPage,int pageSize);
	public Long getTransferCountForReport(Map<String, Object> params);
	
	GameTransfer getGameTransfer(Map<String, Object> params);
	
	GameTransfer saveGameTransfer(GameTransfer entity);
	
	boolean updateGameTransfer(GameTransfer entity);
}
