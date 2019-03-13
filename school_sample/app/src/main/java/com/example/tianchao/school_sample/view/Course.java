package com.example.tianchao.school_sample.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.tianchao.school_sample.R;
import com.example.tianchao.school_sample.Tools.ACache;
import com.example.tianchao.school_sample.Tools.CONSTANT;
import com.example.tianchao.school_sample.Tools.ImageDownLoader;
import com.example.tianchao.school_sample.Tools.initjson;
import com.example.tianchao.school_sample.jsonObject.UserObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Course extends AppCompatActivity {


    private ImageDownLoader imageDownLoader;
    private initjson initjson;
    private UserObject userObject;

    private TextView tittle;
    private Button sign;
    private Button notice;
    private Button talk;
    private ImageView back;

    private String tittles;
    private String num;
    private String teacher;
    private String teachername;

    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private ArrayList arrayList = new ArrayList();


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

        Bundle bundle = getIntent().getExtras();
        tittles = bundle.getString("coursename");
        num = bundle.getString("num");
        teacher = bundle.getString("teacher");
        teachername = bundle.getString("teacher_name");

        tittle.setText(tittles);

        initjson = new initjson(getWindow().getDecorView());
        imageDownLoader = new ImageDownLoader(getWindow().getDecorView().getContext());


        initjson();
        initView();
        addAdapter();
    }

    private void initjson() {
        try {
            userObject = initjson.initUserJson(ACache.get(getApplicationContext()).getAsJSONArray("users"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void initView() {
        for (int i = 0; i < userObject.getName().length; i++) {
            HashMap hashMap = new HashMap();
            hashMap.put("icon_user", (Bitmap) userObject.getPicture().get(i));
            hashMap.put("name_user", userObject.getName()[i]);
            arrayList.add(hashMap);
        }
    }

    private void addAdapter() {
        simpleAdapter = new SimpleAdapter(this,
                arrayList,
                R.layout.user,
                new String[]{"icon_user", "name_user"},
                new int[]{R.id.icon_user, R.id.name_user});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {//使listview可以显示bitmap
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
