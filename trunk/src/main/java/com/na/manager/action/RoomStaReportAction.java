package com.na.manager.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.na.manager.bean.NaResponse;
import com.na.manager.bean.RoomReportRequest;
import com.na.manager.cache.AppCache;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.ChildAccountUser;
import com.na.manager.entity.User;
import com.na.manager.enums.RoomStaEnum;
import com.na.manager.enums.UserType;
import com.na.manager.service.IRoomWinLostService;

/**
 * 包房交收报表
 *  v
 * @create 2017-07
 */
@RestController
@RequestMapping("/roomStaReport")
@Auth("RoomStaReport")
public class RoomStaReportAction {

    @Autowired
    private IRoomWinLostService roomWinLostService;

    @PostMapping("/search")
    public NaResponse<RoomReportRequest> search(@RequestBody RoomReportRequest reportRequest){
    	User currentUser = AppCache.getCurrentUser();
		if (currentUser.getUserTypeEnum() == UserType.UNKNOWN || currentUser.getUserTypeEnum() == UserType.DEALER) {
			return NaResponse.createSuccess();
		}
		// 子账号归属父级节点
		if (currentUser.getUserTypeEnum() == UserType.SUB_ACCOUNT) {
			ChildAccountUser childAccountUser = (ChildAccountUser) currentUser;
			currentUser = childAccountUser.getParentUser();
		}
		reportRequest.setUserId(currentUser.getId());
		if(reportRequest.getStatisType()==RoomStaEnum.TEAMSTA.get()){ //下级团队维度
			return NaResponse.createSuccess(roomWinLostService.queryRoomHedgeCompanyWinLostReport(reportRequest));
		}else if(reportRequest.getStatisType()==RoomStaEnum.PROXYSTA.get()){ //代理汇总维度
			return NaResponse.createSuccess(roomWinLostService.queryRoomHedgeProxyWinLostReport(reportRequest));
		}else if(reportRequest.getStatisType()==RoomStaEnum.ROOMSTA.get()){ //房间汇总维度
			return NaResponse.createSuccess(roomWinLostService.queryRoomHedgeRoomWinLostReport(reportRequest));
		}else{ //单局汇总维度
			return NaResponse.createSuccess(roomWinLostService.queryRoomHedgeRoundWinLostReport(reportRequest));
		}
    }
}
