package com.na.user.socketserver.command.sendpara;

import java.math.BigDecimal;
import java.util.List;

/**
 * 查询投注记录响应
 * 
 * @author alan
 * @date 2017年5月4日 上午10:26:14
 */
public class QueryBetRecordResponse implements IResponse {

	public class BetInfo {
		
		public Integer gameId;
		
		//局号
		public Long roundId;
		
		public String date;
		
		public String game;
		
		public String bets;
		
		public String result;
		
		public BigDecimal winAmount;

		public BetInfo() {
			
		}

	}
	
	private List<BetInfo> data;
	
	private Integer rows;

	public List<BetInfo> getData() {
		return data;
	}

	public void setData(List<BetInfo> data) {
		this.data = data;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	
	
}
