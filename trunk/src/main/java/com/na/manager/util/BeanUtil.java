package com.na.manager.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

/**
 * 实体工具类
 * 
 * @author alan
 * @date 2017年4月28日 下午12:26:53
 */
public class BeanUtil {
	
	/**
	 * 深克隆
	 * @param src
	 * @return
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T cloneTo(T src) throws RuntimeException {
		ByteArrayOutputStream memoryBuffer = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		T dist = null;
		try {
			out = new ObjectOutputStream(memoryBuffer);
			out.writeObject(src);
			out.flush();
			in = new ObjectInputStream(new ByteArrayInputStream(memoryBuffer.toByteArray()));
			dist = (T) in.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null)
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			if (in != null)
				try {
					in.close();
					in = null;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
		}
		return dist;
	}

	public static<T> T getFieldValue(Object obj,String fieldName) {
		return getFieldValue(obj.getClass(),obj,fieldName);
	}

	public static<T> T getFieldValue(Class cls,Object obj,String fieldName) {
		try {
			Field myField = cls.getDeclaredField(fieldName);
			myField.setAccessible(true);
			return (T)myField.get(obj);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}


	public static void setFieldValue(Object obj,String fieldName,Object val) {
		try {
			Field myField = obj.getClass().getDeclaredField(fieldName);
			myField.setAccessible(true);
			myField.set(obj,val);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}