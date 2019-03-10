package com.example.tianchao.school_sample.fragment;

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

import com.example.tianchao.school_sample.R;

import java.util.ArrayList;
import java.util.HashMap;

public class fragmentfour extends Fragment {
    private View view;
    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private ArrayList arrayList = new ArrayList();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragmentfour, null);

        listView = view.findViewById(R.id.list_fragmentfour);
        initJason();
        addAdapter();

        return view;
    }

    private void initJason() {
        for(int i=0;i<3;i++){
            HashMap hashMap = new HashMap();
            hashMap.put("image_work",R.drawable.ic_home_black_24dp);
            hashMap.put("text_work","fasdfs");
            arrayList.add(hashMap);
        }
    }
    private  void addAdapter(){
        simpleAdapter = new SimpleAdapter(view.getContext(),
                arrayList,
                R.layout.work,
                new String[]{"image_work","text_work"},
                new int[]{R.id.image_work,R.id.text_work});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(), "fadsdasf", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
