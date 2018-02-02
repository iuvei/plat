package com.na.test.batchbet.user;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import sun.net.www.http.HttpClient;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/20 0020.
 */
public class Test {
    private static final String url = "http://192.168.0.220:8080/na-manage/";
    //代理用户
    private static final String PROXY_USER = "4";
    //会员
    private static final String USER = "8";

    private String token = "f5S0wwSLPzmhejOV7cqFtBRoCUoTCkvracWtVXJtgsSv5AKO6PgOXIAWws2j3PTY";

    public void login(String username,String pwd,String code){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try{
           HttpPost post = new HttpPost(url+"login");
           JSONObject para = new JSONObject();
           para.put("username",username);
           para.put("password",pwd);
           para.put("captcha",code);
           post.setHeader("Content-type", "application/json");
           post.setEntity(new StringEntity(para.toJSONString()));
           CloseableHttpResponse response = httpclient.execute(post);
           String body = EntityUtils.toString(response.getEntity());
           System.out.println(response.getStatusLine());
            System.out.println(body);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getverifCode(String user){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try{
            HttpGet get = new HttpGet(url+"codeimg?username="+user);
            CloseableHttpResponse response = httpclient.execute(get);
            if(response.getStatusLine().getStatusCode()==200) {
                byte[] data = EntityUtils.toByteArray(response.getEntity());
                FileOutputStream os = new FileOutputStream("d:/codeimg.jpg");
                os.write(data);
            }else {
                System.out.println(response.getStatusLine());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Test test = new Test();
//        test.getverifCode("admin");
//        test.login("admin","123456","5740");
//        for (int i=1;i<=20;i++) {
//            test.createUser("test"+i, "123456", 0,1,PROXY_USER);
//        }

        for (int i=1;i<=50;i++) {
            test.createUser("super"+i, "123456", 10000,9,USER);
        }
    }



    public void createUser(String username,String pwd,long balance,long parentId,String levelId){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try{
            HttpPost post = new HttpPost(url+"accountManager/create");
            JSONObject para = new JSONObject();
            para.put("sid",parentId);
            para.put("levelId",levelId);
            para.put("loginName",username);
            para.put("nickName",username);
            para.put("password",pwd);
            para.put("balance",balance);
            para.put("chips","1,2,3");
            para.put("sPercentage","1");
            para.put("maxLimit","0");
            post.setHeader("Content-type", "application/json");
            post.setHeader("Authorization", token);
            post.setEntity(new StringEntity(para.toJSONString()));
            CloseableHttpResponse response = httpclient.execute(post);
            String body = EntityUtils.toString(response.getEntity());
            System.out.println(response.getStatusLine());
            System.out.println(body);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
