package com.na.baccarat.socketserver.command.sendpara;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.user.socketserver.command.sendpara.IResponse;

/**
 * 投注成功响应。
 * Created by sunny on 2017/5/3 0003.
 */
public class BetResponse implements IResponse {
	
	/**投注所在的实体桌**/
	public Integer tid;
    /**投注用户ID*/
    public Long userId;
    /**投注来源 **/
    public Integer source;
    /**投注交易项列表*/
    private List<Integer> tradeItemIds = new ArrayList<>();

    /**
     * 虚拟桌用户当前余额列表。key 用户ID，val 余额。
     */
    @JSONField(name = "usersBlance")
    private List<UserBalance> virtualTableUsersBalanceList;

    public void addVirtualTableUsersBalance(Long userId,BigDecimal balance){
        if (virtualTableUsersBalanceList==null){
            virtualTableUsersBalanceList = new ArrayList<>();
        }
        UserBalance userBlance = new UserBalance();
        userBlance.userId = userId;
        userBlance.balance = balance;
        virtualTableUsersBalanceList.add(userBlance);
    }

    public List<UserBalance> getVirtualTableUsersBalanceList() {
        return virtualTableUsersBalanceList;
    }

    public void setVirtualTableUsersBalanceList(List<UserBalance> virtualTableUsersBalanceList) {
        this.virtualTableUsersBalanceList = virtualTableUsersBalanceList;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	public void addTradeItemId(Integer tradeItemId){
        this.tradeItemIds.add(tradeItemId);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Integer> getTradeItemIds() {
        return tradeItemIds;
    }

    public void setTradeItemIds(List<Integer> tradeItemIds) {
        this.tradeItemIds = tradeItemIds;
    }

    public static class UserBalance{
        public Long userId;
        public BigDecimal balance;
    }
}
