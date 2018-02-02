package com.na.manager.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 黑白名单表
 * 
 * @author
 * @time 2015-07-21 18:13:40
 */
public class IpBlackWhiteAddr implements Serializable {

	private static final long serialVersionUID = 6862916436771660088L;
	private Long id;
	/**
	 * 明文起始IP
	 */
	private String start;
	/**
	 * 明文截止IP
	 */
	private String end;
	/**
	 * 数字起始IP
	 */
	private Long startNum;
	/**
	 * 数字截止IP
	 */
	private Long endNum;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private String createBy;

	/**
	 * 0.全部 1.游戏前台 2.管理网后台 3.API service
	 */
	private Integer platType;
	/**
	 * 1:白名单 2：黑名单
	 */
	private Integer ipType;
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 区域
	 */
	private String area;

	private Set<String> ips;

	private List<Long> ids;
	
	private String filePath;
	
	public String getKey(){
		return StringUtils.join(new Object[]{platType,ipType},".");
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public Long getStartNum() {
		return startNum;
	}

	public void setStartNum(Long startNum) {
		this.startNum = startNum;
	}

	public Long getEndNum() {
		return endNum;
	}

	public void setEndNum(Long endNum) {
		this.endNum = endNum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Integer getPlatType() {
		return platType;
	}

	public void setPlatType(Integer platType) {
		this.platType = platType;
	}

	public Integer getIpType() {
		return ipType;
	}

	public void setIpType(Integer ipType) {
		this.ipType = ipType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Set<String> getIps() {
		return ips;
	}

	public void setIps(Set<String> ips) {
		this.ips = ips;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}