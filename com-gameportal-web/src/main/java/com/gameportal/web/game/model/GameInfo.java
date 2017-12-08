package com.gameportal.web.game.model;

import java.io.Serializable;

import com.gameportal.web.user.model.BaseEntity;

/**
 * 游戏信息。
 * @author sum
 *
 */
public class GameInfo extends BaseEntity{
	private static final long serialVersionUID = 1L;
	public static final String TABLE_ALIAS = "GameInfo";
	public static final String ALIAS_GID = "游戏ID";
	public static final String ALIAS_PTCODE = "游戏平台类型";
	public static final String ALIAS_GKIND = "游戏种类";
	public static final String ALIAS_GCODE = "游戏编号";
	public static final String ALIAS_GNAME = "游戏名称";

	private  Long gid;
	private String platFormCode;
	private String gameKind;
	private String gamesCode;
	private String gamesName;
	
	
	public Long getGid() {
		return gid;
	}


	public void setGid(Long gid) {
		this.gid = gid;
	}


	public String getPlatFormCode() {
		return platFormCode;
	}


	public void setPlatFormCode(String platFormCode) {
		this.platFormCode = platFormCode;
	}


	public String getGameKind() {
		return gameKind;
	}


	public void setGameKind(String gameKind) {
		this.gameKind = gameKind;
	}


	public String getGamesCode() {
		return gamesCode;
	}


	public void setGamesCode(String gamesCode) {
		this.gamesCode = gamesCode;
	}


	public String getGamesName() {
		return gamesName;
	}


	public void setGamesName(String gamesName) {
		this.gamesName = gamesName;
	}

	@Override
	public Serializable getID() {
		return this.gid;
	}
}
