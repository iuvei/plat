package com.na.baccarat.socketserver.command.sendpara;


import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 公告响应。
 * Created by sunny on 2017/5/3 0003.
 */
public class UserAnnounceResponse {

	public class UserAnnounce{
		@JSONField(serialize = false)
		public List<Long> uidList;
		public String title;
		public String content;
	}

	List<UserAnnounce> userAnnounceList;

	public List<UserAnnounce> getUserAnnounceList() {
		return userAnnounceList;
	}

	public void setUserAnnounceList(List<UserAnnounce> userAnnounceList) {
		this.userAnnounceList = userAnnounceList;
	}
}
