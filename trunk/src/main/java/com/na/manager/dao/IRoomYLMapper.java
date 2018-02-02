package com.na.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.na.manager.bean.RoomReportRequest;
import com.na.manager.bean.vo.RoomVO;

/**
 * 包房余量报表
* @author Andy
* @version 创建时间：2017年9月11日 上午10:28:14
*/
@Mapper
public interface IRoomYLMapper {
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
