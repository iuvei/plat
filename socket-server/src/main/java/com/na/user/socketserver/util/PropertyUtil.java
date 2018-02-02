package com.na.user.socketserver.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyUtil {
	
    private static final Logger log = LoggerFactory.getLogger(PropertyUtil.class);
    
    public static Properties loadProps(String path){
    	Properties props = new Properties();
        InputStream in = null;
        try {
//　　　　　　　<!--第一种，通过类加载器进行获取properties文件流-->
            in = PropertyUtil.class.getClassLoader().getResourceAsStream(path);
//　　　　　　  <!--第二种，通过类进行获取properties文件流-->
            //in = PropertyUtil.class.getResourceAsStream("/jdbc.properties");
            props.load(in);
        } catch (FileNotFoundException e) {
            log.error("jdbc.properties文件未找到");
        } catch (IOException e) {
        	log.error("出现IOException");
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
            	log.error("jdbc.properties文件流关闭出现异常");
            }
        }
		return props;
    }

}
