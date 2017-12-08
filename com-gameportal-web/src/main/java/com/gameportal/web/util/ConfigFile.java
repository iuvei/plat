package com.gameportal.web.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 文件读写
 * @author Administrator
 *
 */
public class ConfigFile {

	public static final String CLIENT_IP = "data/ip.txt";
	
	/**
	 * 读取txt里的单行内容
	 * @param filePath  文件路径
	 */
	public static String readTxtFile(String fileP) {
		try {
			
			String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource(""));	//项目路径
			filePath = filePath.replaceAll("file:/", "");
			filePath = filePath.replaceAll("%20", " ");
			filePath = filePath.trim() + fileP.trim();
			if(filePath.indexOf(":") != 1){
				filePath = File.separator + filePath;
			}
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { 		// 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
				new FileInputStream(file), encoding);	// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					return lineTxt;
				}
				read.close();
			}else{
				System.out.println("找不到指定的文件,查看此路径是否正确:"+filePath);
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
		}
		return "";
	}
	
	/**
	 * 文件写入
	 * @param fileP 路径
	 * @param value 值
	 * @return
	 */
	public static String writeTxtFile(String fileP,String value) {
		try {
			String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource(""));	//项目路径
			filePath = filePath.replaceAll("file:/", "");
			filePath = filePath.replaceAll("%20", " ");
			filePath = filePath.trim() + fileP.trim();
			if(filePath.indexOf(":") != 1){
				filePath = File.separator + filePath;
			}
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { 		// 判断文件是否存在
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(  
			            new FileOutputStream(file), encoding));  
			    writer.write(value);
			    writer.close();
			}
		} catch (Exception e) {
			System.out.println("写入文件内容出错");
		}
		return null;
	}
	
	public static void main(String[] args) {
		String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource(""));	//项目路径
		System.out.println(filePath);
	}
}
