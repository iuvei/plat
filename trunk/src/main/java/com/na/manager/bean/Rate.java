package com.na.manager.bean;

import java.util.concurrent.TimeUnit;

/**
 * 速率设置。
 * number/timer
 * Created by Sunny on 2017/8/19 0019.
 */
public class Rate {
    private int number;
    //单位：秒
    private int timer;

    public Rate(int number, int timer) {
        this.number = number;
        this.timer = timer;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public double getRate(){
        return (double) number/timer;
    }
}
