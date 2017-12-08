package com.gameportal.manage.sitesettings.model;

import java.io.Serializable;

import com.gameportal.manage.system.model.BaseEntity;

/**
 * 门户广告管理
 * @author Administrator
 *
 */
public class WebAdEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4732394668616319433L;
	
	/**
	 * 主键列
	 */
	private Long adid;
	
	/**
	 * 广告标题
	 */
	private String adtitle;
	
	/**
	 * 广告连接地址
	 */
	private String adhref;
	
	/**
	 * 广告图片
	 */
	private String adimages;
	
	/**
	 * 广告高度
	 */
	private String adheight;
	
	/**
	 * 广告宽度
	 */
	private String adwidth;
	
	/**
	 * 广告边界颜色值
	 */
	private String adcolor;
	
	/**
	 * 状态
	 * 0：不显示
	 * 1：正常
	 */
	private int status;
	
	/**
	 * 排序
	 */
	private String adsequence;
	
	/**
	 * 广告地址
	 * index:首页
	 * index_xs:线上站首页
	 */
	private String adlocation;
	
	/**
	 * 编辑时间
	 */
	private String edittime;

	
	public Long getAdid() {
		return adid;
	}

	public void setAdid(Long adid) {
		this.adid = adid;
	}

	public String getAdtitle() {
		return adtitle;
	}

	public void setAdtitle(String adtitle) {
		this.adtitle = adtitle;
	}

	public String getAdhref() {
		return adhref;
	}

	public String getAdimages() {
		return adimages;
	}

	public void setAdimages(String adimages) {
		this.adimages = adimages;
	}

	public void setAdhref(String adhref) {
		this.adhref = adhref;
	}

	public String getAdheight() {
		return adheight;
	}

	public void setAdheight(String adheight) {
		this.adheight = adheight;
	}

	public String getAdwidth() {
		return adwidth;
	}

	public void setAdwidth(String adwidth) {
		this.adwidth = adwidth;
	}

	public String getAdcolor() {
		return adcolor;
	}

	public void setAdcolor(String adcolor) {
		this.adcolor = adcolor;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAdsequence() {
		return adsequence;
	}

	public void setAdsequence(String adsequence) {
		this.adsequence = adsequence;
	}

	public String getAdlocation() {
		return adlocation;
	}

	public void setAdlocation(String adlocation) {
		this.adlocation = adlocation;
	}

	public String getEdittime() {
		return edittime;
	}

	public void setEdittime(String edittime) {
		this.edittime = edittime;
	}
	
	

	@Override
	public String toString() {
		return "WebAdEntity [adid=" + adid + ", adtitle=" + adtitle
				+ ", adhref=" + adhref + ", adheight=" + adheight
				+ ", adwidth=" + adwidth + ", adcolor=" + adcolor + ", status="
				+ status + ", adsequence=" + adsequence + ", adlocation="
				+ adlocation + ", edittime=" + edittime + "]";
	}

	@Override
	public Serializable getID() {
		// TODO Auto-generated method stub
		return this.getAdid();
	}

}
