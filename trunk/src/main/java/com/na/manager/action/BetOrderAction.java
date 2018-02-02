package com.na.manager.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.na.manager.bean.BetOrderReportRequest;
import com.na.manager.bean.NaResponse;
import com.na.manager.common.annotation.Auth;
import com.na.manager.service.IBetOrderService;

/**
 * 投注记录
 * @author andy
 * @date 2017年6月23日 下午2:56:03
 * 
 */
@RestController
@RequestMapping("/tranRecordReport")
@Auth("TranRecordReport")
public class BetOrderAction {
	@Autowired
	private IBetOrderService betOrderService;
	
	@PostMapping("/search")
	public NaResponse<BetOrderReportRequest> search(@RequestBody BetOrderReportRequest reportRequest){
		return NaResponse.createSuccess(betOrderService.queryBetOrderByPage(reportRequest));
	}
	
	@PostMapping("/searchReal")
	public NaResponse<BetOrderReportRequest> searchReal(@RequestBody BetOrderReportRequest reportRequest){
		return NaResponse.createSuccess(betOrderService.queryBetOrderByPage(reportRequest));
	}
	
}
