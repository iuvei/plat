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

import com.na.manager.bean.RoomReportRequest;
import com.na.manager.bean.vo.RoomVO;
import com.na.manager.cache.AppCache;
import com.na.manager.dao.ILiveUserMapper;
import com.na.manager.dao.IRoomYLMapper;
import com.na.manager.entity.LiveUser;
import com.na.manager.enums.RoomStaEnum;
import com.na.manager.service.IRoomYLService;

/**
 * 包房余量
* @author Andy
* @version 创建时间：2017年9月13日 上午9:58:19
*/
@Service
@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
public class RoomYLServiceImpl implements IRoomYLService {
	@Autowired
	private IRoomYLMapper roomYLMapper;
	
	@Autowired
	private ILiveUserMapper liveUserMapper;
	@Override
	public List<RoomVO> queryRoomYLTeamReport(RoomReportRequest reportRequest) {
		LiveUser liveUser = null;
		if (StringUtils.isNoneBlank(reportRequest.getAgentName())) {
			liveUser = liveUserMapper.findLiveUserByUserName(reportRequest.getAgentName());
			if (liveUser == null || !liveUser.getParentPath().contains("/" + AppCache.getCurrentUser().getId() + "/")) {
				return new ArrayList<>();
			}else{
				reportRequest.setUserId(liveUser.getUserId());
			}
		}
		List<RoomVO> dataList = roomYLMapper.queryRoomYLTeamReport(reportRequest);
		if(CollectionUtils.isEmpty(dataList) || dataList.get(0)==null || dataList.get(0).getLoginName()==null){
			return new ArrayList<>();
		}
		dataList.forEach(item->{
			item.setStatisType(RoomStaEnum.PROXYSTA.get());
			item.setStatisTypeDesc(RoomStaEnum.TEAMSTA.getText());
		});
		return dataList;
	}

	@Override
	public List<RoomVO> queryRoomYLProxyReport(RoomReportRequest reportRequest) {
		LiveUser liveUser = null;
		if (StringUtils.isNoneBlank(reportRequest.getAgentName())) {
			liveUser = liveUserMapper.findLiveUserByUserName(reportRequest.getAgentName());
			if (liveUser == null || !liveUser.getParentPath().contains("/" + AppCache.getCurrentUser().getId() + "/")) {
				return new ArrayList<>();
			}else{
				reportRequest.setUserId(liveUser.getUserId());
			}
		}
		List<RoomVO> dataList = roomYLMapper.queryRoomYLProxyReport(reportRequest);
		dataList.forEach(item->{
			item.setStatisType(RoomStaEnum.ROOMSTA.get());
			item.setStatisTypeDesc(RoomStaEnum.PROXYSTA.getText());
		});
		return dataList;
	}

	@Override
	public List<RoomVO> queryRoomYLRoomReport(RoomReportRequest reportRequest) {
		LiveUser liveUser = null;
		if (StringUtils.isNoneBlank(reportRequest.getAgentName())) {
			liveUser = liveUserMapper.findLiveUserByUserName(reportRequest.getAgentName());
			if (liveUser == null || !liveUser.getParentPath().contains("/" + AppCache.getCurrentUser().getId() + "/")) {
				return new ArrayList<>();
			}else{
				reportRequest.setUserId(liveUser.getUserId());
			}
		}
		List<RoomVO> dataList = roomYLMapper.queryRoomYLRoomReport(reportRequest);
		dataList.forEach(item->{
			item.setStatisType(RoomStaEnum.ROUNDSTA.get());
			item.setStatisTypeDesc(RoomStaEnum.ROOMSTA.getText());
		});
		return dataList;
	}

	@Override
	public List<RoomVO> queryRoomYLRoundReport(RoomReportRequest reportRequest) {
		LiveUser liveUser = null;
		if (StringUtils.isNoneBlank(reportRequest.getAgentName())) {
			liveUser = liveUserMapper.findLiveUserByUserName(reportRequest.getAgentName());
			if (liveUser == null || !liveUser.getParentPath().contains("/" + AppCache.getCurrentUser().getId() + "/")) {
				return new ArrayList<>();
			}else{
				reportRequest.setUserId(liveUser.getUserId());
			}
		}
		List<RoomVO> dataList = roomYLMapper.queryRoomYLRoundReport(reportRequest);
		dataList=dataList.stream().filter(item->item.getYlSettle().doubleValue()!=0).collect(Collectors.toList());
		dataList.forEach(item->{
			item.setStatisType(RoomStaEnum.BETVIEW.get());
			item.setStatisTypeDesc(RoomStaEnum.ROUNDSTA.getText());
		});
		return dataList;
	}

}
