package com.na.manager.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.na.manager.bean.BetOrderReportRequest;
import com.na.manager.bean.NaResponse;
import com.na.manager.bean.RoomMemberWinLostReportRequest;
import com.na.manager.common.annotation.Auth;
import com.na.manager.service.IRoomWinLostService;

/**
 * 包房会员输赢报表 
 * 
 * @create 2017-07
 */
@RestController
@RequestMapping("/roomMemberReport")
@Auth("RoomMemberReport")
public class RoomMemberReportAction {

	@Autowired
	private IRoomWinLostService roomWinLostService;

	@PostMapping("/search")
	public NaResponse<BetOrderReportRequest> search(@RequestBody RoomMemberWinLostReportRequest roomRequest) {
		return NaResponse.createSuccess(roomWinLostService.queryRoomWinLostReport(roomRequest));
	}
}
