package com.example.tianchao.school_sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class fragmentthree extends Fragment {
    public View view;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<Fragment>(8);
    private String[] TabTitles = new String[8];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmentthree, null);

        tabLayout = view.findViewById(R.id.tabLayout_fragmentthree);
        viewPager = view.findViewById(R.id.viewpager_fragmentthree);
        initView();

        return view;
    }

    private void initView() {
        TabTitles[0] = "课程表";
        TabTitles[1] = "校园充值";
        TabTitles[2] = "成绩查询";
        TabTitles[3] = "选修明细";
        TabTitles[4] = "校园兼职";
        TabTitles[5] = "校园论坛";
        TabTitles[6] = "寻物启事";
        TabTitles[7] = "校园地图";
        tabLayout.setTabMode(tabLayout.MODE_SCROLLABLE);//设置模型滚动
        //设置tablayout距离上下左右的距离
        tabLayout.setPadding(20,0,20,0);
        fragmentArrayList.add(new tabfragmentone());
        fragmentArrayList.add(new tabfragmenttwo());
        fragmentArrayList.add(new tabfragmentthree());
        fragmentArrayList.add(new tabfragmentfour());
        fragmentArrayList.add(new tabfragmentfive());
        fragmentArrayList.add(new tabfragmentsix());
        fragmentArrayList.add(new tabfragmentseven());
        fragmentArrayList.add(new tabfragmenteight());
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public int getCount() {
                return fragmentArrayList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragmentArrayList.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        /*添加标题*/
        for(int i =0;i<8;i++) {
            tabLayout.getTabAt(i).setText(TabTitles[i]);
           // tabLayout.addTab(tabLayout.newTab().setText(TabTitles[i]));
        }
    }
}

