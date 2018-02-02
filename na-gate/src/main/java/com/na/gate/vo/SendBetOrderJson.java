package com.na.gate.vo;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.na.gate.proto.bean.BetItem;
import com.na.gate.util.SHA256Util;
import com.na.gate.util.ZipUtils;

/**
 * 发送给大厅的交易记录数据。 Created by sunny on 2017/8/21 0021.
 */
public class SendBetOrderJson {
	private static Logger logger = LoggerFactory.getLogger(SendBetOrderJson.class);
	// 压缩后的数据 压缩前前数据必须包括如下参数的数组
	private String records;

	private Long timestamp;

	private String gameType;

	private String sign;

	@JSONField(serialize = false)
	private String gameKey;

	public SendBetOrderJson() {
	}

	public SendBetOrderJson(List<BetOrderResponse> records) throws Exception {
		String json = JSONObject.toJSONString(records);
		byte[] data = ZipUtils.compress(json.getBytes("utf-8"));
		this.records = Base64Utils.encodeToString(data);
	}

	public SendBetOrderJson(List<BetOrderResponse> records, Long timestamp, String gameType) throws Exception {
		String json = JSONObject.toJSONString(records);
		logger.debug(json);
		byte[] data = ZipUtils.compress(json.getBytes("utf-8"));
		this.records = Base64Utils.encodeToString(data);
		this.timestamp = timestamp;
		this.gameType = gameType;
	}

	public List<BetItem> getBetUserList(List<BetOrderResponse> records) {
		List<BetItem> betList = new ArrayList<>();
		Map<String, BigDecimal> map =new HashMap<>();
		for(BetOrderResponse order:records){
			if(map.containsKey(order.getUserId())){
				map.put(order.getUserId(), map.get(order.getUserId()).add(order.getAmount()));
			}else{
				map.put(order.getUserId(), order.getAmount());
			}
		}
		BetItem item = null;
		Iterator entries = map.entrySet().iterator();
		while(entries.hasNext()){
			Map.Entry entry = (Map.Entry) entries.next(); 
			item =new BetItem(Long.valueOf(entry.getKey()+""),new BigDecimal(entry.getValue()+"").doubleValue());
			betList.add(item);
		}
		return betList;
	}

	public String getRecords() {
		return records;
	}

	public void setRecords(String records) {
		this.records = records;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getSign() {
		sign = String.format("gameType%srecords%stimestamp%s", this.gameType, this.records, this.timestamp);
		// logger.info(">>>>>>>>format:"+sign);
		sign = URLEncoder.encode(sign);
		// logger.info(">>>>>>>>encode:"+sign);
		sign = this.getGameKey() + sign + this.gameKey;
		// logger.info(">>>>>>>>gameKey:"+sign);
		sign = SHA256Util.SHA256Encrypt(sign);
		// System.out.println(">>>>>>>>sign:"+sign);
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getGameKey() {
		return gameKey;
	}

	public void setGameKey(String gameKey) {
		this.gameKey = gameKey;
	}
}
