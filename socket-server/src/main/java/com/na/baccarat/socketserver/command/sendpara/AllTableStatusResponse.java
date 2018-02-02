package com.na.baccarat.socketserver.command.sendpara;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 该实体具有特殊性，不能用默认的json序列化。
 * Created by sunny on 2017/4/28 0028.
 */
public class AllTableStatusResponse{
    @JSONField(name = "id")
    private Long loginStatusId;
    @JSONField(name = "tm",format = "yyyy-MM-dd HH:mm:ss")
    private Date currentTime;
    /**桌子状态：key gameTableId*/
    @JSONField(serialize = false)
    private Map<String,TableStatus> tableStatusMap = new HashMap<>();

    public Long getLoginStatusId() {
        return loginStatusId;
    }

    public void setLoginStatusId(Long loginStatusId) {
        this.loginStatusId = loginStatusId;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public Map<String, TableStatus> getTableStatusMap() {
        return tableStatusMap;
    }

    public void addTableStatus(TableStatus tableStatus) {
        this.tableStatusMap.put(tableStatus.getGameTableId()+"",tableStatus);
    }

    public JSONObject toJsonObject(){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject obj = new JSONObject();
        obj.put("id",this.getLoginStatusId());
        obj.put("tm",sf.format(this.currentTime));
        obj.putAll(this.tableStatusMap);
        return obj;
    }
}


