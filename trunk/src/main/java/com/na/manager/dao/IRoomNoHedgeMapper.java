package com.na.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.na.manager.bean.RoomReportRequest;
import com.na.manager.bean.vo.RoomVO;

/**
 * 包房和对报表
* @author Andy
* @version 创建时间：2017年9月11日 上午10:27:41
*/
@Mapper
public interface IRoomNoHedgeMapper {
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
