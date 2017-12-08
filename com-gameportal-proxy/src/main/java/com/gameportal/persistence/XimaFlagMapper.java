package com.gameportal.persistence;

import java.util.List;
import java.util.Map;

import com.gameportal.domain.XimaFlag;

public interface XimaFlagMapper {
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
	public List<XimaFlag> selectXimaFlag(Map<String, Object> param);
}
