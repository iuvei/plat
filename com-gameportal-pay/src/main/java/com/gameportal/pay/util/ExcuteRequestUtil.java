package com.gameportal.pay.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lechinepay.channel.lepay.client.BaseLePay;
import com.lechinepay.channel.lepay.client.DefaultLePay;
import com.lechinepay.channel.lepay.client.apppay.AppPay;
import com.lechinepay.channel.lepay.client.util.httpclient.SimpleHttpClient;
import com.lechinepay.channel.lepay.client.utils.ClientSecurityUtil;
import com.lechinepay.channel.lepay.sdk.util.LePaySignature;
import com.lechinepay.channel.lepay.security.exception.LePaySecurityException;
import com.lechinepay.channel.lepay.security.impl.DefaultLePayCertificateService;
import com.lechinepay.channel.lepay.security.impl.DefaultLePayCipherService;
import com.lechinepay.channel.lepay.security.impl.DefaultLePayKeyStoreService;
import com.lechinepay.channel.lepay.security.impl.DefaultLePaySignatureService;
/**
 * 发送请求
 * 
 * @author nicholas
 *
 */
public class ExcuteRequestUtil extends BaseLePay{
    public static Map<String, Object> excute(String serverLocation, String shortUrl, Map<String, Object> requestMap) {
        Map<String, Object> responseMap = AppPay.execute(serverLocation, shortUrl, requestMap);
        return responseMap;
    }

	  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultLePay.class);
	  public static final String ENCODING_DEFAULT_VALUE = "UTF-8";
	  public static final String VERSION_DEFAULT_VALUE = "1.0.0";
	  private DefaultLePayKeyStoreService defaultLePayKeyStoreService;
	  private DefaultLePayCertificateService defaultLePayCertificateService;
	  private DefaultLePayCipherService defaultLePayCipherService;
	  private DefaultLePaySignatureService defaultLePaySignatureService;
	  private String serverLocation = "http://172.168.19.103:8003/lepay.api";
	  private String shortUrl;

	  public ExcuteRequestUtil(String serverLocation, String shortUrl)
	  {
	    setServerLocation(serverLocation);
	    setShortUrl(shortUrl);
	  }

	  public ExcuteRequestUtil()
	  {
	  }

	  public DefaultLePayKeyStoreService getDefaultLePayKeyStoreService() {
	    return this.defaultLePayKeyStoreService;
	  }

	  public void setDefaultLePayKeyStoreService(DefaultLePayKeyStoreService defaultLePayKeyStoreService) {
	    this.defaultLePayKeyStoreService = defaultLePayKeyStoreService;
	  }

	  public DefaultLePayCertificateService getDefaultLePayCertificateService() {
	    return this.defaultLePayCertificateService;
	  }

	  public void setDefaultLePayCertificateService(DefaultLePayCertificateService defaultLePayCertificateService) {
	    this.defaultLePayCertificateService = defaultLePayCertificateService;
	  }

	  public DefaultLePayCipherService getDefaultLePayCipherService() {
	    return this.defaultLePayCipherService;
	  }

	  public void setDefaultLePayCipherService(DefaultLePayCipherService defaultLePayCipherService) {
	    this.defaultLePayCipherService = defaultLePayCipherService;
	  }

	  public DefaultLePaySignatureService getDefaultLePaySignatureService() {
	    return this.defaultLePaySignatureService;
	  }

	  public void setDefaultLePaySignatureService(DefaultLePaySignatureService defaultLePaySignatureService) {
	    this.defaultLePaySignatureService = defaultLePaySignatureService;
	  }

	  public String getServerLocation() {
	    return this.serverLocation;
	  }

	  public void setServerLocation(String serverLocation) {
	    this.serverLocation = serverLocation;
	  }

	  public String getShortUrl() {
	    return this.shortUrl;
	  }

	  public void setShortUrl(String shortUrl) {
	    this.shortUrl = shortUrl;
	  }

	  public Map<String, Object> execute(Map<String, Object> requestMap)
	  {
		this.setDefaultLePayKeyStoreService(ClientSecurityUtil.getDefaultLePayKeyStoreService());
		this.setDefaultLePayCertificateService(ClientSecurityUtil.getDefaultLePayCertificateService());
		this.setDefaultLePayCipherService(ClientSecurityUtil.getDefaultLePayCipherService());
		this.setDefaultLePaySignatureService(ClientSecurityUtil.getDefaultLePaySignatureService());
	    Map responseMap;
	    String result;
	    try
	    {
	      this.defaultLePayKeyStoreService.init();
	      this.defaultLePayCertificateService.init();
	      if (null == this.defaultLePaySignatureService) {
	        this.defaultLePaySignatureService = new DefaultLePaySignatureService();
	        this.defaultLePaySignatureService.setCertificateService(this.defaultLePayCertificateService);
	        this.defaultLePaySignatureService.setKeyStoreService(this.defaultLePayKeyStoreService);
	      }
	      try {
	        sign(requestMap);
	        result = send(requestMap);
	        responseMap = toMap(result);
	      }
	      catch (LePaySecurityException e)
	      {
	        LOGGER.error(e.getMessage(), e);
	        throw new RuntimeException(e);
	      }

	      boolean verify = verify(responseMap);
	      if (verify) {
	    	responseMap.put("reponseData", result);
	        return responseMap;
	      }
	      responseMap = new HashMap();
	      responseMap.put("respCode", "090909");
	      responseMap.put("respMsg", "Signature error.");
	      LOGGER.error("requestMap:" + requestMap);
	      LOGGER.error("responseMap:" + responseMap);
	    }
	    catch (Exception e) {
	      LOGGER.error(e.getMessage(), e);
	      responseMap = new HashMap();
	      responseMap.put("respCode", "999999");
	      responseMap.put("respMsg", "System error.");
	    }
	    return responseMap;
	  }

	  protected void sign(Map<String, Object> requestMap)
	    throws LePaySecurityException
	  {
	    String encoding = (String)requestMap.get("encoding");
	    if ((null == encoding) || (encoding.trim().isEmpty())) {
	      requestMap.put("encoding", "UTF-8");
	    }

	    String version = (String)requestMap.get("version");
	    if ((null == version) || (version.trim().isEmpty())) {
	      requestMap.put("version", "1.0.0");
	    }

	    requestMap.remove("signature");
	    String signData = LePaySignature.getSignContent(requestMap);
	    LOGGER.info("sign data:" + signData);
	    requestMap.put("signature", this.defaultLePaySignatureService.sign(signData, encoding));
	  }

	  protected String send(Map<String, Object> requestMap)
	  {
	    String encoding = (String)requestMap.get("encoding");
	    String signData = LePaySignature.getRequestParamString(requestMap, encoding);
	    String serverUrl = getServerLocation() + getShortUrl();
	    LOGGER.info("request url:" + serverUrl);
	    LOGGER.info("request data:" + signData);
	    String result = SimpleHttpClient.doPost(serverUrl, signData, encoding);
	    LOGGER.info("response data:" + result);
	    return result;
	  }

	  protected boolean verify(Map<String, Object> responseMap)
	  {
	    String encoding = (String)responseMap.get("encoding");
	    String lepaySignature = (String)responseMap.remove("signature");
	    String signData = LePaySignature.getSignContent(responseMap);
	    boolean verify = this.defaultLePaySignatureService.verify(signData, lepaySignature, encoding);
	    responseMap.put("signature", lepaySignature);
	    return verify;
	  }
}
