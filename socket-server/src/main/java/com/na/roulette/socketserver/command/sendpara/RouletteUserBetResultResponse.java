package com.na.roulette.socketserver.command.sendpara;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.CommandResponse;


/**
 * 用户下注结果
 * 
 * @author alan
 * @date 2017年5月1日 下午12:26:08
 */
public class RouletteUserBetResultResponse extends CommandResponse {
	
	
	/**
     * 游戏结果
     */
    private String result;
    /**
     * 赢取金额
     */
    private BigDecimal money;
   
    /**
     * 输赢细节
     */
    private List<Result> betDetail;
    
    /**
     * 展示状态
     */
    private String showTableStatus;
    
    public static class Result{
    	/**
    	 * 交易项的ID
    	 */
    	@JSONField(name = "id")
    	public Integer id;
        /**
         * 投注金额
         */
    	@JSONField(name = "amount")
        public BigDecimal amount;
    	
    	/**
    	 * 输赢金额
    	 */
    	@JSONField(name = "winAmount")
    	public BigDecimal winAmount;

    }

    
    public List<Result> getBetDetail() {
		return betDetail;
	}

	public void setBetDetail(List<Result> betDetail) {
		this.betDetail = betDetail;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getShowTableStatus() {
		return showTableStatus;
	}

	public void setShowTableStatus(String showTableStatus) {
		this.showTableStatus = showTableStatus;
	}

}
