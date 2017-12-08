package com.gameportal.service;

import java.util.List;

import com.gameportal.domain.Page;
import com.gameportal.domain.ProxyTransferLog;

public interface IProxyTransferLogservice {
	/**
	 * 新增代理转账记录
	 * @param userXimaSet
	 * @return
	 */
	public int insertProxyTransferLog(ProxyTransferLog log);
	
	
	/**
	 * 代理下线出入款记录
	 * @param page
	 * @return
	 */
	public List<ProxyTransferLog> pageProxyTransferLog(Page page);
}
