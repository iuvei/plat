package com.gameportal.web.game.service;

import java.util.List;
import java.util.Map;

import com.gameportal.web.game.model.GameAccount;
import com.gameportal.web.game.model.GameTransfer;
import com.gameportal.web.game.util.ApiException;
import com.gameportal.web.user.model.PayOrder;
import com.gameportal.web.user.model.UserInfo;

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
	
	/**
	 * 更新存款订单。
	 * @param payOrder
	 */
	void updatePayOrder(PayOrder payOrder);
	/**
	 * 查询订单信息。
	 * @param param
	 * @return
	 * @throws Exception
	 */
	List<PayOrder> getPayOrder(Map<String, Object> param) throws Exception;

	public GameAccount queryGameAccountObj(Long uiid, Long gaid, Integer status);

	public String updatePTGameTransfer(Map<String, Object> params) throws ApiException;
	
	public String updateBBINGameTransfer(Map<String, Object> params) throws ApiException;
	
	public String updateAGGameTransfer(Map<String, Object> params) throws ApiException;
	
	public String updateSAGameTransfer(Map<String, Object> params) throws ApiException;
	
	public String updateMGGameTransfer(Map<String, Object> params) throws ApiException;
	
	public boolean updateGameAccount(GameAccount gameAccount);

	public String modifyCaseout(PayOrder payOrder, UserInfo userInfo,Integer caseoutFigure) throws Exception;
	
	/**
	 * 查询转账订单
	 * 
	 * @return
	 */
	List<GameTransfer> selectTranferOrder(Map<String, Object> params);

	/**
	 * 统计转账数量
	 * 
	 * @param params
	 * @return
	 */
	Long selectTranferOrderCount(Map<String, Object> params);
	
	/**
	 * 保存转账记录
	 * @param entity
	 * @return
	 */
	GameTransfer saveGameTransfer(GameTransfer entity);
	
	/**
	 * 更新转账记录
	 * @param entity
	 */
	void updateGameTransfer(GameTransfer entity);
	
	/**
	 * 查询转账记录
	 * @param params
	 * @return
	 */
	GameTransfer getGameTransfer(Map<String, Object> params);
}
