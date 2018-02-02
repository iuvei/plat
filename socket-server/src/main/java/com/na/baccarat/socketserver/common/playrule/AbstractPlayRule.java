package com.na.baccarat.socketserver.common.playrule;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.crsh.console.jline.internal.Log;

import com.na.baccarat.socketserver.common.enums.BetOrderWinLostStatusEnum;
import com.na.baccarat.socketserver.common.enums.TradeItemEnum;
import com.na.baccarat.socketserver.entity.BetOrder;
import com.na.user.socketserver.entity.RoundExtPO;
import com.na.user.socketserver.exception.SocketException;

/**
 * 不同玩法规则超类。
 * Created by sunny on 2017/4/24 0024.
 */
public abstract class AbstractPlayRule implements Serializable {
	
	protected BigDecimal resultAmount;
	
	protected BetOrderWinLostStatusEnum winLostStatus;
	
	protected BigDecimal rate;
	
	protected String gameReuslt;
	
	protected List<TradeItemEnum> gameReusltList;
	
	
	/**
	 * 计算最终输赢
	 * @param order
	 * @param roundExtPO
	 */
	public abstract void calResult(BetOrder order,RoundExtPO roundExtPO);
	
	/**
	 * 获取枚举游戏结果
	 * @return
	 */
	public abstract void getResultEnum();
	
	/**
	 * 	根据卡牌计算该局游戏结果
	 * 	位数
	 *	1  	 	1庄 2闲 3和    点数输赢
	 *	2		0无  1庄例牌  2闲例牌   3庄闲例牌
	 *	3 		0无  1庄对  2闲对  3庄闲对
	 *	4 		庄点数
	 */
	public void getResultStr(RoundExtPO roundExtPO) {
		if(roundExtPO.getPlayerCard1Number() == null || 
				roundExtPO.getPlayerCard2Number() == null || 
				roundExtPO.getBankCard1Number() == null ||
			roundExtPO.getBankCard2Number() == null) {
			throw SocketException.createError("scnner.not.over");
		}
		
		int bankerPoint = calBankerNum(roundExtPO);
		int playerPoint = calPlayerNum(roundExtPO);
		Log.debug("【百家乐结算】 庄点数:" + bankerPoint + ",闲点数:" + playerPoint);
		
		int first,second = 0,third = 0,fourth = 0;
		//计算第一位
		first = (bankerPoint > playerPoint) ? 1 : ((bankerPoint < playerPoint) ? 2 : 3);
		
		//计算第二位
		if(calBankerCase(roundExtPO)) {
			if(calPlayerCase(roundExtPO)) {
				second = 3;
			} else {
				second = 1;
			}
		} else if(calPlayerCase(roundExtPO)) {
			second = 2;
		}
		
		//计算第三位
		if(calBankerPairs(roundExtPO)) {
			if(calPlayerPairs(roundExtPO)) {
				third = 3;
			} else {
				third = 1;
			}
		} else if(calPlayerPairs(roundExtPO)) {
			third = 2;
		}
		
		//计算第四位
		fourth = bankerPoint;
		
		gameReuslt = String.valueOf(first) + String.valueOf(second) + String.valueOf(third) + String.valueOf(fourth) + "";
	}
	
    /**
     * 10以上（包含10）的均返回0,其他返回原数。
     * @param num
     * @return
     */
    protected static int cal(int num){
        return num >= 10 ? 0 : num;
    }
    
//    /**
//     * 计算是否需要博牌
//     */
//    public static void isBoCard(RoundExt roundExt) {
//    	if(roundExt.getBankCard1Num() == null || roundExt.getBankCard2Num() == null ||
//    			roundExt.getPlayerCard1Num() == null || roundExt.getPlayerCard2Num() == null) {
//    		throw SocketException.createError("该局牌张数不完整");
//    	}
//    	
//    	Integer playerPoint = getlastNum(roundExt.getPlayerCard1Num() + roundExt.getPlayerCard2Num());
//    	Integer bankerPoint = getlastNum(roundExt.getBankCard1Num() + roundExt.getBankCard2Num());
//    	
//    	boolean bankerIsBo,playerIsBo = false;
//    	if(playerPoint >= 0 && playerPoint <= 5) {
//    		playerIsBo = true;
//    	}
//    	
//    	if(bankerPoint >= 0 && bankerPoint <= 2) {
//    		bankerIsBo = true;
//    	}
//    	
//    }

    /**
     * 取个位数。
     * @param num
     * @return
     */
    protected static int getlastNum(int num){
        return num % 10;
    }
    
    /**
     * 计算庄对
     * @return
     */
    protected boolean calBankerPairs(RoundExtPO roundExtPO) {
    	if(roundExtPO.getBankCard1Number().compareTo(roundExtPO.getBankCard2Number()) == 0) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * 计算闲对
     * @return
     */
    protected boolean calPlayerPairs(RoundExtPO roundExtPO) {
    	if(roundExtPO.getPlayerCard1Number().compareTo(roundExtPO.getPlayerCard2Number()) == 0) {
    		return true;
    	}
    	return false;
    }
    
    /**
	 * 计算庄例牌
	 * @return
	 */
	private boolean calBankerCase(RoundExtPO roundExtPO) {
		int number = cal(roundExtPO.getBankCard1Number()) + cal(roundExtPO.getBankCard2Number());
		if(getlastNum(number) > 7) {
			return true;
		}
		return false;
	}
	
	/**
	 * 计算闲例牌
	 * @return
	 */
	private boolean calPlayerCase(RoundExtPO roundExtPO) {
		int number = cal(roundExtPO.getPlayerCard1Number()) + cal(roundExtPO.getPlayerCard2Number());
		if(getlastNum(number) > 7) {
			return true;
		}
		return false;
	}
    

    /**
     * 计算庄家点数。
     * @param roundExtPO
     * @return
     */
    protected int calBankerNum(RoundExtPO roundExtPO){
        int number = cal(roundExtPO.getBankCard1Number()) + cal(roundExtPO.getBankCard2Number()) + ((roundExtPO.getBankCard3Number() != null) ? cal(roundExtPO.getBankCard3Number()) : 0);
        return getlastNum(number);
    }

    /**
     * 计算闲家点数。
     * @param roundExtPO
     * @return
     */
    protected int calPlayerNum(RoundExtPO roundExtPO){
        int number = cal(roundExtPO.getPlayerCard1Number()) + cal(roundExtPO.getPlayerCard2Number()) + ((roundExtPO.getPlayerCard3Number() != null) ? cal(roundExtPO.getPlayerCard3Number()) : 0);
        return getlastNum(number);
    }

    
	public BigDecimal getResultAmount() {
		return resultAmount;
	}

	public BetOrderWinLostStatusEnum getWinLostStatus() {
		return winLostStatus;
	}

	public void setGameReuslt(String gameReuslt) {
		this.gameReuslt = gameReuslt;
	}

	public String getGameReuslt() {
		return gameReuslt;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public List<TradeItemEnum> getGameReusltList() {
		return gameReusltList;
	}
}
