package com.na.manager.service;

import java.util.List;

import com.na.manager.bean.RoomReportRequest;
import com.na.manager.bean.vo.RoomVO;

/**
* @author Andy
* @version 创建时间：2017年9月13日 上午9:57:35
*/
public interface IRoomYLService {
	/**
	 * 包房余量下级团队报表
	 * 
	 * @param reportRequest
	 * @return
	 */
	List<RoomVO> queryRoomYLTeamReport(RoomReportRequest reportRequest);

	/**
	 * 包房余量代理报表
	 * 
	 * @param reportRequest
	 * @return
	 */
	List<RoomVO> queryRoomYLProxyReport(RoomReportRequest reportRequest);

	/**
	 * 包房余量房间报表
	 * 
	 * @param reportRequest
	 * @return
	 */
	List<RoomVO> queryRoomYLRoomReport(RoomReportRequest reportRequest);
	
	/**
	 * 包房余量单局报表
	 * 
	 * @param reportRequest
	 * @return
	 */
	List<RoomVO> queryRoomYLRoundReport(RoomReportRequest reportRequest);
}
