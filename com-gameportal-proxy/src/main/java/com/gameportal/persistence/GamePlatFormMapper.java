package com.gameportal.persistence;

import java.util.List;
import java.util.Map;

import com.gameportal.domain.GamePlatform;

/**
 * 游戏平台
 * @author leron
 *
 */
public interface GamePlatFormMapper {

	/**
	 * 查询游戏平台列表
	 * @param params
	 * @return
	 */
	public List<GamePlatform> findGamePlatform(Map<String, Object> params);
}
