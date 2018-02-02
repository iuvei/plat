package com.na.test.batchbet;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Administrator on 2017/5/13 0013.
 */
public class Test {
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(10999);
        new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        Socket socket = serverSocket.accept();
                        new DataThread(socket).start();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public static class DataThread extends Thread{
        private Socket client;
        private Scanner scanner;
        private PrintWriter pw;
        private boolean isExit = true;

        public DataThread(Socket client) throws Exception{
            this.client = client;
            scanner = new Scanner(client.getInputStream());
            pw = new PrintWriter(client.getOutputStream());
        }

        @Override
        public void run() {
            while (isExit){
                try {
                    String cmd = scanner.nextLine();
                    switch (cmd){
                        case "exit":{
                            pw.println("bye..");
                            pw.flush();
                            client.close();
                            isExit = false;
                            break;
                        }
                        default:{
                            pw.println(cmd+"11111");
                            pw.flush();
                        }
                    }
//                    pw.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
