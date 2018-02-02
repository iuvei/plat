package com.na.manager.enums;

/**
 * 缓存枚举。
 * Created by sunny on 2017/6/22 0022.
 */
public enum CacheEnum{
    menu(8640000),
    ;
    CacheEnum() {
    }

    CacheEnum(int ttl) {
        this.ttl = ttl;
    }

    CacheEnum(int ttl, int maxSize) {
        this.ttl = ttl;
        this.maxSize = maxSize;
    }

    private int maxSize = 50000;    //最大數量
    private int ttl = 10;        //过期时间（秒）

    public int getMaxSize() {
        return maxSize;
    }
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
    public int getTtl() {
        return ttl;
    }
    public void setTtl(int ttl) {
        this.ttl = ttl;
    }
}
