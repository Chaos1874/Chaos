package com.example.tianchao.school_sample;

import android.os.Handler;
import android.os.Message;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class requests {

    public static int LOGINSUCCESS=0;
    private static int LOGINNOTFOUND=1;
    private static int LOGINEXCEPT=2;

    public static String infor;



    static  Handler handler = new Handler() {//消息机制，用来在子线程中更新UI
        @Override
        public void handleMessage(Message msg) {
            infor = (String) msg.obj;
        }
    };

    public  String requestNews(){

        new Thread(){
            private HttpURLConnection connection;
            @Override
            public void run() {
                try {
                    System.out.println("444");
                    //封装成传输数据的键值对,无论get还是post,传输中文时都要进行url编码（RULEncoder）
                    // 如果是在浏览器端的话，它会自动进行帮我们转码，不用我们进行手动设置
                    String data= "information="+ URLEncoder.encode("news","utf-8");
                    connection=HttpConnettionUtils.getConnection(data,"sendNews");
                    System.out.println("555");
                    int code = connection.getResponseCode();
                    System.out.println("555");
                    if(code==200){//成功
                        InputStream inputStream = connection.getInputStream();
                        String str = StreamString.Stream2String(inputStream);//写个工具类流转换成字符串
                        Message message = Message.obtain();//更新UI就要向消息机制发送消息
                        message.what=LOGINSUCCESS;//用来标志是哪个消息
                        message.obj=str;//消息主体
                        System.out.println(str);
                        handler.sendMessage(message);
                    }
                    else {
                        Message message = Message.obtain();
                        message.what=LOGINNOTFOUND;
                        message.obj="服务器异常,请稍后再试";
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {//会抛出很多个异常，这里抓一个大的异常
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what=LOGINEXCEPT;
                    message.obj="服务器异常,请稍后再试";
                    handler.sendMessage(message);
                }
            }
        }.start();
        return infor;
    }
}
