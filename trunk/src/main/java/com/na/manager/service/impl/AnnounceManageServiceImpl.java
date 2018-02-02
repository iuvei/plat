package com.na.manager.service.impl;

import com.google.common.base.Preconditions;
import com.na.manager.bean.AnnounceSearchRequest;
import com.na.manager.bean.Page;
import com.na.manager.bean.vo.AnnounceListVO;
import com.na.manager.cache.AppCache;
import com.na.manager.dao.IAnnounceManageMapper;
import com.na.manager.dao.ILiveUserMapper;
import com.na.manager.dao.IUserMapper;
import com.na.manager.entity.AnnounceContent;
import com.na.manager.entity.LiveUser;
import com.na.manager.entity.User;
import com.na.manager.entity.UserAnnounce;
import com.na.manager.enums.LiveUserType;
import com.na.manager.service.IAnnounceManageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author andy
 * @date 2017年6月26日 下午6:37:47
 * 
 */
@Service
@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
public class AnnounceManageServiceImpl implements IAnnounceManageService {

	@Autowired
	private IAnnounceManageMapper announceManageMapper;
	@Autowired
	private IUserMapper userMapper;
	@Autowired
	private ILiveUserMapper liveUserMapper;

	@Override
	@Transactional(readOnly = true)
	public Page<AnnounceListVO> queryUserAnnouneByPage(AnnounceSearchRequest searchRequest) {
		if (StringUtils.isNotEmpty(searchRequest.getUserName())) {
			User user = userMapper.getUser(searchRequest.getUserName());
			if (user == null) {
				return new Page<>(searchRequest);
			}
			searchRequest.setUserName(user.getLoginName());
		} else {
			searchRequest.setUserName(AppCache.getCurrentUser().getLoginName());
		}
		
		Page<AnnounceListVO> page = new Page<>(searchRequest);
		page.setTotal(announceManageMapper.count(searchRequest));
		page.setData(announceManageMapper.queryUserAnnouneByPage(searchRequest));
		return page;
	}

	@Override
	public UserAnnounce insertAnnounceConent(AnnounceContent announceContent) {
		Preconditions.checkNotNull(announceContent.getLoginName(), "user.loginname.not.null");
		announceManageMapper.insertAnnounceConent(announceContent);
		UserAnnounce ua = new UserAnnounce();
		ua.setContentId(announceContent.getId());
		ua.setType(announceContent.getType());
		ua.setUpdateBy(AppCache.getCurrentUser().getLoginName());
		ua.setCreateBy(ua.getUpdateBy());
		ua.setCreateDateTime(new Date());
		ua.setUpdateDateTime(new Date());
		Map<String, String> mapList = dealUserNameList(announceContent.getLoginName());
		ua.setUserName(mapList.get("namePath"));
		ua.setUserId(mapList.get("idPath"));
		List<UserAnnounce> userAnnounces = new ArrayList<>();
		userAnnounces.add(ua);
		announceManageMapper.insertUserAnnounce(userAnnounces);
		return ua;

	}

	@Override
	public void delete(Long id) {
		Preconditions.checkNotNull(id, "userannounce.id.not.null");
		UserAnnounce userAnnounce = announceManageMapper.findUserAnnouneById(id);
		announceManageMapper.deleteUserAnnounce(userAnnounce.getId());
		announceManageMapper.deleteAnnounceContent(userAnnounce.getContentId());
	}

	@Override
	@Transactional(readOnly = true)
	public void checkSysUserExist(User user) {
		String userList = user.getLoginName().replaceAll("，", ",");
		LiveUser liveUser = null;
		String[] names = null;
		if (userList.contains(",")) {
			names = userList.split(",");
			for (String name : names) {
				if (StringUtils.isEmpty(name)) {
					continue;
				}
				liveUser = liveUserMapper.findLiveUserByUserName(name);
				Preconditions.checkNotNull(liveUser, "user.loginname.not.exist");
				if (liveUser.getType() ==LiveUserType.MEMBER.get()) {
					Preconditions.checkNotNull(liveUser, "user.not.proxy");
					break;
				}
			}
		} else {
			liveUser = liveUserMapper.findLiveUserByUserName(user.getLoginName());
			Preconditions.checkNotNull(liveUser, "user.loginname.not.exist");
			if (liveUser.getType() ==LiveUserType.MEMBER.get()) {
				Preconditions.checkNotNull(liveUser, "user.not.proxy");
			}
		}
	}

	// 处理userlist
	@Override
	public Map<String, String> dealUserNameList(String userList) {
		List<User> users = new ArrayList<>();
		User user = null;
		userList = userList.replaceAll("，", ",");
		if (userList.contains(",")) {
			for (String name : userList.split(",")) {
				user = userMapper.getUser(name);
				if (user == null) {
					continue;
				}
				users.add(user);
			}
		} else {
			user = userMapper.getUser(userList);
			if (user == null) {
				return null;
			}
			users.add(user);
		}
		if (CollectionUtils.isEmpty(users)) {
			return null;
		}

		String namePath = "";
		String idPath = "";
		for (User u : users) {
			if (namePath.length() > 0) {
				namePath += ",";
			}
			if (idPath.length() > 0) {
				idPath += ",";
			}
			namePath += u.getLoginName();
			idPath += u.getId();
		}
		Map<String, String> map = new HashMap<>();
		map.put("namePath", namePath);
		map.put("idPath", idPath);
		return map;
	}

	@Override
	public void updateAnnounceContent(AnnounceContent announceContent) {
		announceManageMapper.updateAnnounceContent(announceContent);
	}

	@Override
	public void updateUserAnnounce(UserAnnounce userAnnounce) {
		AnnounceContent announceContent = announceManageMapper.findAnnounceContent(userAnnounce.getContentId());
		userAnnounce.setUpdateBy(AppCache.getCurrentUser().getLoginName());
		userAnnounce.setUpdateDateTime(new Date());
		Map<String, String> mapList = dealUserNameList(userAnnounce.getUserName());
		userAnnounce.setUserName(mapList.get("namePath"));
		userAnnounce.setUserId(mapList.get("idPath"));
		announceManageMapper.updateUserAnnounce(userAnnounce);
		if (announceContent != null) {
			announceContent.setContentDesc(userAnnounce.getAnnounceDesc());
			announceContent.setContentTitle(userAnnounce.getAnnounceTitle());
			announceManageMapper.updateAnnounceContent(announceContent);
		}
	}

}
