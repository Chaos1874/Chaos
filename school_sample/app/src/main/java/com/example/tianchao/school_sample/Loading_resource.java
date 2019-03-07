package com.example.tianchao.school_sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;


public class Loading_resource extends AppCompatActivity {

    private final int LOGINSUCCESS=0;
    private final int LOGINNOTFOUND=1;
    private final int LOGINEXCEPT=2;

    private ACache aCache;


    Handler newsHandler=new Handler(){//消息机制，用来在子线程中更新UI
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){//具体消息，具体显示
                case LOGINSUCCESS:
                    if(msg.obj.equals("服务器异常,请稍后再试")) {
                        Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                    }
                    else{
                        aCache = ACache.get(getApplicationContext());
                        try {
                            aCache.put("news",new JSONArray((String) msg.obj));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit().putBoolean("have_load_news",true);
                        editor.commit();
                    }
                    break;
                case LOGINNOTFOUND:
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case LOGINEXCEPT:
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    Handler courseHandler=new Handler(){//消息机制，用来在子线程中更新UI
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){//具体消息，具体显示
                case LOGINSUCCESS:
                    if(msg.obj.equals("服务器异常,请稍后再试")) {
                        Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                    }
                    else{
                        System.out.println("hbhjbk"+msg.what);
                        aCache = ACache.get(getApplicationContext());
                        try {
                            aCache.put("course",new JSONArray((String) msg.obj));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit().putBoolean("have_load_course",true);
                        editor.commit();
                    }
                    break;
                case LOGINNOTFOUND:
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case LOGINEXCEPT:
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    Handler userHandler=new Handler(){//消息机制，用来在子线程中更新UI
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){//具体消息，具体显示
                case LOGINSUCCESS:
                    if(msg.obj.equals("服务器异常,请稍后再试")) {
                        Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                    }
                    else{
                        aCache = ACache.get(getApplicationContext());
                        try {
                            aCache.put("users",new JSONArray((String) msg.obj));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit().putBoolean("have_load_users",true);
                        editor.commit();
                    }
                    break;
                case LOGINNOTFOUND:
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case LOGINEXCEPT:
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*去除标签栏*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remove notification bar  即全屏

        setContentView(R.layout.activity_loading_resource);

         SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);

        if (!preferences.getBoolean("have_load_news", false)){
            requestNews();
        }
        if(!preferences.getBoolean("have_load_users", false)&&preferences.getBoolean("auto_login",false)){
            requestUsers();
        }
        if(!preferences.getBoolean("have_load_course", false)&&preferences.getBoolean("auto_login",false)){
            requestCourse();
        }

        if(!preferences.getBoolean("auto_login",false)) {

            Intent intent = new Intent(Loading_resource.this, login_page.class);
            startActivity(intent);
            Loading_resource.this.finish();
        }
        else {
                Intent intent = new Intent(Loading_resource.this, main_page.class);
                startActivity(intent);
                Loading_resource.this.finish();
            }
    }

    private void requestUsers() {
        new Thread(){
            private HttpURLConnection connection;
            @Override
            public void run() {
                try {
                    //封装成传输数据的键值对,无论get还是post,传输中文时都要进行url编码（RULEncoder）
                    // 如果是在浏览器端的话，它会自动进行帮我们转码，不用我们进行手动设置
                    String data= "information="+ URLEncoder.encode(getSharedPreferences("preferences", MODE_PRIVATE).getString("account","null"),"utf-8");
                    connection=HttpConnettionUtils.getConnection(data,"sendUsers");
                    int code = connection.getResponseCode();
                    if(code==200){//成功
                        InputStream inputStream = connection.getInputStream();
                        String str = StreamString.Stream2String(inputStream);//写个工具类流转换成字符串
                        Message message = Message.obtain();//更新UI就要向消息机制发送消息
                        message.what=LOGINSUCCESS;//用来标志是哪个消息
                        message.obj=str;//消息主体
                        userHandler.sendMessage(message);
                    }
                    else {
                        Message message = Message.obtain();
                        message.what=LOGINNOTFOUND;
                        message.obj="服务器异常,请稍后再试";
                        userHandler.sendMessage(message);
                    }
                } catch (Exception e) {//会抛出很多个异常，这里抓一个大的异常
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what=LOGINEXCEPT;
                    message.obj="服务器异常,请稍后再试";
                    userHandler.sendMessage(message);
                }
            }
        }.start();
    }

    private void requestCourse() {
        new Thread(){
            private HttpURLConnection connection;
            @Override
            public void run() {
                try {
                    //封装成传输数据的键值对,无论get还是post,传输中文时都要进行url编码（RULEncoder）
                    // 如果是在浏览器端的话，它会自动进行帮我们转码，不用我们进行手动设置
                    String data= "information="+ URLEncoder.encode(getSharedPreferences("preferences", MODE_PRIVATE).getString("account","null"),"utf-8");
                    connection=HttpConnettionUtils.getConnection(data,"sendCourse");
                    int code = connection.getResponseCode();
                    if(code==200){//成功
                        System.out.println(1);
                        InputStream inputStream = connection.getInputStream();
                        System.out.println(2);
                        String str = StreamString.Stream2String(inputStream);//写个工具类流转换成字符串
                        Message message = Message.obtain();//更新UI就要向消息机制发送消息
                        message.what=LOGINSUCCESS;//用来标志是哪个消息
                        message.obj=str;//消息主体
                        System.out.println(str);
                        courseHandler.sendMessage(message);
                    }
                    else {
                        Message message = Message.obtain();
                        message.what=LOGINNOTFOUND;
                        message.obj="服务器异常,请稍后再试";
                        courseHandler.sendMessage(message);
                    }
                } catch (Exception e) {//会抛出很多个异常，这里抓一个大的异常
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what=LOGINEXCEPT;
                    message.obj="服务器异常,请稍后再试";
                    courseHandler.sendMessage(message);
                }
            }
        }.start();
    }

    private void requestNews() {
        new Thread(){
            private HttpURLConnection connection;
            @Override
            public void run() {
                try {
                    //封装成传输数据的键值对,无论get还是post,传输中文时都要进行url编码（RULEncoder）
                    // 如果是在浏览器端的话，它会自动进行帮我们转码，不用我们进行手动设置
                    String data= "information="+ URLEncoder.encode("news","utf-8");
                    connection=HttpConnettionUtils.getConnection(data,"sendNews");
                    int code = connection.getResponseCode();
                    if(code==200){//成功
                        InputStream inputStream = connection.getInputStream();
                        String str = StreamString.Stream2String(inputStream);//写个工具类流转换成字符串
                        Message message = Message.obtain();//更新UI就要向消息机制发送消息
                        message.what=LOGINSUCCESS;//用来标志是哪个消息
                        message.obj=str;//消息主体
                        newsHandler.sendMessage(message);
                    }
                    else {
                        Message message = Message.obtain();
                        message.what=LOGINNOTFOUND;
                        message.obj="服务器异常,请稍后再试";
                        newsHandler.sendMessage(message);
                    }
                } catch (Exception e) {//会抛出很多个异常，这里抓一个大的异常
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what=LOGINEXCEPT;
                    message.obj="服务器异常,请稍后再试";
                    newsHandler.sendMessage(message);
                }
            }
        }.start();
    }

}
