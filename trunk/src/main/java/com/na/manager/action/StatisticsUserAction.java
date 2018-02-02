package com.na.manager.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.na.manager.bean.NaResponse;
import com.na.manager.common.annotation.Auth;
import com.na.manager.service.IStatisticsUserService;

/**
 * @author Andy
 * @version 创建时间：2017年12月20日 下午5:12:03
 */
@RestController
@RequestMapping("/statistics")
@Auth(isPublic = true)
public class StatisticsUserAction {
	
	@Autowired
	private IStatisticsUserService satatisticsUserService;

	@PostMapping("/queryOnlineNumberNow")
	public NaResponse<Object> queryOnlineNumberNow() {
		Long number=satatisticsUserService.selectOnlineNumberNow();
		return NaResponse.createSuccess(number);
	}
	
	@PostMapping("/queryRegisterNumberByDay")
	public NaResponse<Object> queryRegisterNumberByDay() {
		return NaResponse.createSuccess(satatisticsUserService.selectRegisterNumberByDay());
	}
	
	@PostMapping("/queryBetNumberByDay")
	public NaResponse<Object> queryBetNumberByDay() {
		return NaResponse.createSuccess(satatisticsUserService.selectBetNumberByDay());
	}
	
	@PostMapping("/queryBetTotalByDay")
	public NaResponse<Object> queryBetTotalByDay() {
		return NaResponse.createSuccess(satatisticsUserService.selectBetTotalByDay());
	}
	
	@PostMapping("/queryBetTotalRankByDay")
	public NaResponse<Object> queryBetTotalRankByDay() {
		return NaResponse.createSuccess(satatisticsUserService.selectBetTotalRankByDay());
	}
}
