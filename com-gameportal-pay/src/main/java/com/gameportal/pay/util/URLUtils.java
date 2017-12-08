package com.gameportal.pay.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thinkpad on 2015/3/28.
 */
public class URLUtils {
    public static String encode(String str, String charset)
    {
        try {
            return URLEncoder.encode(str, charset);
        } catch (Exception e) {
            System.out.println(e);
            return str;
        }
    }

    public static String decode(String str, String charset)
    {
        try {
            return URLDecoder.decode(str, charset);
        } catch (Exception e) {
            System.out.println(e);
            return str;
        }
    }

    public static void appendParam(StringBuilder sb, String name, String val)
    {
        appendParam(sb, name, val, true);
    }

    public static void appendParam(StringBuilder sb, String name, String val, String charset)
    {
        appendParam(sb, name, val, true, charset);
    }

    public static void appendParam(StringBuilder sb, String name, String val, boolean and)
    {
        appendParam(sb, name, val, and, null);
    }

    public static void appendParam(StringBuilder sb, String name, String val, boolean and, String charset)
    {
        if (and)
            sb.append("&");
        else
            sb.append("?");
        sb.append(name);
        sb.append("=");
        if (val == null)
            val = "";
        if (charset == null || "".equals(charset))
            sb.append(val);
        else
            sb.append(encode(val, charset));
    }
    
    public static Map<String, String> getMap(String queryStr){
    	Map<String, String>  map = new HashMap<String, String>();
    	String[] strs=queryStr.split("&");
    	String[] s;
    	for(String str:strs){
    		s = str.split("=");
    		if(s.length<2){
    		   map.put(s[0],"");
    		}else{
    		   map.put(s[0], s[1]);
    		}
    	}
    	return map;
    }
    
    public static void main(String[] args) {
		String str="orderid=2017051422271132&result=1&fee=1000&paytype=31&tradetime=1494772031&cpparam=&sign=cc5089f17b4df597e764af9ad05baf44";
		System.out.println(getMap(str));
	}
}
