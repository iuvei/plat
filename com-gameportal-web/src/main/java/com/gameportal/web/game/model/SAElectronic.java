package com.gameportal.web.game.model;

import java.io.Serializable;

import com.gameportal.web.user.model.BaseEntity;

/**
 * SA电子游戏实体类
 * 
 * @author Administrator
 *
 */
public class SAElectronic extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private int electronicid;
	private String gameId;
	private String gameNameEN;
	private String gameType;
	private String imageFileName;
	private String gameNameCN;
	private String gameConfig;
	private String sequence;
	private int status;

	public SAElectronic() {
	}

	public int getElectronicid() {
		return electronicid;
	}

	public void setElectronicid(int electronicid) {
		this.electronicid = electronicid;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getGameNameEN() {
		return gameNameEN;
	}

	public void setGameNameEN(String gameNameEN) {
		this.gameNameEN = gameNameEN;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getGameNameCN() {
		return gameNameCN;
	}

	public void setGameNameCN(String gameNameCN) {
		this.gameNameCN = gameNameCN;
	}

	public String getGameConfig() {
		return gameConfig;
	}

	public void setGameConfig(String gameConfig) {
		this.gameConfig = gameConfig;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public Serializable getID() {
		return this.getElectronicid();
	}
}
