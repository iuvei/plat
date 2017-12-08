package com.gameportal.manage.pay.model.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;

import com.gameportal.manage.util.DateUtil;

public class THWithdrawal {
	private String merchantCode = "14251683";// 商户号
	private String merchantKey = "b7b0333efdf3443eaa44b7819af1fd06";// 商户KEY
	private String bankAccount = "";// 银行户名
	private String bankCardNo = "";// 银行账号
	private String bankCode = "ICBC";// 银行编码
	private String amount = "0.01";// 金额
	private String orderNo;
	private String getUrl = "http://pay.41.cn/remit";
	private String getBalanceUrl = "http://pay.41.cn/remit/balance";

	public static void main(String[] args) {
		System.out.println(new THWithdrawal().queryBlance());
	}

	public String remit() {
		Map<String, String> params = new HashMap<>();
		params.put(ParamNames.INPUT_CHARSET, "UTF-8");
		params.put(ParamNames.MERCHANT_CODE, merchantCode);
		params.put(ParamNames.BANK_ACCOUNT, bankAccount);
		params.put(ParamNames.BANK_CARD_NO, bankCardNo);
		params.put(ParamNames.BANK_CODE, getBankCode());
		params.put(ParamNames.AMOUNT, amount);
		params.put(ParamNames.MERCHANT_ORDER, orderNo);
		String sign = KeyValues.create().add(ParamNames.INPUT_CHARSET, params.get(ParamNames.INPUT_CHARSET))
				.add(ParamNames.MERCHANT_CODE, params.get(ParamNames.MERCHANT_CODE))
				.add(ParamNames.BANK_ACCOUNT, params.get(ParamNames.BANK_ACCOUNT))
				.add(ParamNames.BANK_CARD_NO, params.get(ParamNames.BANK_CARD_NO))
				.add(ParamNames.BANK_CODE, params.get(ParamNames.BANK_CODE))
				.add(ParamNames.AMOUNT, params.get(ParamNames.AMOUNT))
				.add(ParamNames.MERCHANT_ORDER, params.get(ParamNames.MERCHANT_ORDER)).sign(merchantKey);
		params.put(ParamNames.SIGN, sign);
		StringBuilder sb = new StringBuilder();
		sb.append(getUrl).append("?").append(ParamNames.INPUT_CHARSET).append("={").append(ParamNames.INPUT_CHARSET)
				.append("}").append("&").append(ParamNames.MERCHANT_CODE).append("={").append(ParamNames.MERCHANT_CODE)
				.append("}").append("&").append(ParamNames.BANK_ACCOUNT).append("={").append(ParamNames.BANK_ACCOUNT)
				.append("}").append("&").append(ParamNames.BANK_CARD_NO).append("={").append(ParamNames.BANK_CARD_NO)
				.append("}").append("&").append(ParamNames.BANK_CODE).append("={").append(ParamNames.BANK_CODE)
				.append("}").append("&").append(ParamNames.AMOUNT).append("={").append(ParamNames.AMOUNT).append("}")
				.append("&").append(ParamNames.MERCHANT_ORDER).append("={").append(ParamNames.MERCHANT_ORDER)
				.append("}").append("&").append(ParamNames.SIGN).append("={").append(ParamNames.SIGN).append("}");
		String url = sb.toString();
		String result = sendGet(url, params);
		System.out.println("出款返回："+result);
		return result;
	}

	public String queryBlance() {
		Map<String, String> params = new HashMap<>();
		params.put(ParamNames.MERCHANT_CODE, merchantCode);
		params.put(ParamNames.QUERY_TIME, DateUtil.getStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		String sign = KeyValues.create().add(ParamNames.INPUT_CHARSET, params.get(ParamNames.INPUT_CHARSET))
				.add(ParamNames.MERCHANT_CODE, params.get(ParamNames.MERCHANT_CODE))
				.add(ParamNames.QUERY_TIME, params.get(ParamNames.QUERY_TIME)).sign(merchantKey);
		params.put(ParamNames.SIGN, sign);
		StringBuilder sb = new StringBuilder();
		sb.append(getBalanceUrl).append("?").append(ParamNames.MERCHANT_CODE).append("={")
				.append(ParamNames.MERCHANT_CODE).append("}").append("&").append(ParamNames.QUERY_TIME).append("={")
				.append(ParamNames.QUERY_TIME).append("}").append("&").append(ParamNames.SIGN).append("={")
				.append(ParamNames.SIGN).append("}");
		String url = sb.toString();
		String result = sendGet(url, params);
		System.out.println("查询余额返回："+result);
		return result;
	}

	public static String sendHttpsGet(String url, Map<String, String> params) {
		String result = "";
		BufferedReader in = null;
		try {
			Set<Map.Entry<String, String>> sets = params.entrySet();
			for (Map.Entry<String, String> entry : sets) {
				String k = entry.getKey();
				String v = entry.getValue();
				if (ParamNames.QUERY_TIME.equals(k)) {
					v = v.replace(" ", "%20");
				}
				url = url.replace("{" + k + "}", v);
			}
			System.out.println("请求地址:" + url);

			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };
			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			// 创建URL对象
			URL myURL = new URL(url);
			// 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
			HttpsURLConnection httpsConn = (HttpsURLConnection) myURL.openConnection();
			// 取得该连接的输入流，以读取响应内容
			in = new BufferedReader(new InputStreamReader(httpsConn.getInputStream()));
			// 读取服务器的响应内容并显示
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	public static String sendGet(String url, Map<String, String> params) {
		String result = "";
		BufferedReader in = null;
		try {
			Set<Map.Entry<String, String>> sets = params.entrySet();
			for (Map.Entry<String, String> entry : sets) {
				String k = entry.getKey();
				String v = entry.getValue();
				if (StringUtils.isNotBlank(k) && ParamNames.QUERY_TIME.equals(k)) {
					v = v.replace(" ", "%20");
				}
				url = url.replace("{" + k + "}", v);
			}
			System.out.println("request url is " + url);
			URL realUrl = new URL(url);
			URLConnection connection = realUrl.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.connect();
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getMerchantKey() {
		return merchantKey;
	}

	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getBankCode() {
		if (bankCode.indexOf("农业银行") != -1) {
			bankCode = "ABC";
		} else if (bankCode.indexOf("中国银行") != -1) {
			bankCode = "BOC";
		} else if (bankCode.indexOf("交通银行") != -1) {
			bankCode = "BOCOM";
		} else if (bankCode.indexOf("建设银行") != -1) {
			bankCode = "CCB";
		} else if (bankCode.indexOf("工商银行") != -1) {
			bankCode = "ICBC";
		} else if (bankCode.indexOf("邮政储蓄银行") != -1) {
			bankCode = "PSBC";
		} else if (bankCode.indexOf("招商银行") != -1) {
			bankCode = "CMBC";
		} else if (bankCode.indexOf("浦发银行") != -1 || bankCode.indexOf("浦东发展银行") != -1) {
			bankCode = "SPDB";
		} else if (bankCode.indexOf("光大银行") != -1) {
			bankCode = "CEBBANK";
		} else if (bankCode.indexOf("中信银行") != -1) {
			bankCode = "ECITIC";
		} else if (bankCode.indexOf("平安银行") != -1) {
			bankCode = "PINGAN";
		} else if (bankCode.indexOf("民生银行") != -1) {
			bankCode = "CMBCS";
		} else if (bankCode.indexOf("华夏银行") != -1) {
			bankCode = "HXB";
		} else if (bankCode.indexOf("广发银行") != -1 || bankCode.indexOf("广东发展银行") != -1) {
			bankCode = "CGB";
		} else if (bankCode.indexOf("兴业银行") != -1) {
			bankCode = "CIB";
		} else if (bankCode.indexOf("徽商银行") != -1) {
			bankCode = "HSB";
		} else if (bankCode.indexOf("长沙银行") != -1) {
			bankCode = "CSCB";
		} else if (bankCode.indexOf("信用社") != -1) {
			bankCode = "ZJRCC";
		}
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getGetUrl() {
		return getUrl;
	}

	public void setGetUrl(String getUrl) {
		this.getUrl = getUrl;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}
