package com.example.tianchao.school_sample;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class fragmenttwo extends Fragment {
    public View view;

    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private ArrayList arrayList = new ArrayList();

    private  String[] name;
    private  String[] num;
    private  String[] teacher;
    private  String[] information;
    private  String[] time ;
    private  String[] place;
    private Bitmap[] picture;//暂时不获取图片


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmenttwo, null);
        listView = view.findViewById(R.id.list_fragmenttwo);
        initJason();
        initInformation();
        initView();
        addAdapter();
        return  view;
    }

    private void initInformation() {
        for(int i=0;i<information.length;i++){
            System.out.println(information[i]);
            String[] para = information[i].split(",");
            for(int k=0;k<para.length;k++){
                String[] part = para[k].split(":");
                time[i] = time[i]+" 星期"+part[0]+"第"+part[1]+"节";
                place[i]= place[i]+" "+part[2];
            }
        }
    }

    private  void initJason() {
        try {
            JSONArray jsonArray = ACache.get(getActivity().getApplicationContext()).getAsJSONArray("course");
            name = new String[jsonArray.length()];
            num = new String[jsonArray.length()];
            teacher = new String[jsonArray.length()];
            information = new String[jsonArray.length()];
            picture = new Bitmap[jsonArray.length()];
            time =  new String[jsonArray.length()];
            place =  new String[jsonArray.length()];
            int k=0;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                System.out.println("aaa");
                if(jsonObject.get("on_net").toString().equals("true")) {
                    System.out.println("bbb");
                    name[k] = jsonObject.get("name").toString();
                    System.out.println("fsad"+ jsonObject.get("name").toString());
                    picture[k] = StreamString.imgStringtobitmap(jsonObject.get("picture").toString());
                    num[k] = jsonObject.get("num").toString();
                    System.out.println("fsad"+ jsonObject.get("num").toString());
                    teacher[k] = jsonObject.get("teacher").toString();
                    System.out.println("fsad"+ jsonObject.get("teacher").toString());
                    information[k] = jsonObject.get("information").toString();
                    System.out.println( "fsad"+jsonObject.get("information").toString());
                    k++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        for(int i=0;i<name.length;i++){
            HashMap hashMap = new HashMap();
            hashMap.put("image_course",R.drawable.backa);
            hashMap.put("coursename_course",name[i]);
            hashMap.put("teacher_course",teacher[i]);
            hashMap.put("time_course",time[i]);
            hashMap.put("place_course",place[i]);
            arrayList.add(hashMap);
        }
    }

    private void addAdapter() {
        simpleAdapter = new SimpleAdapter(view.getContext(),
                arrayList,
                R.layout.course,
                new String[]{"image_course","coursename_course","teacher_course","time_course","place_course"},
                new int[]{R.id.image_course,R.id.coursename_course,R.id.teacher_course,R.id.time_course,R.id.place_course});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(), "fadsdasf", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
