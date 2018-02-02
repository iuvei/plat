package com.na.manager.service;

import java.util.List;

import com.na.manager.bean.Page;
import com.na.manager.bean.RoomReportRequest;
import com.na.manager.bean.RoomMemberWinLostReportRequest;
import com.na.manager.bean.vo.RoomVO;
import com.na.manager.bean.vo.RoomWinLostVO;

/**
* @author Andy
* @version 创建时间：2017年8月19日 下午12:30:55
*/
public interface IRoomWinLostService {

	Page<RoomWinLostVO> queryRoomWinLostReport(RoomMemberWinLostReportRequest roomReportRequest);
	
	List<RoomVO> queryRoomHedgeCompanyWinLostReport(RoomReportRequest reportRequest);
	
	List<RoomVO> queryRoomHedgeProxyWinLostReport(RoomReportRequest reportRequest);
	
	List<RoomVO> queryRoomHedgeRoomWinLostReport(RoomReportRequest reportRequest);
	
	List<RoomVO> queryRoomHedgeRoundWinLostReport(RoomReportRequest reportRequest);
	
}
