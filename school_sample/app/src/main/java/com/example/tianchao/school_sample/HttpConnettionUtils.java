package com.example.tianchao.school_sample;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpConnettionUtils {//传输信息类（以流的形式传输）
    public  static HttpURLConnection getConnection(String data,String nets) throws Exception{
        URL url = new URL(CONSTANT.HOST+"/"+nets);
        HttpURLConnection connection  = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");//设置post请求
        connection.setReadTimeout(5000);//设置5s的响应时间
        connection.setDoOutput(true);//允许输出
        connection.setDoInput(true);//允许输入
        //设置请求头，以键值对的方式传输（以下这两点在GET请求中不用设置）
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded ");
        connection.setRequestProperty("Content-Length",data.length()+"");//设置请求体的长度
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(data.getBytes());//进行传输操作
        //判断服务端返回的响应码，这里是http协议的内容
        return connection;
    }
    public static HttpURLConnection getpicConnection(String str) throws Exception {
        URL url = new URL(str);
        HttpURLConnection connection  = (HttpURLConnection)url.openConnection();
        return connection;
    }
}
