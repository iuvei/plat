package com.na.baccarat.socketserver.command.sendpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.IResponse;


/**
 * 新一靴(返回数据)
 * body:{tid:台桌ID,bid:"靴ID","bn":"第几靴","rn":"第几局"}
 * @author Administrator
 *
 */
public class NewBootResponse implements IResponse {
	/**
	 * 台桌ID
	 */
	@JSONField(name = "tid")
	private Integer tid;
	/**
	 * 靴ID
	 */
	@JSONField(name = "bid")
	private String bid;
	/**
	 * 第几靴
	 */
	@JSONField(name = "bn")
	private Integer bNum;
	/**
	 * 第几局
	 */
	@JSONField(name = "rn")
	private Integer num;
	
	/**
     * 桌子展示状态
     */
    private String showTableStatus;
	
	public Integer getTid() {
		return tid;
	}
	public void setTid(Integer tid) {
		this.tid = tid;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public Integer getbNum() {
		return bNum;
	}
	public void setbNum(Integer bNum) {
		this.bNum = bNum;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getShowTableStatus() {
		return showTableStatus;
	}
	public void setShowTableStatus(String showTableStatus) {
		this.showTableStatus = showTableStatus;
	}
	 
	
}
