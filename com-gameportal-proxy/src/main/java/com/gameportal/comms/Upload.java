package com.gameportal.comms;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("all")
public class Upload {
	 
	private static final Logger logger = LoggerFactory.getLogger(Upload.class);
	private static Upload upload = null;
	//单例模式
	public static Upload getInstance(){
		if(upload == null){
			upload = new Upload();
		}
		return upload;
	}
	
	//对文件重命名
	public static String  getMin()
	{
		Date date=new Date();
		Random r = new Random();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return (sdf.format(date)+r.nextInt(1000)).toString();
	}
	
	/**
	 * Spring MVC 多文件上传
	 * @param request
	 * @return
	 */
	public String saveUpload(HttpServletRequest request){
		String imageName = null;
		int fileMaxSize = 4;
		String path = request.getSession().getServletContext().getRealPath("/");
		//创建一个基于磁盘的文件工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(4096);//设置缓冲区大小，这里是4kb
		factory.setRepository(new File(path+"temp"));//设置缓冲区目录
		ServletFileUpload upload = new ServletFileUpload();
		upload.setSizeMax(fileMaxSize * 1000000); // 设置最大文件尺寸，这里是4MB
		String paths = path+"userfiles/product/";
		try {
			FileItemIterator items = upload.getItemIterator(request);// 得到所有的文件
			while (items.hasNext()){
				FileItemStream stream = items.next();  
                if(!stream.isFormField()){
                	String fileName = stream.getName();// 得到文件的名称  
                	imageName = getMin() + fileName.substring(fileName.lastIndexOf("."));
                    if(fileName==null || "".equals(fileName.trim())){  
                        continue;  
                    }  
                    File file = new File(paths,imageName);  
                    if(file.exists()){  
                        file.delete();  
                    }  
                    InputStream is = stream.openStream();                     
                    ByteArrayOutputStream bStream = new ByteArrayOutputStream();  
                    long size = Streams.copy(is,bStream,true);  
                    OutputStream op = new FileOutputStream(file);  
                    bStream.writeTo(op);  
                      
                }else{  
                    InputStream is = stream.openStream();  
                    InputStreamReader isr = new InputStreamReader(is,"utf-8");  
                    BufferedReader br = new BufferedReader(isr);  
                    char b[] = new char[1024];  
                   
					int len = 0;  
                    StringBuffer sb = new StringBuffer();  
                    String line = null;  
                    while((line=br.readLine())!=null){  
                        sb.append(line);  
                    } 
                    System.out.println(stream.getFieldName()+"  "+sb.toString());  
                }  
			}
		} catch (Exception e) {
			logger.error("文件上传错误："+e.getMessage());
			imageName=null;
		}
		return imageName;
	}
	
	/**
	 * 单文件上传
	 * @param io 文件流
	 * @param filename 文件名称
	 * @param firstPath 子目录例如:/upload/leave/
	 * @param rootPath 跟目录例如:/upload/
	 * @return 图片名称
	 */
	public boolean saveUpload(InputStream stream,String filename,String path,String firstPath){
		boolean result = true;
		logger.debug("文件名称："+filename);
		logger.debug("文件上传根目录："+path);
		int fileMaxSize = 4;
		//创建一个基于磁盘的文件工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(4096);//设置缓冲区大小，这里是4kb
		factory.setRepository(new File(path+WebConst.ROOT_PATH+WebConst.TEMP_FIRST_PATH));//设置缓冲区目录
		ServletFileUpload upload = new ServletFileUpload();
		upload.setSizeMax(fileMaxSize * 1000000); // 设置最大文件尺寸，这里是4MB
		String paths = path+WebConst.ROOT_PATH+firstPath;
		logger.debug("文件上传子目录："+paths);
		//检查文件是否存在，存在则删除
		File file = new File(paths,filename);  
        if(file.exists()){  
            file.delete();
        }
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        try {
        	long size = Streams.copy(stream,bStream,true);
			logger.debug("文件大小："+size);
			logger.debug("限制文件大小："+upload.getSizeMax());
			OutputStream op = new FileOutputStream(file);  
		    bStream.writeTo(op);
		    bStream.flush();
			bStream.close();
			op.flush();
			op.close();
		} catch (IOException e) {
			logger.error("文件上传IO异常。"+e.getMessage());
			result = false;
		}
		return result;
	}
	
	 /**
	  * 判断文件是否存在
	  * @param path 文件夹路径
	  */
	 public static boolean isExist(String path) {
		 File file = new File(path);
		 //判断文件夹是否存在,如果不存在则创建文件夹
		 return file.exists();
	 }
	 
	 public static void main(String[] args) {
		 String mailPath = "E:/m/a";
		 File[] dirs=new File(mailPath).listFiles();
		 for(int i=0;i<dirs.length;i++){
			 try {
				System.out.println(dirs[i].createNewFile());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
	 }
	
	//删除图片及文件
	public static boolean deleteImage(String filePath){
		  filePath = filePath.toString();   
		  File   myDelFile=new  File(filePath);   
		 if(myDelFile.exists())
		     myDelFile.delete();  //根据文件路径删除文件
	      return  true;
	}
     
   //-----文件类型判断------
     public static boolean isGif(String file) {
           if (file.toLowerCase().endsWith(".gif")) {
               return true;
           } else {
               return false;
           }
     }

       public static boolean isJpg(String file) {
           if (file.toLowerCase().endsWith(".jpg")) {
               return true;
           } else {
               return false;
           }
       }

       public static boolean isPng(String file) {
           if (file.toLowerCase().endsWith(".png")) {
               return true;
           } else {
               return false;
           }
       }
       


}
