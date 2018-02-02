package com.na.manager.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 现场投注响应。
 * Created by sunny on 2017/6/17 0017.
 */
public class LiveBetResponse {
    private int liveUserNum;
    private int betUserNum;
    private List<LiveBetBean> liveBetBeans = new ArrayList<>();

    public int getLiveUserNum() {
        return liveUserNum;
    }

    public void setLiveUserNum(int liveUserNum) {
        this.liveUserNum = liveUserNum;
    }

    public int getBetUserNum() {
        return betUserNum;
    }

    public void setBetUserNum(int betUserNum) {
        this.betUserNum = betUserNum;
    }

    public List<LiveBetBean> getLiveBetBeans() {
        return liveBetBeans;
    }

    public void addLiveBetBeans(List beans){
        this.liveBetBeans.addAll(beans);
    }
}
