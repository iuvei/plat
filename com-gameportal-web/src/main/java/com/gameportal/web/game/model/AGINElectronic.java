package com.gameportal.web.game.model;

import java.io.Serializable;

import com.gameportal.web.user.model.BaseEntity;

/**
 * AGIN电子游戏实体类
 * 
 * @author Administrator
 *
 */
public class AGINElectronic extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private int electronicid;
	private String gameCode;// 游戏代码
	private String category;// 游戏分类
	private String gameEnName;// 游戏英文名称
	private String imageFileName;// 游戏图标
	private String gameNameCN;// 游戏中文名称
	private String sequence;// 排序
	private int status;

	public int getElectronicid() {
		return electronicid;
	}

	public void setElectronicid(int electronicid) {
		this.electronicid = electronicid;
	}

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getGameEnName() {
		return gameEnName;
	}

	public void setGameEnName(String gameEnName) {
		this.gameEnName = gameEnName;
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
