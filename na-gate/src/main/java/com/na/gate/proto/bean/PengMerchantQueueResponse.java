package com.na.gate.proto.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * 待处理商户队列
 * 
 * @author Andy
 * @version 创建时间：2017年9月26日 下午5:20:58
 */
public class PengMerchantQueueResponse {
	private String parentId;
	
	private String lowerId;

	private MerchantJson orginData;
	
	public static void main(String[] args) {
		String s="{\"username\":\"YYSH03_YYmerchant03\",\"role\":\"100\",\"id\":\"26570fd4-dbc6-4047-abdc-66b2347974be\",\"nickname\":\"歪歪三号商户昵称\",\"headPic\":\"NULL!\",\"parentId\":\"5d2a37e5-f1d1-4f22-9826-48d0217ddd6a\",\"msn\":\"8\",\"suffix\":\"YYSH03\",\"levelIndex\":\"01,e15adc7c-80f3-40bc-a473-e0d465214b71,f7e2e060-0cab-44e6-9211-cc547ebfe201,5d2a37e5-f1d1-4f22-9826-48d0217ddd6a\",\"liveMix\":-1,\"vedioMix\":-1,\"rate\":70,\"gameList\":[\"10000\",\"30000\"],\"merUrl\":\"Http://www.baidu.com\"}";
		PengMerchantQueueResponse response = new PengMerchantQueueResponse("5d2a37e5-f1d1-4f22-9826-48d0217ddd6a", JSONObject.parseObject(s,MerchantJson.class));
		System.out.println(response.findLowerId());
	}
	
	protected String findLowerId(){
		String[] lis = orginData.getLevelIndex().split(",");
		for(int i=0;i<lis.length;i++){
			if(lis[i].equals(parentId) && i+1<lis.length){
				return lis[i+1];
			}
		}
		return null;
	}

	public PengMerchantQueueResponse(String parentId, MerchantJson orginData) {
		super();
		this.parentId = parentId;
		this.lowerId = getLowerId();
		this.orginData = orginData;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getLowerId() {
		return lowerId;
	}

	public void setLowerId(String lowerId) {
		this.lowerId = lowerId;
	}

	public MerchantJson getOrginData() {
		return orginData;
	}

	public void setOrginData(MerchantJson orginData) {
		this.orginData = orginData;
	}

	
}
