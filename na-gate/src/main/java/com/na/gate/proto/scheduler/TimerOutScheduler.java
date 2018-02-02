package com.na.gate.proto.scheduler;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by sunny on 2017/6/6 0006.
 */
public class TimerOutScheduler {
    private HashedWheelTimer wheelTimer = new HashedWheelTimer(new DefaultThreadFactory("HashedWheelTimer"));
    private Map<SchedulerKey,Timeout> scheduledFutures = new ConcurrentHashMap<>();

    /**
     * 新建定时任务。
     * @param key
     * @param timerTask
     * @param delay
     * @param unit
     */
    public void newTimeout(SchedulerKey key, TimerTask timerTask, long delay, TimeUnit unit){
        Timeout timeout = wheelTimer.newTimeout(timerTask,delay,unit);
        scheduledFutures.put(key,timeout);
    }

    /**
     * 取消任务。
     * @param key
     */
    public void cancelTimeout(SchedulerKey key){
        Timeout timeout = scheduledFutures.remove(key);
        if(timeout!=null) {
            timeout.cancel();
        }
    }
}
