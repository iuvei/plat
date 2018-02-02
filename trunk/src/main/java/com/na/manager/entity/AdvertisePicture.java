package com.na.manager.entity;

import java.io.Serializable;

/**
 * 广告图片
 *
 * @create 2017-07
 */
public class AdvertisePicture implements Serializable{

    private static final long serialVersionUID = 6675519232550521814L;
    private Integer id;
    private Integer advertiseId;
    private Integer order;
    private String url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdvertiseId() {
        return advertiseId;
    }

    public void setAdvertiseId(Integer advertiseId) {
        this.advertiseId = advertiseId;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
