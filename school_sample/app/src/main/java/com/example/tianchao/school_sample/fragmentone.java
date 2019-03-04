package com.example.tianchao.school_sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class fragmentone extends Fragment {

    private final int LOGINSUCCESS=0;
    private final int LOGINNOTFOUND=1;
    private final int LOGINEXCEPT=2;

    public View view;

    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private ArrayList arrayList = new ArrayList();
    private  String[] tittle;
    // 在values文件假下创建了pic.xml文件，并定义了4张轮播图对应的id，用于点击事件
    private int[] image_ids = new int[]{R.id.pager_image1, R.id.pager_image2, R.id.pager_image3, R.id.pager_image4};
    private Bitmap[] picture;
    private  String[] description;
    private  String[] link;

    private ViewPager viewPager;
    private TextView descrip;//标题
    private List<ImageView> ImageList;//轮播的图片集合
    private int previousPosition = 0;//前一个被选中的position
    private List<View> Dots;//小点
    private boolean isStop = false;//线程是否停止
    private static int PAGER_TIOME = 5000;//间隔时间

    /*Handler handler=new Handler(){//消息机制，用来在子线程中更新UI
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){//具体消息，具体显示
                case LOGINSUCCESS:
                    if(msg.obj.equals("获取失败")) {
                        Toast.makeText(view.getContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                    }
                    else{
                        System.out.println("555");
                        initJason((String) msg.obj);
                    }
                    break;
                case LOGINNOTFOUND:
                    Toast.makeText(view.getContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case LOGINEXCEPT:
                    Toast.makeText(view.getContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };*/


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmentone, null);
        /**
         * 第一步、初始化控件
         */
        listView = view.findViewById(R.id.list_fragmentone);

        viewPager = view.findViewById(R.id.viewpager_fragmentone);
        descrip = view.findViewById(R.id.descrip_fragentone);

        Toast.makeText(view.getContext(), "fdas", Toast.LENGTH_LONG).show();

        /*服务器获得数据*/
        //requestNews();
        initJason();


        return view;
    }

    /*private void requestNews() {
        new Thread(){
            private HttpURLConnection connection;
            @Override
            public void run() {
                try {
                    //封装成传输数据的键值对,无论get还是post,传输中文时都要进行url编码（RULEncoder）
                    // 如果是在浏览器端的话，它会自动进行帮我们转码，不用我们进行手动设置
                    String data= "information="+ URLEncoder.encode("news","utf-8");
                    connection=HttpConnettionUtils.getConnection(data,"sendNews");
                    int code = connection.getResponseCode();
                    if(code==200){//成功
                        InputStream inputStream = connection.getInputStream();
                        String str = StreamString.Stream2String(inputStream);//写个工具类流转换成字符串
                        Message message = Message.obtain();//更新UI就要向消息机制发送消息
                        message.what=LOGINSUCCESS;//用来标志是哪个消息
                        message.obj=str;//消息主体
                        System.out.println(str);
                        handler.sendMessage(message);
                    }
                    else {
                        Message message = Message.obtain();
                        message.what=LOGINNOTFOUND;
                        message.obj="服务器异常...请稍后再试1";
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {//会抛出很多个异常，这里抓一个大的异常
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what=LOGINEXCEPT;
                    message.obj="服务器异常...请稍后再试2";
                    handler.sendMessage(message);
                }
            }
        }.start();

    }*/

    private void initJason(){
        try {
           JSONArray jsonArray = ACache.get(getActivity().getApplicationContext()).getAsJSONArray("news");
            tittle = new String[jsonArray.length()];
            description = new String[jsonArray.length()];
            picture = new Bitmap[jsonArray.length()];
            link = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                tittle[i] =  jsonObject.get("tittle").toString();
                picture[i]=StreamString.imgStringtobitmap( jsonObject.get("picture").toString());
                description[i] = jsonObject.get("description").toString();
                link[i] = jsonObject.get("link").toString();
            }
            /*news加载完后开始显示*/
            /*lisView加载*/
            initListmap();
            addAdapter();

            /*pagerView加载*/
            initPagerViewData();//初始化数据
            initPagerView();//初始化View，设置适配器
            autoPlayView();//开启线程，自动播放
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void initListmap(){
        for(int i=0;i<tittle.length;i++){
            HashMap hashMap = new HashMap();
            hashMap.put("tittle_news",tittle[i]);
            hashMap.put("description_news",description[i]);
            hashMap.put("image_news",picture[i]);
            arrayList.add(hashMap);
        }
    }

    private void addAdapter() {
        simpleAdapter = new SimpleAdapter(view.getContext(),
                arrayList,
                R.layout.news,
                new String[]{"tittle_news","description_news","image_news"},
                new int[]{R.id.tittle_news,R.id.descripment_news,R.id.image_news});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {//使listview可以显示bitmap
                if(view instanceof  ImageView&&data instanceof  Bitmap){
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
                Toast.makeText(view.getContext(), link[position], Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void initPagerViewData() {

        //添加图片到图片列表里
        ImageList = new ArrayList<>();
        ImageView iv;
        for (int i = 0; i < image_ids.length; i++) {
            iv = new ImageView(view.getContext());
            iv.setImageBitmap(picture[i]);//设置图片
            iv.setId(image_ids[i]);//顺便给图片设置id
            iv.setOnClickListener(new pagerImageOnclick());//设置图片点击事件
            ImageList.add(iv);
        }
        //添加轮播点
        LinearLayout linearLayout = view.findViewById(R.id.linerLayout_fragmentone);
        Dots = addDots(linearLayout, fromResToDrawable(view.getContext(), R.drawable.ic_dot_normal), ImageList.size());//其中fromResToDrawable()方法是我自定义的，目的是将资源文件转成Drawable
    }

    /**
     *  第三步、给PagerViw设置适配器，并实现自动轮播功能
     */
    public void initPagerView() {
        ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter(ImageList,viewPager);
        viewPager.setAdapter(viewpagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {//viewpager切换时调用
                //伪无限循环，滑到最后一张图片又从新进入第一张图片
                int newPosition = (int) (position % ImageList.size());
                // 把当前选中的点给切换了, 还有描述信息也切换
                descrip.setText(tittle[newPosition]);//图片下面设置显示文本
                //设置轮播点
                LinearLayout.LayoutParams newDotParams = (LinearLayout.LayoutParams) Dots.get(newPosition).getLayoutParams();
                newDotParams.width = 24;
                newDotParams.height = 24;

                LinearLayout.LayoutParams oldDotParams = (LinearLayout.LayoutParams) Dots.get(previousPosition).getLayoutParams();
                oldDotParams.width = 16;
                oldDotParams.height = 16;

                // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
                previousPosition = newPosition;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setFirstLocation();
    }

    /**
     * 第五步: 设置自动播放,每隔PAGER_TIOME秒换一张图片
     */
    private void autoPlayView() {
        //自动播放图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {//线程更改ui
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        }
                    });
                    SystemClock.sleep(PAGER_TIOME);
                }
            }
        }).start();
    }

    private class pagerImageOnclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), newscontext.class);//打开网页显示news的内容
            switch (v.getId()) {
                case R.id.pager_image1:
                    Toast.makeText(view.getContext(), "图片1被点击", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    break;
                case R.id.pager_image2:
                    Toast.makeText(view.getContext(), "图片2被点击", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    break;
                case R.id.pager_image3:
                    Toast.makeText(view.getContext(), "图片3被点击", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    break;
                case R.id.pager_image4:
                    Toast.makeText(view.getContext(), "图片4被点击", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    break;

            }
        }
    }
    /**
     * 第四步：设置刚打开app时显示的图片和文字
     */
    private void setFirstLocation() {
        descrip.setText(tittle[previousPosition]);
        //把ViewPager设置为默认选中Integer.MAX_VALUE / 2，从十几亿次开始轮播图片，达到无限循环目的;
       int m = (int) (( Integer.MAX_VALUE / 2.0) % ImageList.size());
        int currentPosition = (int) (Integer.MAX_VALUE / 2.0 - m);
        viewPager.setCurrentItem(currentPosition);
    }

    /**
     * 资源图片转Drawable
     * @param context
     * @param resId
     * @return
     */
    public Drawable fromResToDrawable(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }

    /**
     * 动态添加一个点
     * @param linearLayout 添加到LinearLayout布局
     * @param backgount 设置
     * @return
     */
    public int addDot(final LinearLayout linearLayout, Drawable backgount) {
        final View dot = new View(view.getContext());
        LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dotParams.width = 16;
        dotParams.height = 16;
        dotParams.setMargins(4,0,4,0);
        dot.setLayoutParams(dotParams);
        dot.setBackground(backgount);
        dot.setId(View.generateViewId());
        linearLayout.addView(dot);
        return dot.getId();
    }

    /**
     * 添加多个轮播小点到横向线性布局
     * @param linearLayout
     * @param backgount
     * @param number
     * @return
     */
    public List<View> addDots(final LinearLayout linearLayout, Drawable backgount, int number){
        List<View> dots = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int dotId = addDot(linearLayout,backgount);
            dots.add(view.findViewById(dotId));
        }
        return dots;
    }

}