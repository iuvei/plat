package com.gameportal.manage.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 
 *@Description:xstream工具类。
 *@Author:sum
 *@Since:2015年7月17日
 */
@SuppressWarnings("all")
public class XstreamUtil {
	private static XStream xstream = null;
    
    /**
     * 获取xstream对象。
     * @return 返回xstream对象。
     */
    public static XStream getXstream(){
    	if(xstream==null){
    		xstream = new XStream(new DomDriver());
    	}
    	return xstream;
    }
    
    /**
     * xml转对象。 
     * @param xmlStr
     * @param cls
     * @return 返回泛型对象。
     */
    public static <T> T toBean(String xmlStr,Class<T> cls){
    	getXstream();
    	xstream.processAnnotations(cls);
    	return (T) xstream.fromXML(xmlStr);
    }
    
    /**
     * 对象转xml字符串。 
     * @param obj
     * @return 返回字符串。
     */
    public static String toXml(Object obj){
    	getXstream();
    	xstream.processAnnotations(obj.getClass());
    	return xstream.toXML(obj);
    }
}
