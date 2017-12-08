package com.gameportal.pay.util;

import java.io.Writer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 
 * @Description:xstream工具类。
 * @Author:sum
 * @Since:2015年7月17日
 */
@SuppressWarnings("all")
public class XstreamUtil {
	private static XStream xstream = null;
	private static String PREFIX_CDATA = "<![CDATA[";
	private static String SUFFIX_CDATA = "]]>";

	/**
	 * 获取xstream对象。
	 * 
	 * @return 返回xstream对象。
	 */
	public static XStream getXstream() {
		if (xstream == null) {
			xstream = new XStream(new DomDriver());
		}
		return xstream;
	}

	/**
	 * xml转对象。
	 * 
	 * @param xmlStr
	 * @param cls
	 * @return 返回泛型对象。
	 */
	public static <T> T toBean(String xmlStr, Class<T> cls) {
		getXstream();
		xstream.processAnnotations(cls);
		return (T) xstream.fromXML(xmlStr);
	}

	/**
	 * 对象转xml字符串。
	 * 
	 * @param obj
	 * @return 返回字符串。
	 */
	public static String toXml(Object obj) {
		getXstream();
		xstream.processAnnotations(obj.getClass());
		return xstream.toXML(obj);
	}

	/**
	 * 全部转化
	 */
	public static XStream initXStream() {
		return new XStream(new DomDriver()  {
			@Override
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					protected void writeText(QuickWriter writer, String text) {
						// if (text.startsWith(PREFIX_CDATA) &&
						// text.endsWith(SUFFIX_CDATA)) {
						
						writer.write(PREFIX_CDATA + text + SUFFIX_CDATA);
						// } else {
						// super.writeText(writer, text);
						// }
					}
				};
			}
		});
	}

	/**
	 * 初始化XStream可支持某一字段可以加入CDATA标签,如果需要某一字段使用原文,就需要在String类型的text的头加上
	 * "<![CDATA["和结尾处加上"]]>"标签， 以供XStream输出时进行识别
	 * 
	 * @param isAddCDATA
	 *            是否支持CDATA标签
	 */
	public static XStream initXStream(boolean isAddCDATA,final Object obj) {
		if (isAddCDATA) {
			xstream = new XStream(new DomDriver() {
				@Override
				public HierarchicalStreamWriter createWriter(Writer out) {
					xstream.processAnnotations(obj.getClass());
					return new PrettyPrintWriter(out) {
						protected void writeText(QuickWriter writer, String text) {
							if (text.startsWith(PREFIX_CDATA) && text.endsWith(SUFFIX_CDATA)) {
								writer.write(text);
							} else {
								super.writeText(writer, text);
							}
						}
					};
				}
			});
		} else {
			xstream = new XStream();
			xstream.processAnnotations(obj.getClass());
		}
		return xstream;
	}
}
