package com.na.manager.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.na.manager.bean.NaResponse;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.UrlConfig;
import com.na.manager.service.IUrlConfigService;

/**
 * @author Andy
 * @version 创建时间：2017年9月14日 上午11:57:53
 */
@RestController
@Auth("UrlConfig")
@RequestMapping("/urlConfig")
public class UrlConfigAction {
	@Autowired
	private IUrlConfigService urlConfigService;

	@PostMapping("/search")
	public NaResponse<Object> search() {
		return NaResponse.createSuccess(urlConfigService.list());
	}

	@RequestMapping("/update")
	public NaResponse<Object> updateGameConfig(@RequestBody UrlConfig urlConfig) {
		urlConfigService.update(urlConfig);
		return NaResponse.createSuccess();
	}
}
