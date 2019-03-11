package com.example.tianchao.school_sample.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tianchao.school_sample.R;

public class Course extends AppCompatActivity {

    private TextView tittle;
    private Button sign;
    private Button notice;
    private Button talk;
    private ImageButton back;
    private ListView listView;

    private String tittles;
    private String num;
    private  String teacher;
    private  String teachername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*去除标签栏*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remove notification bar  即全屏

        setContentView(R.layout.activity_course);

        tittle = findViewById(R.id.tittle_accourse);
        sign = findViewById(R.id.sign_accourse);
        notice = findViewById(R.id.notice_accourse);
        talk = findViewById(R.id.talk_accourse);
        back = findViewById(R.id.back_accourse);
        listView = findViewById(R.id.list_accourse);


        init();
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
       tittles = bundle.getString("coursename");
       num = bundle.getString("num");
       teacher = bundle.getString("teacher");
       teachername = bundle.getString("teacher_name");

       tittle.setText(tittles);
    }
}
