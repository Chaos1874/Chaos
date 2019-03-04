package com.example.tianchao.school_sample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.OrientationListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class tabfragmenteight extends Fragment {
    private View view;

    private static final int BAIDU_READ_PHONE_STATE = 100;

    private MapView mapView;
    private BaiduMap baiduMap;//百度地图对象
    private LocationClient locationClient;
    private MyLocationListener LocationListener;
    private Context context;

    private double Latitude;//纬度
    private double Longitude;//经度
    private float CurrentX;//方向

    private Button GetmylocationBN;
    private Button ChangemodelBN;

    PopupMenu popupMenu = null;//弹出菜单

    private BitmapDescriptor IconLocation;//自定义图标
    private MyOrientationListener myOrientationListener;//方向检测
    private MyLocationConfiguration.LocationMode locationMode;//定义图层显示方式


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现,（tabfragmenteight部分的代码转移到这）
        SDKInitializer.initialize(getActivity().getApplicationContext());
        view = inflater.inflate(R.layout.tabfragmenteight, null);
        context = view.getContext();
        initMaps();
        showLocMap();//判读权限，在进行定位
        return view;
    }

    private void initMaps() {
        mapView = view.findViewById(R.id.mapView_tabfragmenteight);
        //获取百度地图
        baiduMap = mapView.getMap();

        //根据给定增量缩放地图级别
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
        baiduMap.setMapStatus(msu);//设置缩放级别
        MapStatus mapStatus;//地图当前状态
        MapStatusUpdate mapStatusUpdate;//地图将要变成的状态
        mapStatus = new MapStatus.Builder().overlook(-45).build();
        mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.setMapStatus(mapStatusUpdate);

        //类型位普通类型
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//BaiduMap.MAP_TYPE_SATELLITE卫星地图类型
        //开启交通图
        baiduMap.setTrafficEnabled(true);

        GetmylocationBN = view.findViewById(R.id.buttonloc_tabfragmenteight);
        GetmylocationBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();//回到当前位置
            }
        });
        ChangemodelBN = view.findViewById(R.id.buttonmodel_tabfragmenteight);
        ChangemodelBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    onPopupMenuClick(view);//弹出菜单
            }
        });
    }

    private void getLocation() {//每次更换模式以后，新模式的地图呈现
        LatLng latLng = new LatLng(Latitude, Longitude);//经纬度对象
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);//通过经纬度定位
        baiduMap.setMapStatus(msu);
    }

    /*Android6.0以上的版本定位方法*/
    private void showLocMap() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity().getApplicationContext(), "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE
            }, BAIDU_READ_PHONE_STATE);//这里手动申请权限，完成后便会回到onRequestPermissionsResult方法
        } else {
            initLocation();
        }
    }

    //Android 6.0 以上的版本申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    initLocation();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getActivity().getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    //加载定位信息方法
    private void initLocation() {
        locationMode = MyLocationConfiguration.LocationMode.NORMAL;//地图模型种类

        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        locationClient = new LocationClient(getActivity());
        LocationListener = new MyLocationListener();

        //注册监听器
        locationClient.registerLocationListener(LocationListener);
        //配置定位SDK各配置参数，比如定位模式、定位时间间隔、坐标系类型等
        LocationClientOption mOption = new LocationClientOption();
        //设置坐标类型
        mOption.setCoorType("bd09ll");
        //设置是否需要地址信息，默认为无地址
        mOption.setIsNeedAddress(true);
        //设置是否打开gps进行定位
        mOption.setOpenGps(true);
        //设置扫描间隔，单位是毫秒，当<1000(1s)时，定时定位无效
        int span = 1000;
        mOption.setScanSpan(span);
        //设置 LocationClientOption
        locationClient.setLocOption(mOption);

        //初始化图标,BitmapDescriptorFactory是bitmap 描述信息工厂类.
        IconLocation = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_geo);

        myOrientationListener = new MyOrientationListener(context);
        //通过接口回调来实现实时方向的改变
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                CurrentX = x;
            }
        });
    }

    /*所以的定位信息都是通过接口回调来实现*/
    public class MyLocationListener implements BDLocationListener {
        //定位请求回调接口
        private boolean isFirstIn = true;

        //定位请求回调函数，这里面会得到定位信息
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //BDLocation 回调的百度坐标类，内部封装了如经纬度、半径等属性信息
            //MyLocationData 定位数据,定位数据建造器
            /**
             * 可以通过BDLocation配置如下参数
             * 1.accuracy 定位精度
             * 2.latitude 百度纬度坐标
             * 3.longitude 百度经度坐标
             * 4.satellitesNum GPS定位时卫星数目 getSatelliteNumber() gps定位结果时，获取gps锁定用的卫星数
             * 5.speed GPS定位时速度 getSpeed()获取速度，仅gps定位结果时有速度信息，单位公里/小时，默认值0.0f
             * 6.direction GPS定位时方向角度
             * */
            Latitude = bdLocation.getLatitude();
            Longitude = bdLocation.getLongitude();
            MyLocationData data = new MyLocationData.Builder()
                    .direction(CurrentX)//设定图标方向
                    .accuracy(bdLocation.getRadius())//getRadius 获取定位精度,默认值0.0f
                    .latitude(Latitude)//百度纬度坐标
                    .longitude(Longitude)//百度经度坐标
                    .build();
            //设置定位数据, 只有先允许定位图层后设置数据才会生效，参见 setMyLocationEnabled(boolean)
            baiduMap.setMyLocationData(data);
            //配置定位图层显示方式,三个参数的构造器
            /**
             * 1.定位图层显示模式
             * 2.是否允许显示方向信息
             * 3.用户自定义定位图标
             * */
            MyLocationConfiguration configuration
                    = new MyLocationConfiguration(locationMode, true, IconLocation);
            //设置定位图层配置信息，只有先允许定位图层后设置定位图层配置信息才会生效，参见 setMyLocationEnabled(boolean)
            baiduMap.setMyLocationConfigeration(configuration);
            //判断是否为第一次定位,是的话需要定位到用户当前位置
            if (isFirstIn) {
                //地理坐标基本数据结构
                LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                //描述地图状态将要发生的变化,通过当前经纬度来使地图显示到该位置
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                //改变地图状态
                baiduMap.setMapStatus(msu);
                isFirstIn = false;
                Toast.makeText(context, "您当前的位置为：" + bdLocation.getAddrStr(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /*弹出菜单管理*/
    public void onPopupMenuClick(View v) {
        // 创建PopupMenu对象
        popupMenu = new PopupMenu(context, v);
        // 将R.menu.menu_main菜单资源加载到popup菜单中

        popupMenu.getMenuInflater().inflate(R.menu.model_menu, popupMenu.getMenu());
        // 为popup菜单的菜单项单击事件绑定事件监听器
        popupMenu.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.id_map_common:
                                baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                                break;
                            case R.id.id_map_site:
                                baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                                break;
                            case R.id.id_map_traffic:
                                if (baiduMap.isTrafficEnabled()) {
                                    baiduMap.setTrafficEnabled(false);
                                    item.setTitle("实时交通(off)");
                                } else {
                                    baiduMap.setTrafficEnabled(true);
                                    item.setTitle("实时交通(on)");
                                }
                                break;
                            case R.id.id_map_mlocation:
                                getLocation();
                                break;
                            case R.id.id_map_model_common:
                                //普通模式
                                locationMode = MyLocationConfiguration.LocationMode.NORMAL;
                                break;
                            case R.id.id_map_model_following:
                                //跟随模式
                                locationMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                                break;
                            case R.id.id_map_model_compass:
                                //罗盘模式
                                locationMode = MyLocationConfiguration.LocationMode.COMPASS;
                                break;
                        }
                        return true;
                    }
                });
        popupMenu.show();
    }

    public static class MyOrientationListener implements SensorEventListener {

        private SensorManager mSensorManager;
        private Sensor mSensor;
        private Context mContext;
        private float lastX;
        private OnOrientationListener mOnOrientationListener;

        public MyOrientationListener(Context context) {
            this.mContext = context;
        }

        public void start() {
            mSensorManager = (SensorManager) mContext
                    .getSystemService(Context.SENSOR_SERVICE);
            if (mSensorManager != null) {
                //获得方向传感器
                mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            }
            //判断是否有方向传感器
            if (mSensor != null) {
                //注册监听器
                mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }

        public void stop() {
            mSensorManager.unregisterListener(this);
        }

        //方向改变
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                float x = event.values[SensorManager.DATA_X];
                if (Math.abs(x - lastX) > 1.0) {
                    if (mOnOrientationListener != null) {
                        mOnOrientationListener.onOrientationChanged(x);
                    }
                }
                lastX = x;
            }
        }

        public void setOnOrientationListener(OnOrientationListener listener) {
            mOnOrientationListener = listener;
        }

        public interface OnOrientationListener {
            void onOrientationChanged(float x);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    }

    /*地图生命周期管理*/

    @Override
    public void onStart() {
        super.onStart();
        //开启定位
        baiduMap.setMyLocationEnabled(true);
            locationClient.start();

        myOrientationListener.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        //停止定位
        baiduMap.setMyLocationEnabled(false);
        locationClient.stop();
        myOrientationListener.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

}
