package com.na.gate.listen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.Subscribe;
import com.na.gate.common.SpringContextUtil;
import com.na.gate.proto.SocketClient;
import com.na.gate.proto.bean.MerchantRequest;
import com.na.gate.proto.bean.PengMerchantQueueResponse;

/**
 * @author Andy
 * @version 创建时间：2017年9月26日 下午4:55:32
 */
@Component
public class MerchantListen {
	private Logger logger = LoggerFactory.getLogger(MerchantListen.class);
	private SocketClient client;

	@Subscribe
	public void onPengMerchantQueue(PengMerchantQueueResponse response) {
		client =(SocketClient)SpringContextUtil.getBean("socketClient");
		logger.info("开始请求商户{}信息!",response.getParentId());
		MerchantRequest request = new MerchantRequest();
		request.setJson(response.getParentId());
		client.send(request);
	}
}
