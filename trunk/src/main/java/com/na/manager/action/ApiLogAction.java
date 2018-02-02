package com.na.manager.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.na.manager.bean.LogSearchRequest;
import com.na.manager.bean.NaResponse;
import com.na.manager.common.annotation.Auth;
import com.na.manager.service.ILogService;

/**
 * API操作日志
 * @author andy
 * @date 2017年6月26日 上午10:12:12
 * 
 */
@RestController
@RequestMapping("/apiLog")
@Auth("ApiLog")
public class ApiLogAction {
	
	@Autowired
	private ILogService logService;

	@PostMapping("/search")
	public NaResponse<LogSearchRequest> search(@RequestBody LogSearchRequest searchRequest) {
		return NaResponse.createSuccess(logService.searchLoginLog(searchRequest));
	}

}
