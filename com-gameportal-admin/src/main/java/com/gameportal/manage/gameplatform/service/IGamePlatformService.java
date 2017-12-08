package com.gameportal.manage.gameplatform.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.gameplatform.model.GamePlatform;

/**
 * @ClassName: IGamePlatformService
 * @Description: TODO(游戏平台接口)
 * @author shejia@gz-mstc.com
 * @date 2014-4-10 下午4:39:05
 */
public abstract interface IGamePlatformService {
	public abstract GamePlatform queryGamePlatformById(String gpid);

	public abstract List<GamePlatform> queryGamePlatform(Long gpid,
			String gpname, Integer startNo, Integer pageSize);

	public abstract List<GamePlatform> queryGamePlatform(Long gpid,
			String gpname, Integer status, Integer startNo, Integer pageSize);

	public abstract List<GamePlatform> queryGamePlatform(Long gpid,
			String gpname, String domainname, String domainip, String deskey,
			String ciphercode, Integer status, Integer startNo, Integer pageSize);

	public abstract Long queryGamePlatformCount(Long gpid, String gpname);

	public abstract Long queryGamePlatformCount(Long gpid, String gpname,
			Integer status);

	public abstract Long queryGamePlatformCount(Long gpid, String gpname,
			String domainname, String domainip, String deskey,
			String ciphercode, Integer status);

	public abstract GamePlatform saveGamePlatform(GamePlatform gamePlatform)
			throws Exception;

	public abstract boolean saveOrUpdateGamePlatform(GamePlatform gamePlatform)
			throws Exception;

	public abstract boolean deleteGamePlatform(Long gpid) throws Exception;
	
	/**
	 * 
	 * @param params
	 * @return
	 */
	public List<GamePlatform> getList(Map<String, Object> params);
	
	public GamePlatform getObject(Map<String, Object> params);
	

}
