package com.example.tianchao.school_sample.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.tianchao.school_sample.R;
import com.example.tianchao.school_sample.fragment.fragmentfour;
import com.example.tianchao.school_sample.fragment.fragmentone;
import com.example.tianchao.school_sample.fragment.fragmentthree;
import com.example.tianchao.school_sample.fragment.fragmenttwo;

import java.util.ArrayList;

public class main_page extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragmentlist = new ArrayList<>(4);

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_course:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_service:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_mine:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*去除标签栏*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remove notification bar  即全屏

        setContentView(R.layout.activity_main_page);

        navigationView = findViewById(R.id.navigation_main);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager=findViewById(R.id.viewpager_main);

        fragmentlist.add(new fragmentone());
        fragmentlist.add(new fragmenttwo());
        fragmentlist.add(new fragmentthree());
        fragmentlist.add(new fragmentfour());


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {//viewpager的监听
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigationView.getMenu().getItem(position).setChecked(true);
                //滑动页面后做的事，这里与BottomNavigationView结合，使其与正确page对应
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return fragmentlist.get(position);
            }

            @Override
            public int getCount() {
                return fragmentlist.size();
            }
        };

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);//预加载剩下三页
    }

}
