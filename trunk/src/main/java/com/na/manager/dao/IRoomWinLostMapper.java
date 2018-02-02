package com.na.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.na.manager.bean.RoomReportRequest;
import com.na.manager.bean.RoomMemberWinLostReportRequest;
import com.na.manager.bean.vo.RoomVO;
import com.na.manager.bean.vo.RoomWinLostVO;

/**
 * 包房输赢报表
 * @author Andy
 * @version 创建时间：2017年8月19日 下午12:02:14
 */
@Mapper
public interface IRoomWinLostMapper {
	/**
	 * 包房会员输赢报表
	 * 
	 * @param roomReportRequest
	 * @return
	 */
	List<RoomWinLostVO> queryRoomWinLostReport(RoomMemberWinLostReportRequest roomReportRequest);

	long getRoomWinLostReportCount(RoomMemberWinLostReportRequest roomReportRequest);

	/**
	 * 包房对冲公司输赢报表
	 * 
	 * @param reportRequest
	 * @return
	 */
	List<RoomVO> queryRoomHedgeCompanyWinLostReport(RoomReportRequest reportRequest);

	/**
	 * 包房对冲代理输赢报表
	 * 
	 * @param reportRequest
	 * @return
	 */
	List<RoomVO> queryRoomHedgeProxyWinLostReport(RoomReportRequest reportRequest);

	/**
	 * 包房对冲房间输赢报表
	 * 
	 * @param reportRequest
	 * @return
	 */
	List<RoomVO> queryRoomHedgeRoomWinLostReport(RoomReportRequest reportRequest);

	/**
	 * 包房对冲局输赢报表
	 * 
	 * @param reportRequest
	 * @return
	 */
	List<RoomVO> queryRoomHedgeRoundWinLostReport(RoomReportRequest reportRequest);
}
