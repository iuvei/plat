package com.gameportal.web.mail;

import java.security.Security;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.gameportal.web.mail.model.MailSenderInfo;
@SuppressWarnings("restriction")
public class SimpleMailSender {
	
	private static final Logger logger = Logger.getLogger(SimpleMailSender.class);

	/**   
	  * 以文本格式发送邮件   
	  * @param mailInfo 待发送的邮件的信息   
	  */    
	    public boolean sendTextMail(MailSenderInfo mailInfo) throws Exception{   
	      Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
	      final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	      // 判断是否需要身份认证    
	      MyAuthenticator authenticator = null;    
	      Properties pro = mailInfo.getProperties();   
	      pro.put("mail.smtp.socketFactory.class", SSL_FACTORY);
	      if(mailInfo.getMailServerHost().indexOf("163") !=-1){
	    	  pro.put("mail.smtp.socketFactory.fallback", "true");
	      }else{
	    	  pro.put("mail.smtp.socketFactory.fallback", "false");
	      }
	      pro.put("mail.smtp.port", mailInfo.getMailServerPort());
	      pro.put("mail.smtp.socketFactory.port", mailInfo.getMailServerPort());
	      if (mailInfo.isValidate()) {    
	      // 如果需要身份认证，则创建一个密码验证器    
	        authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());    
	      }   
	      // 根据邮件会话属性和密码验证器构造一个发送邮件的session    
	      Session sendMailSession = Session.getInstance(pro,authenticator); 
	      logger.info("构造一个发送邮件的session");
	      
	      // 根据session创建一个邮件消息    
	      Message mailMessage = new MimeMessage(sendMailSession);    
	      // 创建邮件发送者地址    
	      Address from = new InternetAddress(mailInfo.getFromAddress());    
	      // 设置邮件消息的发送者    
	      mailMessage.setFrom(from);    
	      // 创建邮件的接收者地址，并设置到邮件消息中    
	      Address to = new InternetAddress(mailInfo.getToAddress());    
	      mailMessage.setRecipient(Message.RecipientType.TO,to);    
	      // 设置邮件消息的主题    
	      mailMessage.setSubject(mailInfo.getSubject());    
	      // 设置邮件消息发送的时间    
	      mailMessage.setSentDate(new Date());    
	      // 设置邮件消息的主要内容    
	      String mailContent = mailInfo.getContent();    
	      mailMessage.setText(mailContent);    
	      // 发送邮件    
	      Transport.send(mailMessage); 
	      logger.info("发送成功！");
	      return true;    
	    }
	    
	    /**   
	      * 以HTML格式发送邮件   
	      * @param mailInfo 待发送的邮件信息   
	      */    
	    public  boolean sendHtmlMail(MailSenderInfo mailInfo) throws Exception{
    	  Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
	      final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	      // 判断是否需要身份认证    
	      MyAuthenticator authenticator = null;    
	      Properties pro = mailInfo.getProperties();   
	      pro.put("mail.smtp.socketFactory.class", SSL_FACTORY);
	      pro.put("mail.smtp.socketFactory.fallback", "false");
	      pro.put("mail.smtp.port", mailInfo.getMailServerPort());
	      pro.put("mail.smtp.socketFactory.port", mailInfo.getMailServerPort());
	      //如果需要身份认证，则创建一个密码验证器     
	      if (mailInfo.isValidate()) {    
	        authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());   
	      }    
	      // 根据邮件会话属性和密码验证器构造一个发送邮件的session    
	      Session sendMailSession = Session.getInstance(pro,authenticator);
	        
	      // 根据session创建一个邮件消息    
	      Message mailMessage = new MimeMessage(sendMailSession);    
	      // 创建邮件发送者地址    
	      Address from = new InternetAddress(mailInfo.getFromAddress());    
	      // 设置邮件消息的发送者    
	      mailMessage.setFrom(from);    
	      // 创建邮件的接收者地址，并设置到邮件消息中    
	      Address to = new InternetAddress(mailInfo.getToAddress());    
	      // Message.RecipientType.TO属性表示接收者的类型为TO    
	      mailMessage.setRecipient(Message.RecipientType.TO,to);    
	      // 设置邮件消息的主题    
	      mailMessage.setSubject(mailInfo.getSubject());    
	      // 设置邮件消息发送的时间    
	      mailMessage.setSentDate(new Date());    
	      // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象    
	      Multipart mainPart = new MimeMultipart();    
	      // 创建一个包含HTML内容的MimeBodyPart    
	      BodyPart html = new MimeBodyPart();    
	      // 设置HTML内容    
	      html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");    
	      mainPart.addBodyPart(html);    
	      // 将MiniMultipart对象设置为邮件内容    
	      mailMessage.setContent(mainPart);  
	      // 发送邮件    
	      Transport.send(mailMessage);    
	      return true;    
	    }
	    
	    /*
	     * @title:标题
	     * @content:内容
	     * @type:类型,1:文本格式;2:html格式
	     * @tomail:接收的邮箱
	     */
	    public boolean sendMail(String title,String content,String type,String tomail) throws Exception{   
	        
	    	//这个类主要是设置邮件   
		     MailSenderInfo mailInfo = new MailSenderInfo();    
		     mailInfo.setMailServerHost("smtp.qq.com");    
		     mailInfo.setMailServerPort("25");    
		     mailInfo.setValidate(true);    
		     mailInfo.setUserName("itfather@1b23.com");    
		     mailInfo.setPassword("tttt");//您的邮箱密码    
		     mailInfo.setFromAddress("itfather@1b23.com");    
		     mailInfo.setToAddress(tomail);    
		     mailInfo.setSubject(title);    
		     mailInfo.setContent(content);    
		     //这个类主要来发送邮件   
		  
		     SimpleMailSender sms = new SimpleMailSender();   
		     
		     if("1".equals(type)){
		    	 return sms.sendTextMail(mailInfo);//发送文体格式    
		     }else if("2".equals(type)){
		    	 return sms.sendHtmlMail(mailInfo);//发送html格式   
		     }
		     return false;
		   }
	    
	    
	    /**
	     * @param SMTP  	邮件服务器
	     * @param PORT		端口
	     * @param EMAIL		本邮箱账号
	     * @param PAW		本邮箱密码
	     * @param toEMAIL	对方箱账号
	     * @param TITLE		标题
	     * @param CONTENT	内容
	     * @param TYPE		1：文本格式;2：HTML格式
	     */
	    public static void sendEmail(String SMTP, String PORT, String EMAIL, String PAW, String toEMAIL, String TITLE, String CONTENT, String TYPE) throws Exception{ 
	        //这个类主要是设置邮件   
		     MailSenderInfo mailInfo = new MailSenderInfo();
		     mailInfo.setMailServerHost(SMTP);    
		     mailInfo.setMailServerPort(PORT);    
		     mailInfo.setValidate(true);    
		     mailInfo.setUserName(EMAIL);    
		     mailInfo.setPassword(PAW);   
		     mailInfo.setFromAddress(EMAIL);    
		     mailInfo.setToAddress(toEMAIL);    
		     mailInfo.setSubject(TITLE);    
		     mailInfo.setContent(CONTENT);
		     //这个类主要来发送邮件   
		  
		     SimpleMailSender sms = new SimpleMailSender();
		    if("1".equals(TYPE)){
		    	sms.sendTextMail(mailInfo);
		    }else{
		    	sms.sendHtmlMail(mailInfo);
		    }
		     
	    }
	    
	    public static void main(String[] args){   
	         //这个类主要是设置邮件   
	    	//设置邮件   
		     MailSenderInfo mailInfo = new MailSenderInfo();    
		     mailInfo.setMailServerHost("smtp.qiye.163.com");   
		     mailInfo.setMailServerPort("994"); 
//		     mailInfo.setMailServerHost("smtp.gmail.com");   
//		     mailInfo.setMailServerPort("587"); 
		     mailInfo.setValidate(true);    
		     //mailInfo.setUserName("cs@188dxy.com");    
		     //mailInfo.setPassword("5Afkj988@");//您的邮箱密码   
		     mailInfo.setUserName("cs1@xoso.cc");    
		     mailInfo.setPassword("Qb517518!");//您的邮箱密码
		     //
		     mailInfo.setFromAddress("cs1@xoso.cc"); 
		     
		     mailInfo.setToAddress("3112819148@qq.com");    
		     mailInfo.setSubject("day400-新的邮箱消息");    
		     mailInfo.setContent("<div style=\"width:602px; height:250px;\"><ul style=\" padding-top:100px; list-style:none; font-family:'新宋体';\"><li>day400,欢迎来到钱宝娱乐！</li><li>点击下面的链接完成激活：</li><li><h3><a href='http://localhost:8080/mail/verify.html?1D45E9D8158E69853FA594A1F875FAAD49405680C0D50F6D22BE19F865F0D665944B9ADA33722BFAE516BA90335F902DDFB6C35D66833D8B7715273997AB0146' target='_blank' style='color:red;'>http://localhost:8080/mail/verify.html?1D45E9D8158E69853FA594A1F875FAAD49405680C0D50F6D22BE19F865F0D665944B9ADA33722BFAE516BA90335F902DDFB6C35D66833D8B7715273997AB0146</a></h3></li></ul></div>");
		     mailInfo.setEmailType("false");
		     //mailInfo.setStarttlsEnable(false);
		     //这个类主要来发送邮件   
		     SimpleMailSender sms = new SimpleMailSender();  
		     try {
				sms.sendHtmlMail(mailInfo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//发送文体格式    
		     //sms.sendHtmlMail(mailInfo);//发送html格式   
		     
		   }
}
