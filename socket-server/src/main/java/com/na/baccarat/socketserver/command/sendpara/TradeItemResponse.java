package com.na.baccarat.socketserver.command.sendpara;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.IResponse;

/**
 * 获取交易项列表返回参数
 * 
 * @author alan
 * @date 2017年5月1日 下午12:26:08
 */
public class TradeItemResponse implements IResponse {
	
	
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
	
	
   
    /**
     * 当前桌玩法列表
     */
    @JSONField(name = "playList")
    private List<PlayInfo> playList;
    
    /**
     * 交易项
     */
    @JSONField(name = "tradeItemList")
    private List<TradeItemInfo> tradeItemList;
  
	public List<PlayInfo> getPlayList() {
		return playList;
	}
	public void setPlayList(List<PlayInfo> playList) {
		this.playList = playList;
	}	
	public List<TradeItemInfo> getTradeItemList() {
		return tradeItemList;
	}
	public void setTradeItemList(List<TradeItemInfo> tradeItemList) {
		this.tradeItemList = tradeItemList;
	}
    
}
