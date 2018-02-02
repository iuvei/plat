package com.na.manager.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 广告
 *
 * @create 2017-07
 */
public class Advertise implements Serializable{
    private static final long serialVersionUID = -5960680919662043225L;
    private Integer id;
    private Integer type;
    private Integer platform;
    private String remark;
    private List<AdvertisePicture> advertisePictures;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<AdvertisePicture> getAdvertisePictures() {
        return advertisePictures;
    }

    public void setAdvertisePictures(List<AdvertisePicture> advertisePictures) {
        this.advertisePictures = advertisePictures;
    }
}
