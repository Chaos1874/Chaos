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


        public UserObject initUserJson (JSONArray jsonArray) throws InterruptedException {
            UserObject userObject = new UserObject();

            String[] name = new String[jsonArray.length()];
            String[] id_school = new String[jsonArray.length()];
            String[] id_card = new String[jsonArray.length()];
            String[] phone = new String[jsonArray.length()];
            String[] type = new String[jsonArray.length()];
            String[] username = new String[jsonArray.length()];
            String[] password = new String[jsonArray.length()];
            String[] is_signed = new String[jsonArray.length()];
            String[] class_num = new String[jsonArray.length()];
            String[] icon = new String[jsonArray.length()];
            ArrayList<Bitmap> picture = new ArrayList<Bitmap>(jsonArray.length());

            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    name[i] = jsonObject.get("name").toString();
                    id_school[i] = jsonObject.get("id_school").toString();
                    id_card[i] = jsonObject.get("id_card").toString();
                    phone[i] = jsonObject.get("phone").toString();
                    type[i] = jsonObject.get("type").toString();
                    username[i] = jsonObject.get("username").toString();
                    password[i] = jsonObject.get("password").toString();
                    is_signed[i] = jsonObject.get("is_signed").toString();
                    class_num[i] = jsonObject.get("class_num").toString();
                    icon[i] =  CONSTANT.HOST + jsonObject.get("icon").toString();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //加载图片
            int[] Nullnumber = new int[50];
            int m =0;
            for (int i = 0; i < icon.length; i++) {
                    Bitmap bitmap = imageDownLoader.getBitmapCache(icon[i]);//先从内存和缓存中查找图片
                    if (bitmap != null) {
                        picture.add(i, bitmap);
                    } else {
                        Nullnumber[m] = i;
                        imageDownLoader.loadImage(icon[i],
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

            userObject.setClass_num(class_num);
            userObject.setIcon(icon);
            userObject.setId_card(id_card);
            userObject.setId_school(id_school);
            userObject.setIs_signed(is_signed);
            userObject.setName(name);
            userObject.setPassword(password);
            userObject.setPicture(picture);
            userObject.setPhone(phone);
            userObject.setType(type);
            userObject.setUsername(username);
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
            courseObject.setPicture(picture);
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
