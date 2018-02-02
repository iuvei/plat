package com.na.baccarat.socketserver.entity;

import java.io.Serializable;

import com.na.user.socketserver.entity.UserChipsPO;

/**
 * @author
 * @time 2015-07-21 18:13:40
 */
public class UserChips implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private UserChipsPO userChipsPO;
	
	
	public UserChips() {
	}

	public UserChips(UserChipsPO userChipsPO) {
		super();
		this.userChipsPO = userChipsPO;
	}

	public UserChipsPO getUserChipsPO() {
		return userChipsPO;
	}

	public void setUserChipsPO(UserChipsPO userChipsPO) {
		this.userChipsPO = userChipsPO;
	}
	
	
	
}