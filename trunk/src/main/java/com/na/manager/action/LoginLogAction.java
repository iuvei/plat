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
 * 登录日志
 * 
 * @author andy
 * @date 2017年6月23日 下午2:12:15
 * 
 */
@RestController
@RequestMapping("/loginLogReport")
@Auth("LoginReport")
public class LoginLogAction {
	@Autowired
	private ILogService logService;

	@PostMapping("/search")
	public NaResponse<LogSearchRequest> search(@RequestBody LogSearchRequest LogSearchRequest) {
		return NaResponse.createSuccess(logService.searchLoginLog(LogSearchRequest));
	}
}
