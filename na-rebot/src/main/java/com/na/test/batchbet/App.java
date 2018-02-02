package com.na.test.batchbet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by sunny on 2017/5/10 0010.
 */
public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
//        String url = "http://52.221.4.3:18080";
        String url = "http://192.168.0.28:18080";
        String userPrefix = "tt" ;
        int start = 1;
        int end = 1000;
        String pwd = "123456";
        int betTimeNum = 10;
        if(args.length==1){
            switch (args[0]){
                case "help":{
                    System.out.println("url userPrefix start end pwd betTimeNum");
                    System.out.println("http://192.168.0.28:18080 tt 1 20 10");
                    return;
                }
                case "?":{
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("服务器地址:");
                    url = scanner.next();
                    System.out.println("用户前缀:");
                    userPrefix = scanner.next();
                    System.out.println("start:");
                    start = Integer.valueOf(scanner.next());
                    System.out.println("end:");
                    end = Integer.valueOf(scanner.next());
                    System.out.println("密码:");
                    pwd = scanner.next();
                    System.out.println("每局下单次数:");
                    betTimeNum = Integer.valueOf(scanner.next());
                }
            }
        }else if(args.length==6){
            url = args[0];
            userPrefix = args[1];
            start = Integer.valueOf(args[2]);
            end = Integer.valueOf(args[3]);
            pwd = args[4];
            betTimeNum = Integer.valueOf(args[5]);
        }

        final String tempUrl = url;

        List<User> users = new ArrayList<>();
//        users.add(new User("li001","123456"));
        if(start==0 && end==0){
            users.add(new User(userPrefix,"123456"));
        }else {
            for (int i = start; i <= end; i++) {
                users.add(new User(userPrefix + i, pwd));
            }
        }

//        ExecutorService service = Executors.newFixedThreadPool(1);
        try {
            for (User user : users) {
//                service.submit(new Runnable() {
//                    @Override
//                    public void run() {
                        Client client = new Client(tempUrl, user.user, user.pwd,betTimeNum);
                        client.start();
//                    }
//                });
            }
            logger.info("自动投注机器人投注中……");
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    public static class User{
        public User(String user, String pwd) {
            this.user = user;
            this.pwd = pwd;
        }

        public String user;
        public String pwd;
    }

}


