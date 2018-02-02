package com.na.manager.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.na.manager.bean.Page;
import com.na.manager.bean.RoomMemberWinLostReportRequest;
import com.na.manager.bean.RoomReportRequest;
import com.na.manager.bean.vo.RoomVO;
import com.na.manager.bean.vo.RoomWinLostVO;
import com.na.manager.cache.AppCache;
import com.na.manager.dao.ILiveUserMapper;
import com.na.manager.dao.IRoomWinLostMapper;
import com.na.manager.entity.ChildAccountUser;
import com.na.manager.entity.LiveUser;
import com.na.manager.entity.User;
import com.na.manager.enums.LiveUserType;
import com.na.manager.enums.RoomStaEnum;
import com.na.manager.enums.UserType;
import com.na.manager.service.IRoomWinLostService;

/**
 * @author Andy
 * @version 创建时间：2017年8月19日 下午12:31:40
 */
@Service
@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
public class RoomWinLostServiceImpl implements IRoomWinLostService {

	@Autowired
	private IRoomWinLostMapper roomWinLostMapper;

	@Autowired
	private ILiveUserMapper liveUserMapper;
	
	@Override
	@Transactional(readOnly = true)
	public List<RoomVO> queryRoomHedgeCompanyWinLostReport(RoomReportRequest reportRequest) {
		LiveUser liveUser = null;
		if (StringUtils.isNoneBlank(reportRequest.getAgentName())) {
			liveUser = liveUserMapper.findLiveUserByUserName(reportRequest.getAgentName());
			if (liveUser == null || !liveUser.getParentPath().contains("/" + AppCache.getCurrentUser().getId() + "/")) {
				return new ArrayList<>();
			}else{
				reportRequest.setUserId(liveUser.getUserId());
			}
		}
		List<RoomVO> dataList = roomWinLostMapper.queryRoomHedgeCompanyWinLostReport(reportRequest);
		if(CollectionUtils.isEmpty(dataList) || dataList.get(0)==null || dataList.get(0).getLoginName()==null){
			return new ArrayList<>();
		}
		dataList.forEach(item->{
			item.setStatisType(RoomStaEnum.PROXYSTA.get());
			item.setStatisTypeDesc(RoomStaEnum.TEAMSTA.getText());
			item.setCompanySettle(item.getWaterSettle().add(item.getNoHedgeSettle()).add(item.getYlSettle()));
		});
		return dataList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RoomVO> queryRoomHedgeProxyWinLostReport(RoomReportRequest reportRequest) {
		LiveUser liveUser = null;
		if (StringUtils.isNoneBlank(reportRequest.getAgentName())) {
			liveUser = liveUserMapper.findLiveUserByUserName(reportRequest.getAgentName());
			if (liveUser == null || !liveUser.getParentPath().contains("/" + AppCache.getCurrentUser().getId() + "/")) {
				return new ArrayList<>();
			}else{
				reportRequest.setUserId(liveUser.getUserId());
			}
		}
		List<RoomVO> dataList = roomWinLostMapper.queryRoomHedgeProxyWinLostReport(reportRequest);
		dataList.forEach(item->{
			item.setStatisType(RoomStaEnum.ROOMSTA.get());
			item.setStatisTypeDesc(RoomStaEnum.PROXYSTA.getText());
			item.setCompanySettle(item.getWaterSettle().add(item.getNoHedgeSettle()).add(item.getYlSettle()));
		});
		return dataList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RoomVO> queryRoomHedgeRoomWinLostReport(RoomReportRequest reportRequest) {
		LiveUser liveUser = null;
		if (StringUtils.isNoneBlank(reportRequest.getAgentName())) {
			liveUser = liveUserMapper.findLiveUserByUserName(reportRequest.getAgentName());
			if (liveUser == null || !liveUser.getParentPath().contains("/" + AppCache.getCurrentUser().getId() + "/")) {
				return new ArrayList<>();
			}else{
				reportRequest.setUserId(liveUser.getUserId());
			}
		}
		List<RoomVO> dataList = roomWinLostMapper.queryRoomHedgeRoomWinLostReport(reportRequest);
		dataList.forEach(item->{
			item.setStatisType(RoomStaEnum.ROUNDSTA.get());
			item.setStatisTypeDesc(RoomStaEnum.ROOMSTA.getText());
			item.setCompanySettle(item.getWaterSettle().add(item.getNoHedgeSettle()).add(item.getYlSettle()));
		});
		return dataList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RoomVO> queryRoomHedgeRoundWinLostReport(
			RoomReportRequest reportRequest) {
		LiveUser liveUser = null;
		if (StringUtils.isNoneBlank(reportRequest.getAgentName())) {
			liveUser = liveUserMapper.findLiveUserByUserName(reportRequest.getAgentName());
			if (liveUser == null || !liveUser.getParentPath().contains("/" + AppCache.getCurrentUser().getId() + "/")) {
				return new ArrayList<>();
			}else{
				reportRequest.setUserId(liveUser.getUserId());
			}
		}

		List<RoomVO> dataList = roomWinLostMapper.queryRoomHedgeRoundWinLostReport(reportRequest);
		dataList=dataList.stream().filter(item->(item.getWaterSettle().add(item.getNoHedgeSettle()).add(item.getYlSettle())).doubleValue()!=0).collect(Collectors.toList());
		dataList.forEach(item->{
			item.setStatisType(RoomStaEnum.BETVIEW.get());
			item.setStatisTypeDesc(RoomStaEnum.ROUNDSTA.getText());
			item.setCompanySettle(item.getWaterSettle().add(item.getNoHedgeSettle()).add(item.getYlSettle()));
		});
		return dataList;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<RoomWinLostVO> queryRoomWinLostReport(RoomMemberWinLostReportRequest roomReportRequest) {
		Page<RoomWinLostVO> page = new Page<>(roomReportRequest);
		User currentUser = AppCache.getCurrentUser();

		// 子账号归属父级节点
		if (currentUser.getUserTypeEnum() == UserType.SUB_ACCOUNT) {
			ChildAccountUser childAccountUser = (ChildAccountUser) currentUser;
			currentUser = childAccountUser.getParentUser();
		}
		roomReportRequest.setUserId(currentUser.getId());
		if (StringUtils.isNotEmpty(roomReportRequest.getLoginName())) {
			LiveUser liveUser = liveUserMapper.findLiveUserByUserName(roomReportRequest.getLoginName());
			// 用户不存在或者不是其下级，返回空
			if (liveUser == null || liveUser.getParentPath().indexOf(String.valueOf(currentUser.getId())) == -1) {
				return page;
			} else if (liveUser.getTypeEnum() != LiveUserType.MEMBER) {
				// 查询代理所有下线
				roomReportRequest.setUserId(liveUser.getId());
				roomReportRequest.setLoginName(null);
			}
		}

		page.setData(roomWinLostMapper.queryRoomWinLostReport(roomReportRequest));
		page.setTotal(roomWinLostMapper.getRoomWinLostReportCount(roomReportRequest));
		return page;
	}

}
