package com.gameportal.comms;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理校验
 * @author brooke
 *
 */
public class StringUtils extends org.apache.commons.lang.StringUtils{
	
	final static int BUFFER_SIZE = 4096;  

	/**
	 * 防SQL注入攻击关键字符
	 */
	private static final String[] CHAR_SQL = {"and"
	    ,"exec"
	    ,"insert"
	    ,"select"
	    ,"delete"
	    ,"update"
	    ,"count"
	    ,"or"
	    //,"*"
	    ,"%"
	    ,":"
	    ,"\'"
	    ,"\""
	    ,"chr"
	    ,"mid"
	    ,"master"
	    ,"truncate"
	    ,"char"
	    ,"declare"
	    ,"SiteName"
	    ,"net user"
	    ,"xp_cmdshell"
	    ,"/add"
	    ,"exec master.dbo.xp_cmdshell"
	    ,"net localgroup administrators"};
	
	/**
	 * 过滤字符串
	 * @param value
	 * @return
	 */
	public static String getStringReplace(String value){
		value = value.replace(";", "");
		value = value.replace("&", "&amp;");
		value = value.replace("<", "&lt;");
		value = value.replace(">", "&gt;");
		value = value.replace("\"", "&quot;");
		value = value.replace(" ", "&nbsp;");
		value = value.replace("\\", "");
		value = value.replace("'", "");
		value = value.replace("(", "");
		value = value.replace(")", "");
		value = value.replace("+", "");
		value = value.replace("/", "");
		value = value.replace("=", "");
		return value.trim();
	}
	
	/**
	 * 防SQL注入攻击
	 * @param value 参数
	 * @return true:可以被注入 false:不可被注入
	 */
	public static boolean isSQL(String value){
		//构造正则表达式
		String matc_regex = ".*(";
		for(int i=0;i<CHAR_SQL.length - 1;i++){
			matc_regex += CHAR_SQL[i] + "|";
		}
		matc_regex += CHAR_SQL[CHAR_SQL.length - 1] + ").*";
		return (value.matches(matc_regex));
	}
	
	/**
	 * 过滤前台提交参数的特殊字符,防浏览器注入
	 * @param str
	 * @return
	 */
	 public static String StringFilter(String str){     
		 // 只允许字母和数字       
		 // String   regEx  =  "[^a-zA-Z0-9]";                     
		 // 清除掉所有特殊字符  
		 String regEx="[`~!@$%^&*()+=|{}''<>?~！@￥%……&*（）——+|{}【】‘；：”“’。，、？]";  
		 Pattern   p   =   Pattern.compile(regEx);     
		 Matcher   m   =   p.matcher(str);     
		 return   m.replaceAll("").trim();     
	 }
	 
	 /**
	 * 解析URL值
	 * @param params 
	 * @param url http://www.88888.com/{path}?p={p}
	 * @return http://www.88888.com/3692581?p=999999
	 */
	public static String resolveUrl(String[] params,String url){
		String tmp = url;
		Pattern p = Pattern.compile("(\\{[^\\}]*\\})");
		Matcher m = p.matcher(url);
		
		List<String> result=new ArrayList<String>();
		while(m.find()){
			result.add(m.group());
		}
		int i=0;
		for(String s1:result){
			tmp = tmp.replace(s1, params[i] == null ? "" : params[i]);
			i++;
			if(i==params.length){
				break;
			}
		}
		return tmp;
	}
	
	/**
	 * 返回错误信息字符串
	 * @param ex
	 * @return 错误信息字符串
	 */
	public static String getExceptionMessage(Exception ex){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		return sw.toString();
	}
	
	/**
	 * 根据游戏类型处理游戏奖期
	 * @param value
	 * @param type
	 * @return
	 */
	public static String changePeriod(String value,String type){
		String today = value.substring(0, 8);
		if(type.equals(GameConst.CQ_SSC)){
			today = DateUtil.format(new Date(), DateUtil.DATE_PATTERN_D2);
			char[] valueSub = value.substring(value.length()-3, value.length()).toCharArray();
			if(valueSub[0] == '0'){
				today += "-"+valueSub[1]+valueSub[2];
			}else{
				today += "-"+valueSub[0]+valueSub[1]+valueSub[2];
			}
		}else if(type.equals(GameConst.JX_SSC)){
			char[] valueSub = value.substring(value.length()-3, value.length()).toCharArray();
			if(valueSub[0] == '0'){
				today += "-"+valueSub[1]+valueSub[2];
			}else{
				today += "-"+valueSub[0]+valueSub[1]+valueSub[2];
			}
		}else if(type.equals(GameConst.XJ_SSC)){
			char[] valueSub = value.substring(value.length()-2, value.length()).toCharArray();
				today += "-"+valueSub[0]+valueSub[1];
		}
		return today;
	}
	
	/**
	 * 获取下期开奖数据
	 * @param value 本期奖期
	 * @param type
	 * @return 0：本网奖期，1：外网奖期
	 */
	public static String[] getPeriod(String value,String type){
		String[] param = value.split("-");//将数据转换20150417-12
		int param1 = Integer.valueOf(param[1]);
		param1++;
		String[] result = new String[2];
		if(param1 < 10){
			result[0] = param[0]+"-0"+param1;
		}else{
			result[0] = param[0]+"-"+param1;
		}
		if(type.equals(GameConst.CQ_SSC)){
			String str = param[0].substring(2, param[0].length());
			if(param1<10){
				result[1] = str + "00"+param1;
			}else if(param1 >= 10 && param1 < 100){
				result[1] = str + "0"+param1;
			}
		}
		if(type.equals(GameConst.JX_SSC)){
			if(param1<10){
				result[1] = param[0] + "00"+param1;
			}else if(param1 >= 10 && param1 < 100){
				result[1] = param[0] + "0"+param1;
			}
		}
		if(type.equals(GameConst.XJ_SSC)){
			if(param1<10){
				result[1] = param[0] + "0"+param1;
			}else{
				result[1] = param[0] + param1;
			}
		}
		return result;
	}
	
	/**
	 * 检测是否为十六进制非空字符串
	 * 
	 * @param value 待检测的字符串
	 * @return 检测结果
	 */
	public static boolean isHex( String value ) {
		return value.matches("^[0-9A-Fa-f]+$"); 
	}
	
	/**
	 * 检测是否为整形数字
	 * true为是false为不是
	 * @param value 待检测的字符串
	 * @return 检测结果
	 */
	public static boolean isInteger( String value) {
		try {
			Integer.valueOf(value);
			return true;
		} catch (Exception ex) {
			return false;
		}		
	}
	
	/**
	 * 验证是否为浮点型数字
	 * @param value
	 * @return
	 */
	public static boolean isDouble(String value){
		try {
			Double.valueOf(value);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	/**
	 * 检测是否为非负整数
	 * 是为false 不是为true
	 * @param value 待检测的字符串
	 * @return 检测结果
	 */
	public static boolean isNonnegative( String value ) {
		try {
			if (Integer.valueOf(value) < 0 ) {
				return false;
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	/**
     * 将字符流转换为字符串
     *
     * @param is
     * @return
     */
    public static String getStrByStream(InputStream is) {
        String encoding = "UTF-8";
        StringBuffer str = new StringBuffer();
        try {
            BufferedInputStream in = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int i = 0;
            do {
                i = in.read(buffer);
                if (i > 0)
                    str.append(new String(buffer, 0, i, encoding));
            } while (i == 1024);
            return str.toString();
        } catch (Exception e) {
            throw new RuntimeException("字符串转换错误", e);
        }
    }
	
	public static String getUUID(){
		return UUIDHexGenerator.getUUID();
	}
	public static void main(String[] args) {
		System.out.println(changePeriod("20150420043",GameConst.JX_SSC));
		
	}
}
