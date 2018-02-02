package com.na.manager.util;

/**
 * 记录时间。
 * Created by sunny on 2017/5/13 0013.
 */
public class TimeLeft {
    private long startTime;
    private long endTime;

    public TimeLeft(){
        this.start();
    }

    public void start(){
        this.startTime = System.currentTimeMillis();
    }

    public long end(){
        this.endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
