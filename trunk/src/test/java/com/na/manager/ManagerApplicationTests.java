package com.na.manager;

import java.util.Date;

import com.na.manager.mail.MailInfo;
import com.na.manager.mail.SimpleMail;
import com.na.manager.util.DateUtil;


public class ManagerApplicationTests {

	public static void main(String[] args) {  
        MailInfo mailInfo = new MailInfo();  
        mailInfo.setMailServerHost("smtp.126.com");  
        mailInfo.setMailServerPort("25");  
        mailInfo.setValidate(true);  
        mailInfo.setUsername("niudear@126.com");  
        mailInfo.setPassword("testniuli1");// 您的邮箱密码  
        mailInfo.setFromAddress("niudear@126.com");  
        mailInfo.setToAddress("andy@na77.com");  
        mailInfo.setSubject(DateUtil.string(new Date())+"~~~设置邮箱标题~~~");  
        
        //附件  
        //String[] attachFileNames={"d:/Sunset.jpg"};  
        //mailInfo.setAttachFileNames(attachFileNames);  
          
        // 这个类主要来发送邮件  
        //mailInfo.setContent("设置邮箱内容");  
        //SimpleMail.sendTextMail(mailInfo);// 发送文体格式  
        StringBuffer demo = new StringBuffer();  
        demo.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">")  
        .append("<html>")  
        .append("<head>")  
        .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")  
        .append("<title>测试邮件</title>")  
        .append("<style type=\"text/css\">")  
        .append(".test{font-family:\"Microsoft Yahei\";font-size: 18px;color: red;}")  
        .append("</style>")  
        .append("</head>")  
        .append("<body>")  
        .append("<span class=\"test\">大家好，这里是测试Demo</span>")  
        .append("</body>")  
        .append("</html>");  
        mailInfo.setContent(demo.toString()+new Date().getTime());  
        SimpleMail.sendHtmlMail(mailInfo);// 发送html格式  
    }  

}
