package com.na.baccarat.socketserver.command.sendpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.CommandResponse;

import java.util.List;

/**
 * 好路返回的数据类型
 * @author Administrator
 *
 */
public class GoodRoadResponse extends CommandResponse {
	//好路类型
	private Integer  gtp;
	//桌子ID
	private Integer tid;
	//桌子状态
	private Integer stu;
	//桌子名字
	private String tableName;
	//开始投注倒计时
	private Integer countDownSeconds;
    //当前桌玩法列表
    @JSONField(name = "playList")
    private List<PlayInfo> playList;
	/**
	 * 玩法信息
	 */
	public class PlayInfo {
		
		/**
	     * 玩法Id
	     */
	    public Integer id;
	    /**
	     * 玩法名称
	     */
	    public String name;
		/**
	     * 交易项列表
	     */
	    @JSONField(name = "tradeList")
	    public List<TradeItemInfo> tradeList;
	}
	/**
	 * 交易项
	 */
	public class TradeItemInfo {
		/**
	     * 交易项ID
	     */
	    @JSONField(name = "id")
	    public Integer id;
		/**
	     * 交易项对应投注区标识
	     */
	    @JSONField(name = "key")
	    public String key;
		/**
	     * 名称
	     */
	    @JSONField(name = "name")
	    public String name;
		/**
	     * 赔率
	     */
	    @JSONField(name = "rate")
	    public Double rate;
	}


	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<PlayInfo> getPlayList() {
		return playList;
	}
	public void setPlayList(List<PlayInfo> playList) {
		this.playList = playList;
	}
	public Integer getCountDownSeconds() {
		return countDownSeconds;
	}
	public void setCountDownSeconds(Integer countDownSeconds) {
		this.countDownSeconds = countDownSeconds;
	}
	public Integer getGtp() {
		return gtp;
	}
	public void setGtp(Integer gtp) {
		this.gtp = gtp;
	}
	public Integer getStu() {
		return stu;
	}
	public void setStu(Integer stu) {
		this.stu = stu;
	}
	public Integer getTid() {
		return tid;
	}
	public void setTid(Integer tid) {
		this.tid = tid;
	}
	
	
	
	
}
