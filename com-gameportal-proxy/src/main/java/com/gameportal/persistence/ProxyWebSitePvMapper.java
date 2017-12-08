package com.gameportal.persistence;

import java.util.Map;

/**
 * 域名访问量统计。
 * @author sum
 *
 */
public interface ProxyWebSitePvMapper {

	/**
	 * 
	 * @param map
	 * @return
	 */
	Long queryCount(Map<String, Object> map);
}
