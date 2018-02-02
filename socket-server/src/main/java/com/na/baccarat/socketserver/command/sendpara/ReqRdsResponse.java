package com.na.baccarat.socketserver.command.sendpara;

import java.util.List;

import com.na.baccarat.socketserver.entity.Round;

/**
 * 查看路子返回信息
 *  body:{ {"gid":游戏ID, "tid":台桌ID, "stu":桌子状态,did:"荷官ID",dna:"荷官昵称",kind:"桌子的类别(普通，VIP，龙湖)"
 * "rds":[{rid:"局ID","rn":"局数",ret:"结果"},{rid:"局ID","rn":"局数",ret:"结果"}....]}
 * @author Administrator
 *
 */
public class ReqRdsResponse {
	/**
	 * 台桌ID
	 */
	private Integer tid; 
	/**
	 * 靴ID
	 */
	private String bid; 
	/**
	 * 荷官ID
	 */
	private Long did;
	/**
	 *荷官昵称 
	 */
	private String dna;
	/**
	 * 头像信息
	 */
	private String dpic;
	/**
	 * 桌子状态
	 */
	private Integer stu;
	/**
	 * 桌子类型
	 */
	private Integer tp;
	
	/**
	 * round信息
	 */
	private List<Round> rds;
	
	/**
	 * 包台投注之后倒计时
	 */
	private Long tms;
	/**
	 * 投注倒计时
	 */
	private Long ht;
	/**
	 * 游戏ID
	 */
	private Integer gid;
	
	/**
	 * 靴ID
	 */
	private Long rid;
	
	
	public Long getRid() {
		return rid;
	}
	public void setRid(Long rid) {
		this.rid = rid;
	}
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
	public Long getDid() {
		return did;
	}
	public void setDid(Long did) {
		this.did = did;
	}
	public String getDna() {
		return dna;
	}
	public void setDna(String dna) {
		this.dna = dna;
	}
	public String getDpic() {
		return dpic;
	}
	public void setDpic(String dpic) {
		this.dpic = dpic;
	}
	public Integer getStu() {
		return stu;
	}
	public void setStu(Integer stu) {
		this.stu = stu;
	}
	public Integer getTp() {
		return tp;
	}
	public void setTp(Integer tp) {
		this.tp = tp;
	}
	public List<Round> getRds() {
		return rds;
	}
	public void setRds(List<Round> rds) {
		this.rds = rds;
	}
	public Long getTms() {
		return tms;
	}
	public void setTms(Long tms) {
		this.tms = tms;
	}
	public Long getHt() {
		return ht;
	}
	public void setHt(Long ht) {
		this.ht = ht;
	}
	public Integer getGid() {
		return gid;
	}
	public void setGid(Integer gid) {
		this.gid = gid;
	}
	
	
	
	
}
