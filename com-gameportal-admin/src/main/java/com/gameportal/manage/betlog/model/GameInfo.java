package com.gameportal.manage.betlog.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gameportal.manage.system.model.BaseEntity;


public class GameInfo extends BaseEntity {
	
	//alias
	public static final String TABLE_ALIAS = "GameInfo";
	public static final String ALIAS_GID = "gid";
	public static final String ALIAS_PLATFORMCODE = "platformcode";
	public static final String ALIAS_GAMKIND = "gamkind";
	public static final String ALIAS_GAMESCODE = "gamescode";
	public static final String ALIAS_GAMES_NAME = "gamesName";
	
	
	//columns START
	private java.lang.Long gid;
	private java.lang.String platformcode;
	private java.lang.String gamkind;
	private java.lang.String gamescode;
	private java.lang.String gamesName;
	//columns END

	public GameInfo(){
	}

	public GameInfo(
		java.lang.Long gid
	){
		this.gid = gid;
	}

	
	public void setGid(java.lang.Long value) {
		this.gid = value;
	}
	
	public java.lang.Long getGid() {
		return this.gid;
	}
	
	public void setPlatformcode(java.lang.String value) {
		this.platformcode = value;
	}
	
	public java.lang.String getPlatformcode() {
		return this.platformcode;
	}
	
	public void setGamkind(java.lang.String value) {
		this.gamkind = value;
	}
	
	public java.lang.String getGamkind() {
		return this.gamkind;
	}
	
	public void setGamescode(java.lang.String value) {
		this.gamescode = value;
	}
	
	public java.lang.String getGamescode() {
		return this.gamescode;
	}
	
	public void setGamesName(java.lang.String value) {
		this.gamesName = value;
	}
	
	public java.lang.String getGamesName() {
		return this.gamesName;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("Gid",getGid())
			.append("Platformcode",getPlatformcode())
			.append("Gamkind",getGamkind())
			.append("Gamescode",getGamescode())
			.append("GamesName",getGamesName())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getGid())
			.append(getPlatformcode())
			.append(getGamkind())
			.append(getGamescode())
			.append(getGamesName())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof GameInfo == false) return false;
		if(this == obj) return true;
		GameInfo other = (GameInfo)obj;
		return new EqualsBuilder()
			.append(getGid(),other.getGid())
			.append(getPlatformcode(),other.getPlatformcode())
			.append(getGamkind(),other.getGamkind())
			.append(getGamescode(),other.getGamescode())
			.append(getGamesName(),other.getGamesName())
			.isEquals();
	}
	



	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return  this.gid;
	}
}

