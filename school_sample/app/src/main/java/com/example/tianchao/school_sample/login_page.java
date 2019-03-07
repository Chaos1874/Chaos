package com.example.tianchao.school_sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class login_page extends AppCompatActivity {
    private EditText account,password;
    private Button login;

    private final int LOGINSUCCESS=0;
    private final int LOGINNOTFOUND=1;
    private final int LOGINEXCEPT=2;

    Handler handler=new Handler(){//消息机制，用来在子线程中更新UI
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){//具体消息，具体显示
                case LOGINSUCCESS:
                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                    if(msg.obj.equals("登录成功")) {
                        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("account", account.getText().toString());
                        editor.putString("password",password.getText().toString());
                        editor.putBoolean("auto_login",true);
                        editor.commit();

                        Intent intent = new Intent(login_page.this, Loading_resource.class);
                        startActivity(intent);
                        login_page.this.finish();
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

        setContentView(R.layout.activity_login_page);

        account = findViewById(R.id.account_login);
        password = findViewById(R.id.password_login);
        login = findViewById(R.id.login_login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("list:safd");
                login();
            }
        });
    }

    public void login(){
        final String account_string = account.getText().toString().trim();
        final String password_string = password.getText().toString().trim();
        if(account_string.equals("") || password_string.equals("")){
            Toast.makeText(login_page.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
        }//进行登录操作(联网操作要添加权限)
        else {
            //联网操作要开子线程，在主线程不能更新UI
            new Thread(){
                private HttpURLConnection connection;
                @Override
                public void run() {
                    try {
                        //封装成传输数据的键值对,无论get还是post,传输中文时都要进行url编码（RULEncoder）
                        // 如果是在浏览器端的话，它会自动进行帮我们转码，不用我们进行手动设置
                        String data= "account="+ URLEncoder.encode(account_string,"utf-8")+"&password="+ URLEncoder.encode(password_string,"utf-8");
                        connection=HttpConnettionUtils.getConnection(data,"Login");
                        int code = connection.getResponseCode();
                        if(code==200){//成功
                            InputStream inputStream = connection.getInputStream();
                            String str = StreamString.Stream2String(inputStream);//写个工具类流转换成字符串
                            Message message = Message.obtain();//更新UI就要向消息机制发送消息
                            message.what=LOGINSUCCESS;//用来标志是哪个消息
                            message.obj=str;//消息主体
                            handler.sendMessage(message);
                        }
                        else {
                            Toast.makeText(login_page.this,"1",Toast.LENGTH_SHORT).show();
                            Message message = Message.obtain();
                            message.what=LOGINNOTFOUND;
                            message.obj="登录异常...请稍后再试1";
                            handler.sendMessage(message);
                        }
                    } catch (Exception e) {//会抛出很多个异常，这里抓一个大的异常
                        e.printStackTrace();
                        Log.e("exception",Log.getStackTraceString(e));
                        Message message = Message.obtain();
                        message.what=LOGINEXCEPT;
                        message.obj="服务器异常...请稍后再试2";
                        handler.sendMessage(message);
                    }
                }
            }.start();
        }
    }
}
