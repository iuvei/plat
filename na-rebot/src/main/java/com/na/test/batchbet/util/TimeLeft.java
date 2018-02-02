package com.na.test.batchbet.util;

/**
 * Created by Administrator on 2017/5/13 0013.
 */
public class TimeLeft {
    private long startTime;
    private long endTime;

    public void start(){
        this.startTime = System.currentTimeMillis();
    }

    public long end(){
        this.endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
