package com.example.tianchao.school_sample.fragment;

import android.graphics.Bitmap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.tianchao.school_sample.Tools.ACache;
import com.example.tianchao.school_sample.R;
import com.example.tianchao.school_sample.Tools.ImageDownLoader;
import com.example.tianchao.school_sample.Tools.initjson;
import com.example.tianchao.school_sample.jsonObject.CourseObject;
import com.example.tianchao.school_sample.jsonObject.NewsObject;

import java.util.ArrayList;
import java.util.HashMap;

public class fragmenttwo extends Fragment {
    public View view;

    private ImageDownLoader imageDownLoader;
    private initjson initjson;

    private String[] time = new String[100];
    private String[] place = new String[100];
    private CourseObject courseObject = new CourseObject();

    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private ArrayList arrayList = new ArrayList();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmenttwo, null);
        listView = view.findViewById(R.id.list_fragmenttwo);

        initjson = new initjson(view);
        imageDownLoader = new  ImageDownLoader(view.getContext());

        try {
            initJason();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        initInformation();
        initView();
        addAdapter();

        return  view;
    }


    private void initInformation() {
        for(int i=0;i<courseObject.getInformation().length;i++){
            String[] para = courseObject.getInformation()[i].split(",");
            for(int k=0;k<para.length;k++){
                String[] part = para[k].split(":");
                time[i] = time[i]+" 星期"+part[0]+"第"+part[1]+"节";
                place[i]= place[i]+" "+part[2];
            }
        }
    }

    private  void initJason() throws InterruptedException {
        courseObject= initjson.initCoursejson(ACache.get(getActivity().getApplicationContext()).getAsJSONArray("course"));
    }


    private void initView() {
        //System.out.println(courseObject.getPicture().size()+"sfasddfas");
        //System.out.println(courseObject.getName().length+"www");
        for(int i=0;i<courseObject.getName().length;i++){
            HashMap hashMap = new HashMap();
            hashMap.put("image_course",(Bitmap)courseObject.getPicture().get(i));
            hashMap.put("coursename_course",courseObject.getName()[i]);
            hashMap.put("teacher_course",courseObject.getTeacher()[i]);
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
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {//使listview可以显示bitmap
                if(view instanceof ImageView &&data instanceof  Bitmap){
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return  true;
                }
                return false;
            }
        });
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(), "fadsdasf", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
