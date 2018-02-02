package com.na.gate.vo;

import java.net.URLEncoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.na.gate.util.SHA256Util;
import com.na.gate.util.ZipUtils;
import com.na.manager.entity.AccountRecord;

/**
 * 发送账单数据给大厅。 Created by sunny on 2017/8/10 0010.
 */
public class SendAccountJson {
	private static Logger logger = LoggerFactory.getLogger(SendAccountJson.class);
	// 用户ID
	private String userId;

	// 游戏ID,请参照游戏ID查询
	private String gameId;

	// 签名值,根据userId, gameId, timestamp, records根据签名算法进行签名
	private String sign;

	// 时间戳，单位：毫秒数
	private Long timestamp;
	
	//如果是退出游戏是固定值为1,如果只是推送流水并没有退出游戏此值不传或者传其他值（如0）
	private Integer exit=1;
	
	@JSONField(serialize = false)
	private String gameKey;

	// 对账单数据列表zlib base64压缩后的数据，压缩请参照压缩算法，（压缩前为json数组）
	private String records;
	
	//如果值为1，表示没有压缩，其他值或者不传表示压缩的
	private Integer zlib;

	public SendAccountJson(Integer zlib,List<AccountRecord> records) throws Exception {
		this.zlib =zlib;
		String json = JSONObject.toJSONString(records);
		if(zlib ==0){
			byte[] data =json.getBytes("utf-8");
			data = ZipUtils.compress(json.getBytes("utf-8"));
			this.records = Base64Utils.encodeToString(data);
		}else{
			this.records =json;
		}
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	
	public String getSign() {
		sign =String.format("gameId%srecords%stimestamp%s", this.gameId,this.records,this.timestamp);
//		sign =String.format("gameId%srecords%stimestamp%suserId%s", this.gameId,this.records,this.timestamp,this.userId);
		sign =URLEncoder.encode(sign);
		sign = this.gameKey+sign+this.gameKey;
		sign = SHA256Util.SHA256Encrypt(sign);
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getRecords() {
		return records;
	}

	public void setRecords(String records) {
		this.records = records;
	}

	public String getGameKey() {
		return gameKey;
	}

	public void setGameKey(String gameKey) {
		this.gameKey = gameKey;
	}

	public Integer getExit() {
		return exit;
	}

	public void setExit(Integer exit) {
		this.exit = exit;
	}

	public Integer getZlib() {
		return zlib;
	}

	public void setZlib(Integer zlib) {
		this.zlib = zlib;
	}
}
