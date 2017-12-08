package com.gameportal.manage.betlog.model;

import java.util.Date;
import java.util.List;

import com.gameportal.manage.gameplatform.model.GamePlatform;
import com.gameportal.manage.user.model.UserInfo;

public class CollectionAttr {
	/**
	 * 每个线程会员人数
	 */
  private int pageSize;
  
  private  List<UserInfo> userInfoList;
 
  private Date startDate;
  
  private Date endDate;
  
  private List<GameInfo> gameInfoList;

  private GamePlatform gamePlatformBBIN;

public int getPageSize() {
	return pageSize;
}

public void setPageSize(int pageSize) {
	this.pageSize = pageSize;
}

public List<UserInfo> getUserInfoList() {
	return userInfoList;
}

public void setUserInfoList(List<UserInfo> userInfoList) {
	this.userInfoList = userInfoList;
}

public Date getStartDate() {
	return startDate;
}

public void setStartDate(Date startDate) {
	this.startDate = startDate;
}

public Date getEndDate() {
	return endDate;
}

public void setEndDate(Date endDate) {
	this.endDate = endDate;
}

public List<GameInfo> getGameInfoList() {
	return gameInfoList;
}

public void setGameInfoList(List<GameInfo> gameInfoList) {
	this.gameInfoList = gameInfoList;
}

public GamePlatform getGamePlatformBBIN() {
	return gamePlatformBBIN;
}

public void setGamePlatformBBIN(GamePlatform gamePlatformBBIN) {
	this.gamePlatformBBIN = gamePlatformBBIN;
}
  
  
}
