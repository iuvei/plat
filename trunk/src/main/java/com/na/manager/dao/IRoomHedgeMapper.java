package com.na.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.na.manager.bean.RoomReportRequest;
import com.na.manager.bean.vo.RoomVO;

/**
 * 包房对冲报表
* @author Andy
* @version 创建时间：2017年9月11日 上午10:27:29
*/
@Mapper
public interface IRoomHedgeMapper {
	/**
	 * 包房对冲下级团队报表
	 * 
	 * @param reportRequest
	 * @return
	 */
	List<RoomVO> queryRoomHedgeTeamReport(RoomReportRequest reportRequest);

	/**
	 * 包房对冲代理报表
	 * 
	 * @param reportRequest
	 * @return
	 */
	List<RoomVO> queryRoomHedgeProxyReport(RoomReportRequest reportRequest);

	/**
	 * 包房对冲房间报表
	 * 
	 * @param reportRequest
	 * @return
	 */
	List<RoomVO> queryRoomHedgeRoomReport(RoomReportRequest reportRequest);
	
	/**
	 * 包房对冲单局报表
	 * 
	 * @param reportRequest
	 * @return
	 */
	List<RoomVO> queryRoomHedgeRoundReport(RoomReportRequest reportRequest);
}
