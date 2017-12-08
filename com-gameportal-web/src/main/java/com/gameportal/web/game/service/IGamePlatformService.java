package com.gameportal.web.game.service;

import java.util.List;
import java.util.Map;

import com.gameportal.web.game.model.AGElectronic;
import com.gameportal.web.game.model.AGINElectronic;
import com.gameportal.web.game.model.GameAccount;
import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.game.model.MGElectronic;
import com.gameportal.web.game.model.PlatformBetStats;
import com.gameportal.web.game.model.SAElectronic;

public interface IGamePlatformService {
	/**
	 * @Title: queryGamePlatform
	 * @Description: (按ID查询游戏平台配置信息)
	 * @param gpid
	 *            游戏平台ID
	 * @return GamePlatform 返回类型
	 */
	public GamePlatform queryGamePlatform(String gpname);

	/**
	 * @Title: queryGamePlatform
	 * @Description: (按ID与状态查询游戏平台配置信息)
	 * @param gpid
	 * @param status
	 * @return 设定文件
	 * @return GamePlatform 返回类型
	 */
	public GamePlatform queryGamePlatform(String gpname, Integer status);

	/**
	 * @Title: isExistAccount
	 * @Description: (用户在游戏平台是否存在)
	 * @param uiid
	 *            用户ID
	 * @param type
	 *            平台类型ID
	 * @return 设定文件
	 * @return boolean 返回类型
	 */
	public boolean isExistAccount(Long uiid, String type);

	public boolean isExistAccount(Long uiid, String type, Integer status);
	
	public List<GamePlatform> queryGame(Map<String, Object> params);
	
	public List<MGElectronic> queryMPT(Map<String, Object> params);

	public GameAccount saveGameAccount(GameAccount gameAccount)
			throws Exception;
	
	public void saveMGElectronic(MGElectronic entity);
	
	public Long queryElectronicCount(Map<String, Object> params);
	public List<MGElectronic> queryElectronic(Map<String, Object> params,int startNo,int pagaSize);
	
	/**
	 * 查询电子游戏信息指定类型查询
	 * @param params
	 * @return
	 */
	public List<MGElectronic> queryElectronic(Map<String, Object> params);
	
	
	
	public Long queryAgElectronicCount(Map<String, Object> params);
	
	/**
	 * 查询(NMG)AG电子游戏信息指定类型查询
	 * @param params
	 * @return
	 */
	public List<AGElectronic> queryAGElectronic(Map<String, Object> params);
	
	
	public Long queryAginElecCount(Map<String, Object> params);
	
	/**
	 * 查询AGIN电子游戏信息指定类型查询
	 * @param params
	 * @return
	 */
	public List<AGINElectronic> queryAginElec(Map<String, Object> params);
	
	
	public Long querySAElecCount(Map<String, Object> params);
	
	/**
	 * 查询SA电子游戏信息指定类型查询
	 * @param params
	 * @return
	 */
	public List<SAElectronic> querySAElec(Map<String, Object> params);

	/**
	 * 统计游戏平台的投注
	 */
	public void saveStatsPlatformBet();
	
	List<PlatformBetStats> selectBetFromBetlogByUser(Map<String, Object> params);
	
	String refreshMgToken();
	
}
