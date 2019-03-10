package com.example.tianchao.school_sample.Tools;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class ViewpagerAdapter  extends PagerAdapter{
    private List<ImageView> images;
    private  ViewPager viewPager;

    public ViewpagerAdapter(List<ImageView> images, ViewPager viewPager) {
        this.images  = images;
        this.viewPager = viewPager;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;//返回一个无限大的值，可以 无限循环
    }

    /**
     * 判断是否使用缓存, 如果返回的是true, 使用缓存. 不去调用instantiateItem方法创建一个新的对象
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    /**
     * 初始化一个条目
     * @param container
     * @param position 当前需要加载条目的索引
     * @return
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // 把position对应位置的ImageView添加到ViewPager中
        ImageView iv = images.get(position%images.size());
        viewPager.addView(iv);
        return iv;
    }

    /**
     * 销毁一个条目
     * position 就是当前需要被销毁的条目的索引
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        viewPager.removeView(images.get(position%images.size()));
    }
}
