package com.gameportal.pay.utils;


import java.net.URLEncoder;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.gameportal.pay.model.fcs.request.FCSOpenApiRequest;
import com.gameportal.pay.model.fcs.response.FCSOpenApiResponse;
import com.gameportal.pay.util.WebConst;


public class DefaultFCSOpenApiClient
implements FCSOpenApiClient
{
private final String fcsPublicKey;
private final String privateKey;
private String charset = "UTF-8";

private final int connectTimeout = 3000;

private final int readTimeout = 15000;
private static final String PARAM_URL_NAME = "content";
private static final String SIGN_URL_NAME = "sign";
private static final String SIGN_TYPE_URL_NAME = "sign_type";
private static final String CHARSET_URL_NAME = "input_charset";
private static final String SIGN_TYPE = "SHA1WithRSA";

public DefaultFCSOpenApiClient(String clientPublicKey, String privateKey)
{
  this.fcsPublicKey = clientPublicKey;
  this.privateKey = privateKey;
}

public DefaultFCSOpenApiClient(String fcsPublicKey, String privateKey, String charset) {
  this.fcsPublicKey = fcsPublicKey;
  this.privateKey = privateKey;
  this.charset = charset;
}

public FCSOpenApiResponse excute(FCSOpenApiRequest request, String apiUrl) throws Exception {
  Map params = request.getTextParams();
  String paramsString = WebUtil.buildAlphabeticalSortedQuery(params);
  Map contents = new HashMap();
  System.out.println("请求参数： " + paramsString);
  contents.put("partner", request.getPartner());
  contents.put(PARAM_URL_NAME, RSACoderUtil.getParamsWithDecodeByPublicKey(paramsString, this.charset, this.fcsPublicKey));
  contents.put(CHARSET_URL_NAME, this.charset);
  contents.put(SIGN_TYPE_URL_NAME, "SHA1WithRSA");
  contents.put(SIGN_URL_NAME, URLEncoder.encode(RSACoderUtil.sign(paramsString.getBytes(this.charset), this.privateKey), this.charset));
  String result = WebUtil.doPost(apiUrl, contents, this.charset, 3000, 15000);
  FCSOpenApiResponse openApiResponse = (FCSOpenApiResponse)JSONObject.parseObject(result, FCSOpenApiResponse.class);
  return openApiResponse;
}

static
{
  Security.setProperty("jdk.certpath.disabledAlgorithms", "");
}
}