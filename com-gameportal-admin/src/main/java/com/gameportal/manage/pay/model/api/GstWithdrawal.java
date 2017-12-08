package com.gameportal.manage.pay.model.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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
import com.gameportal.manage.util.HttpUtil;
import com.gameportal.manage.util.RandomUtil;

public class GstWithdrawal {
	private String merchantCode = "1458";// 商户号
	private String merchantKey = "7faaa1d8d00caefd131e64a12f483127";// 商户KEY
	private String bankAccount = "";// 银行户名
	private String bankCardNo = "";// 银行账号
	private String bankCode = "ICBC";// 银行编码
	private String amount = "0.01";// 金额
	private String orderNo;
	private String getUrl = "http://df.gstpay.vip/gstpay/gateway/dfm.html";
	private String getBalanceUrl = "http://www.gstpay.vip/login/gateway/queryMoney.html";

	public static void main(String[] args) {
		System.out.println(new GstWithdrawal().queryBlance());
		//input_charset=UTF-8&merchant_code=1458&account_name=郭嘉&account_number=6222020502014253897&bank_name=ICBC&amount=100.00
		//&transid=2017053022593802&currentDate=2017-05-30 23:03:23&bitch_no=45t22knc&sign=3d2fd87d97ad2127b41208fda6b737c5
//		Map<String, String> params = new HashMap<>();
//		params.put("input_charset", "UTF-8");
//		params.put("merchant_code", "1458");
//		params.put("account_name", "郭嘉");
//		params.put("account_number", "6222020502014253897");
//		params.put("bank_name", "ICBC");
//		params.put("amount", "100.00");
//		params.put("transid", "2017053022593802");
//		params.put("bitch_no","45t22knc");
//		params.put("currentDate","2017-05-30 23:03:23");
//		params.put("sign","3d2fd87d97ad2127b41208fda6b737c5");
		//System.out.println(HttpUtil.doPost("http://df.keise990.top/gstpay/gateway/dfm.html", params));
		
//		System.out.println(sendPost("http://df.keise990.top/gstpay/gateway/dfm.html", "input_charset=UTF-8&merchant_code=1458&account_name=欧权智&account_number=622848 0478713307872&bank_name=ABC&amount=100.00&transid=2017053023474400&currentDate=2017-05-30 23:48:11&bitch_no=2iz26brh&sign=24993094de453a4059690840c5ebf55b"));
		
	}

	public String remit() {
		Map<String, String> params = new HashMap<>();
		params.put("input_charset", "UTF-8");
		params.put("merchant_code", merchantCode);
		try {
			params.put("account_name", URLEncoder.encode(bankAccount,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		params.put("account_number", bankCardNo);
		params.put("bank_name", getBankCode());
		params.put("amount", amount);
		params.put("transid", orderNo);
		params.put("bitch_no",new RandomUtil().getRandomCode(8));
		params.put("currentDate",DateUtil.getStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		
		String sign ="account_name="+bankAccount+"&account_number="+bankCardNo
				+ "&amount="+amount+"&bank_name="+getBankCode()+"&bitch_no="+params.get("bitch_no")
				+ "&currentDate="+params.get("currentDate")+"&input_charset=UTF-8"
				+ "&merchant_code="+merchantCode+"&transid="+orderNo+"&key="+merchantKey;
		System.out.println("参数："+sign);
		sign = MD5Encoder.encode(sign);
		System.out.println("加密1："+sign);
		sign = MD5Encoder.encode(sign.toUpperCase());
		System.out.println("加密2："+sign);
		sign = MD5Encoder.encode(sign);
		System.out.println("加密3："+sign);
		params.put("sign", sign);
		String result = HttpUtil.doPost(getUrl, params);
		System.out.println("出款返回："+result);
		return result;
	}

	public String queryBlance() {
		Map<String, String> params = new HashMap<>();
		params.put("input_charset", "UTF-8");
		params.put("merchant_code", merchantCode);
		params.put("query_time", DateUtil.getStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		String sign = KeyValues.create().add("input_charset","UTF-8")
				.add("merchant_code", params.get("merchant_code"))
				.add("query_time", params.get("query_time")).sign(merchantKey);
		params.put("sign", sign);
		StringBuilder sb = new StringBuilder();
		sb.append(getBalanceUrl).append("?").append(ParamNames.MERCHANT_CODE).append("={")
				.append(ParamNames.MERCHANT_CODE).append("}").append("&").append(ParamNames.INPUT_CHARSET).append("={")
				.append(ParamNames.INPUT_CHARSET).append("}").append("&").append(ParamNames.QUERY_TIME).append("={")
				.append(ParamNames.QUERY_TIME).append("}").append("&").append(ParamNames.SIGN).append("={")
				.append(ParamNames.SIGN).append("}");
		String url = sb.toString();
		String result = sendGet(url, params);
		System.out.println("查询余额返回："+result);
		return result;
	}
	
	 /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
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
		} else if (bankCode.indexOf("北京银行") != -1) {
			bankCode = "BCCB";
		} else if (bankCode.indexOf("上海银行") != -1) {
			bankCode = "BOS";
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
