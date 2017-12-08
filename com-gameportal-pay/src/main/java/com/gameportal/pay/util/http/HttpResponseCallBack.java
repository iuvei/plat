package com.gameportal.pay.util.http;

import java.io.IOException;
import java.io.InputStream;
/**
 * 
 * �ص��ӿ�
 */
public interface HttpResponseCallBack {

	public void processResponse(InputStream responseBody) throws IOException;
}
