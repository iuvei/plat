package com.na.manager.action;

import com.na.manager.bean.vo.BetOrderVO;
import com.na.manager.service.IBetOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.na.manager.bean.AccountRecordReportRequest;
import com.na.manager.bean.NaResponse;
import com.na.manager.common.annotation.Auth;
import com.na.manager.service.IAccountRecordService;

/**
 * 转账记录
 * @author andy
 * @date 2017年6月23日 上午10:13:11
 * 
 */
@RestController
@RequestMapping("/accountRecordReport")
@Auth("PointInOutReport")
public class AccountRecordAction {
	@Autowired
	private IAccountRecordService accountRecordService;
	@Autowired
	private IBetOrderService betOrderService;
	
	@PostMapping("/search")
	public NaResponse<AccountRecordReportRequest> search(@RequestBody AccountRecordReportRequest reportRequest){
		reportRequest.setType(1); //查询存提点数据
		return NaResponse.createSuccess(accountRecordService.queryAccountRecordByPage(reportRequest));
	}

	@Auth("AccountReport")
	@PostMapping("/searchAccountRecord")
	public NaResponse<AccountRecordReportRequest> searchAccountRecord(@RequestBody AccountRecordReportRequest reportRequest){
		return NaResponse.createSuccess(accountRecordService.queryAccountRecordByPage(reportRequest));
	}

	@Auth("AccountReport")
	@GetMapping("/betorder/{betId}")
	public NaResponse<BetOrderVO> findBetOrderDetail(@PathVariable("betId") Long betId){
		return NaResponse.createSuccess(betOrderService.findBetOrderDetail(betId));
	}
}
