package com.gameportal.web.sms.service;

import com.gameportal.web.sms.model.SmsPlatAccount;


public abstract interface ISmsService {
	/**
	 * 发送短信接口。
	 * 
	 * @param mobile
	 *            要发送的手机号码(多个手机用","分隔。一次最多99个号码)
	 * @param message
	 *            短信内容，一条短信最大长度，视所选择的通道而不同。(通道二：64个字；即时通道：50个字)
	 * @param time
	 *            短信发送的时间(值为空或比当前时间小即是即时发送)。
	 * @param type
	 *            (通道选择 0：默认通道； 2：通道2； 3：即时通道)
	 * @return 返回发送结果。
	 */
	String sendSMS(String mobile, String message, String time, String type) throws Exception;
	/**
	 * 获取短信平台信息。
	 * @return 返回短信平台信息。
	 */
	SmsPlatAccount getSmsPlatAccount();
}
