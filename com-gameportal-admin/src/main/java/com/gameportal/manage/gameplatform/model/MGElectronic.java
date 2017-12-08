package com.gameportal.manage.gameplatform.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;


/**
 * 电子游戏实体类
 * @author Administrator
 *
 */
public class MGElectronic extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -499517252713309235L;
	
	private int electronicid;
	private String category;//游戏分类
	private String categoryID;//游戏ID
	private String gameEnName;//游戏英文名称
	private String imageFileName;//游戏图标
	private String gameName;//游戏名称
	private String gameNameCN;//游戏中文名称
	private String sequence;//排序
	private int status;
	private int prizepool;//是否显示奖金 
	
	
	public int getElectronicid() {
		return electronicid;
	}


	public void setElectronicid(int electronicid) {
		this.electronicid = electronicid;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getCategoryID() {
		return categoryID;
	}


	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
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


	public String getGameName() {
		return gameName;
	}


	public void setGameName(String gameName) {
		this.gameName = gameName;
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
	
	


	public int getPrizepool() {
		return prizepool;
	}


	public void setPrizepool(int prizepool) {
		this.prizepool = prizepool;
	}


	@Override
	public String toString() {
		return "MGElectronic [electronicid=" + electronicid + ", category="
				+ category + ", categoryID=" + categoryID + ", gameEnName="
				+ gameEnName + ", imageFileName=" + imageFileName
				+ ", gameName=" + gameName + ", gameNameCN=" + gameNameCN
				+ ", sequence=" + sequence + ", status=" + status + "]";
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result
				+ ((categoryID == null) ? 0 : categoryID.hashCode());
		result = prime * result + electronicid;
		result = prime * result
				+ ((gameEnName == null) ? 0 : gameEnName.hashCode());
		result = prime * result
				+ ((gameName == null) ? 0 : gameName.hashCode());
		result = prime * result
				+ ((gameNameCN == null) ? 0 : gameNameCN.hashCode());
		result = prime * result
				+ ((imageFileName == null) ? 0 : imageFileName.hashCode());
		result = prime * result
				+ ((sequence == null) ? 0 : sequence.hashCode());
		result = prime * result + status;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MGElectronic other = (MGElectronic) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (categoryID == null) {
			if (other.categoryID != null)
				return false;
		} else if (!categoryID.equals(other.categoryID))
			return false;
		if (electronicid != other.electronicid)
			return false;
		if (gameEnName == null) {
			if (other.gameEnName != null)
				return false;
		} else if (!gameEnName.equals(other.gameEnName))
			return false;
		if (gameName == null) {
			if (other.gameName != null)
				return false;
		} else if (!gameName.equals(other.gameName))
			return false;
		if (gameNameCN == null) {
			if (other.gameNameCN != null)
				return false;
		} else if (!gameNameCN.equals(other.gameNameCN))
			return false;
		if (imageFileName == null) {
			if (other.imageFileName != null)
				return false;
		} else if (!imageFileName.equals(other.imageFileName))
			return false;
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		if (status != other.status)
			return false;
		return true;
	}


	@Override
	public Serializable getID() {
		return this.getElectronicid();
	}
	
}
