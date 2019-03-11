package com.example.tianchao.school_sample.Tools;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.example.tianchao.school_sample.R;
import com.example.tianchao.school_sample.Tools.ImageDownLoader.AsyncImageLoaderListener;
import com.example.tianchao.school_sample.jsonObject.CourseObject;
import com.example.tianchao.school_sample.jsonObject.NewsObject;
import com.example.tianchao.school_sample.jsonObject.UserObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class initjson {
    private ImageDownLoader imageDownLoader;
    private View view;

    public initjson(View view) {
        this.view = view;
        imageDownLoader = new ImageDownLoader(view.getContext());
    }

    public NewsObject initNewsJson(JSONArray jsonArray) throws InterruptedException {
        NewsObject newsObject = new NewsObject();
        String[] tittle = new String[jsonArray.length()];
        String[] picString = new String[jsonArray.length()];
        String[] description = new String[jsonArray.length()];
        String[] link = new String[jsonArray.length()];
        ArrayList<Bitmap> picture = new ArrayList<Bitmap>(jsonArray.length());
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                tittle[i] = jsonObject.get("tittle").toString();
                picString[i] = CONSTANT.HOST + jsonObject.get("picture").toString();
                description[i] = jsonObject.get("description").toString();
                link[i] = jsonObject.get("link").toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //加载图片
        int[] Nullnumber = new int[50];
        int m =0;
        for (int i = 0; i < picString.length; i++) {
            Bitmap bitmap = imageDownLoader.getBitmapCache(picString[i]);//先从内存和缓存中查找图片
            if (bitmap != null) {
                picture.add(i,bitmap);
            } else {
                Nullnumber[m] = i;
                imageDownLoader.loadImage(picString[i],
                        300, 400);
                m++;
            }
        }
        ArrayList<Bitmap> arrayList = null;
        try {
            arrayList = imageDownLoader.startPool();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        m=0;
        for(Bitmap b:arrayList){
            picture.add(Nullnumber[m],b);
            m++;
        }

            newsObject.setPicture( picture);
            newsObject.setTittle(tittle);
            newsObject.setPicString(picString);
            newsObject.setDescription(description);
            newsObject.setLink(link);
            return newsObject;
    }


        public UserObject initUserJson (JSONArray jsonArray){
            UserObject userObject = new UserObject();
            return userObject;
        }
        public CourseObject initCoursejson (JSONArray jsonArray) throws InterruptedException {
            CourseObject courseObject = new CourseObject();
            String[] name = new String[jsonArray.length()];
            String[] num = new String[jsonArray.length()];
            String[] teacher = new String[jsonArray.length()];
            String[] information = new String[jsonArray.length()];
            String[] picString = new String[jsonArray.length()];
            String[] time = new String[jsonArray.length()];
            String[] place = new String[jsonArray.length()];
            String[] teacher_name = new String[jsonArray.length()];
            ArrayList<Bitmap> picture = new ArrayList<Bitmap>(jsonArray.length());
            try {
                int k = 0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.get("on_net").toString().equals("true")) {
                        name[k] = jsonObject.get("name").toString();
                        picString[k] = CONSTANT.HOST + jsonObject.get("picture").toString();
                        num[k] = jsonObject.get("num").toString();
                        teacher[k] = jsonObject.get("teacher").toString();
                        information[k] = jsonObject.get("information").toString();
                        teacher_name[k] = jsonObject.get("teacher_name").toString();
                        k++;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //加载图片
            int[] Nullnumber = new int[50];
            int m =0;
            for (int i = 0; i < picString.length; i++) {
                Bitmap bitmap = imageDownLoader.getBitmapCache(picString[i]);//先从内存和缓存中查找图片
                if (bitmap != null) {
                    picture.add(i,bitmap);
                }
                else {
                    Nullnumber[m] = i;
                    imageDownLoader.loadImage(picString[i],
                            300,400);
                    m++;
                }
            }
            ArrayList<Bitmap> arrayList = null;
            try {
                arrayList = imageDownLoader.startPool();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            m=0;
            for(Bitmap b:arrayList){
                picture.add(Nullnumber[m],b);
                m++;
            }
            courseObject.setPicture(picture);//开启线程
            courseObject.setName(name);
            courseObject.setNum(num);
            courseObject.setTeacher(teacher);
            courseObject.setInformation(information);
            courseObject.setPicString(picString);
            courseObject.setTime(time);
            courseObject.setPlace(place);
            courseObject.setTeacher_name(teacher_name);
            return courseObject;
        }
    }
