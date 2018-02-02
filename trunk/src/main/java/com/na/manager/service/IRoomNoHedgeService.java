package com.na.manager.service;

import java.util.List;

import com.na.manager.bean.RoomReportRequest;
import com.na.manager.bean.vo.RoomVO;

/**
* @author Andy
* @version 创建时间：2017年9月12日 下午1:57:22
*/
public interface IRoomNoHedgeService {
	/**
	 * 包房和对下级团队报表
	 * 
	 * @param reportRequest
	 * @return
	 */
	List<RoomVO> queryRoomNoHedgeTeamReport(RoomReportRequest reportRequest);

	/**
	 * 包房和对代理报表
	 * 
	 * @param reportRequest
	 * @return
	 */
	List<RoomVO> queryRoomNoHedgeProxyReport(RoomReportRequest reportRequest);

	/**
	 * 包房和对房间报表
	 * 
	 * @param reportRequest
	 * @return
	 */
	List<RoomVO> queryRoomNoHedgeRoomReport(RoomReportRequest reportRequest);
	
	/**
	 * 包房和对单局报表
	 * 
	 * @param reportRequest
	 * @return
	 */
	List<RoomVO> queryRoomNoHedgeRoundReport(RoomReportRequest reportRequest);
}
