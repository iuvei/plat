package com.gameportal.web.user.model;

import java.io.Serializable;
import java.util.Date;

import com.gameportal.web.util.DateUtil;

/**
 * 会员是否洗码标示
 * @author Administrator
 *
 */
public class XimaFlag extends BaseEntity{
	private static final long serialVersionUID = -6551493792380778890L;
	
	private Integer flagid;
	
	/**
	 * 会员UIID
	 */
	private Long flaguiid;
	
	/**
	 * 会员账号
	 */
	private String flagaccount;
	
	/**
	 * 是否洗码
	 * 0：不洗码
	 * 1：洗码
	 * 抓取数据的时候根据此状态设置betlog表的flag标识是否要洗码
	 */
	private int isxima;
	
	/**
	 * 更新时间
	 */
	private String updatetime = DateUtil.getStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 描述
	 */
	private String remark;
	

	public Integer getFlagid() {
		return flagid;
	}

	public void setFlagid(Integer flagid) {
		this.flagid = flagid;
	}

	public Long getFlaguiid() {
		return flaguiid;
	}

	public void setFlaguiid(Long flaguiid) {
		this.flaguiid = flaguiid;
	}

	public String getFlagaccount() {
		return flagaccount;
	}

	public void setFlagaccount(String flagaccount) {
		this.flagaccount = flagaccount;
	}

	public int getIsxima() {
		return isxima;
	}

	public void setIsxima(int isxima) {
		this.isxima = isxima;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "XimaFlag [flagid=" + flagid + ", flaguiid=" + flaguiid
				+ ", flagaccount=" + flagaccount + ", isxima=" + isxima
				+ ", updatetime=" + updatetime + "]";
	}

	@Override
	public Serializable getID() {
		return this.getFlagid();
	}

}
