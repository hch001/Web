package com.example.demo.other;

import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class client {
    static private int port= 8848;
    static private String host="localhost";

    public static void chat(String filmId) throws IOException {
        Socket socket = new Socket(host,port);
        socket.getOutputStream().write(filmId.getBytes());

        byte[] bytes = new byte[200];
        socket.getInputStream().read(bytes);

        String s = new String(bytes);
        String result = s.substring(0,s.indexOf("\0"));

        System.out.println(result);
        if(result.equals("未找到"))
            ;

        Pattern pattern = Pattern.compile("\\d+");
        Matcher m = pattern.matcher(result);

        while(m.find()){
            System.out.println(m.group());
        }

    }


    public static void main(String[] args) throws IOException {
//        new Thread(()->{
//            try{
//                ServerSocket serverSocket = new ServerSocket(8848);
//                Socket socket = serverSocket.accept();
//                System.out.println("收到连接请求");
//                socket.getOutputStream().write(new byte[]{2,2,2,21,1});
//                System.out.println("已应答");
//            }
//            catch ( Exception e){
//                e.printStackTrace();
//            }
//        }).start();

        String s = "附件是看/飞洒地方/飞洒d地方";
        Pattern pattern = Pattern.compile("(.*?)/");
        Matcher m = pattern.matcher(s);
        while(m.find()) {
            System.out.println(m.group(1));
        }
    }
}
