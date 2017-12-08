package com.gameportal.service;

import java.util.Map;

import com.gameportal.domain.XimaFlag;

public interface IXimaFlagService {
	/**
	 * 新增会员洗码标记
	 * @param flag
	 * @return
	 */
	public int insert(XimaFlag flag);
	
	/**
	 * 更新会员洗码标记
	 * @param flag
	 * @return
	 */
	public int update(XimaFlag flag);
	
	/**
	 * 查询会员洗码标记
	 * @param param
	 * @return
	 */
	public XimaFlag getNewestXimaFlag(Map<String, Object> param);
}
