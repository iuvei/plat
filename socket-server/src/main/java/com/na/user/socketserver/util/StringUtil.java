package com.na.user.socketserver.util;

public class StringUtil {
	
	/**
	 * 隐藏部分用户昵称
	 * @param nickName
	 * @return
	 */
	public static String hideNickName(String nickName) {
		if(nickName==null){
			return "";
		}
		if(nickName.length()>3){
			nickName = "***"+nickName.substring(nickName.length()-2, nickName.length());
		} else {
			int a = 5 - nickName.length();
			String str = "";
			for (int i = 0 ; i < a;i++) {
				str += "*";
			}
			nickName = str + nickName;
		}
		return nickName;
	}

}
