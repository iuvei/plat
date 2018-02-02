package com.na.manager.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.na.manager.common.annotation.I18NField;

import java.io.Serializable;

/**
 * Created by sunny on 2017/6/24 0024.
 */
public class Dict implements Serializable{
    @JSONField(deserialize = false)
    private String type;

    private String code;

    @I18NField
    private String name;

    private String value;

    @JSONField(deserialize = false)
    private int order;

    @JSONField(deserialize = false)
    private String remark;

    public Dict() {
    }

    public Dict(String type, String code, String value) {
        this.type = type;
        this.code = code;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
