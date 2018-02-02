package com.na.manager.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
* @author Andy
* @version 创建时间：2017年9月6日 下午6:27:02
*/
public class MyAuthenticator extends Authenticator {  
    private String username = null;  
    private String password = null;  
  
    public MyAuthenticator() {  
    };  
  
    public MyAuthenticator(String username, String password) {  
        this.username = username;  
        this.password = password;  
    }  
  
    protected PasswordAuthentication getPasswordAuthentication() {  
        return new PasswordAuthentication(username, password);  
    }  

}
