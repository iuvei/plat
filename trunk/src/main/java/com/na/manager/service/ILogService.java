package com.na.manager.service;

import com.na.manager.bean.LogSearchRequest;
import com.na.manager.bean.Page;
import com.na.manager.bean.vo.ApiLogVO;
import com.na.manager.bean.vo.LoginLogVO;

/**
 * @author andy
 * @date 2017年6月23日 下午3:21:36
 * 
 */
public interface ILogService {
	
	Page<LoginLogVO> searchLoginLog(LogSearchRequest searchRequest);
	
	Page<ApiLogVO> searchApiLog(LogSearchRequest searchRequest);
}
