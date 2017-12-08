package com.gameportal.manage.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: ContrastObjects
 * @Description: TODO(判断对象属性是否可以替换)
 * @author shejia@gz-mstc.com
 * @date 2014-5-1 下午3:51:35
 */
@SuppressWarnings("rawtypes")
public class ContrastObjects {

	public ContrastObjects() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @Title: replaceTheFront @Description: TODO(相同类把第二个对象属性有值的替换到第一个对象) @param obj1 @param obj2 @return Object
	 * 返回类型 @throws
	 */
	public static Object replaceTheFront(Object obj1, Object obj2) throws Exception {
		Class obj1Class = Class.forName(obj1.toString().split("@")[0]);// 加载类
		Class obj2Class = Class.forName(obj2.toString().split("@")[0]);// 加载类
		Field[] fields = obj2Class.getDeclaredFields();
		for (Field field : fields) {
			if (!(field.getName().indexOf("_") > -1)) {
				field.setAccessible(true);
				@SuppressWarnings("unchecked")
				Method metd = obj2Class.getMethod("get" + change(field.getName()), null);
				Object obj2Value = metd.invoke(obj2, null);
				if (StringUtils.isNotBlank(ObjectUtils.toString(obj2Value))) {
					Method metds = obj1Class.getDeclaredMethod("set" + change(field.getName()), field.getType());
					metds.invoke(obj1, obj2Value);
				}
			}

		}
		return obj1;
	}

	public static boolean isCapital(String str2) {
		Pattern pattern2 = Pattern.compile("[A-Z]*");
		Matcher matcher2 = pattern2.matcher(str2);
		return matcher2.matches() ? true : false;
	}

	public static String change(String src) {
		if (src != null) {
			StringBuffer sb = new StringBuffer(src);
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
			return sb.toString();
		} else {
			return null;
		}
	}

}
