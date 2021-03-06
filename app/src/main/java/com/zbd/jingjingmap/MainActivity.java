package com.zbd.jingjingmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity   implements GeocodeSearch.OnGeocodeSearchListener, AMap.OnMarkerClickListener {
    AMap aMap = null;

    MapView mMapView = null;
    List<GeocodeAddress> geoList = new ArrayList<>();

    LocationSource.OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;
    UiSettings mUiSettings;//定义一个UiSettings对象



    Button bt_search;
    EditText edittext;
    Button settings;
    Button bt_goto;
    String city;
    double startA;
    double startB;
    double endA;
    double endB;

    int cameraState = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();


        bt_search = (Button)findViewById(R.id.search_bt);
        bt_goto = (Button) findViewById(R.id.go_to);
        edittext = (EditText) findViewById(R.id.search_edit);
        settings = (Button) findViewById(R.id.settings);
        aMap.showIndoorMap(true);
        aMap.setTrafficEnabled(true);
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setScaleControlsEnabled(true);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LocationListActivity.class);
                intent.putExtra("city",city);
                intent.putExtra("startA",startA);
                intent.putExtra("startB",startB);
                startActivity(intent);
            }
        });







        // 设置定位监听
        aMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener listener) {
                mListener = listener;
                if (mlocationClient == null) {
                    //初始化定位
                    mlocationClient = new AMapLocationClient(MainActivity.this);
                    //初始化定位参数
                    mLocationOption = new AMapLocationClientOption();
                    //设置定位回调监听
                    mlocationClient.setLocationListener(new AMapLocationListener() {
                        @Override
                        public void onLocationChanged(AMapLocation amapLocation) {

                            if (mListener != null && amapLocation != null) {
                                if (amapLocation != null
                                        && amapLocation.getErrorCode() == 0) {
                                    mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                                    startA = amapLocation.getLatitude();
                                    startB = amapLocation.getLongitude();

                                    city = amapLocation.getCity();

                                    if (cameraState==0){
                                        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                                        cameraState = 1;
                                    }



                                } else {
                                    String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                                    Log.e("AmapErr",errText);
                                }
                            }

                        }
                    });

                    //设置为高精度定位模式
                    mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                    //设置定位参数
                    mlocationClient.setLocationOption(mLocationOption);
                    // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                    // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
                    // 在定位结束后，在合适的生命周期调用onDestroy()方法
                    // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
                    mlocationClient.startLocation();
                    //启动定位
                }

            }

            @Override
            public void deactivate() {
                mListener = null;
                if (mlocationClient != null) {
                    mlocationClient.stopLocation();
                    mlocationClient.onDestroy();
                }
                mlocationClient = null;

            }
        });
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮

        aMap.setMyLocationEnabled(true);// 可触发定位并显示当前位置

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setVisibility(View.INVISIBLE);


                String addr = edittext.getText().toString();
                GeocodeSearch search = new GeocodeSearch(MainActivity.this);
                search.setOnGeocodeSearchListener(MainActivity.this);
                GeocodeQuery quera = new GeocodeQuery(city+addr,city);
                search.getFromLocationNameAsyn(quera);

//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);


            }
        });

        bt_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusPathListActivity.actionStart(MainActivity.this,startA,startB,endA,endB,city);
            }
        });



        aMap.setOnMarkerClickListener(this);




    }





    public void initMarker(double a, double b){
        LatLng latLng = new LatLng(a,b);
        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng));
    }

    @Override
    protected void onStart() {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        geoList = geocodeResult.getGeocodeAddressList();
        for (int t = 0;t<geoList.size();t++){

            GeocodeAddress geo = geocodeResult.getGeocodeAddressList().get(t);
            LatLonPoint pos = geo.getLatLonPoint();
            LatLng targetPos = new LatLng(pos.getLatitude(),pos.getLongitude());
            CameraUpdate cu = CameraUpdateFactory.changeLatLng(targetPos);
            aMap.moveCamera(cu);

            double a = pos.getLatitude();
            double b = pos.getLongitude();

            endA = a;
            endB = b;
            initMarker(a,b);
            Toast.makeText(MainActivity.this,"请点击坐标确认位置",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.located);
        marker.setIcon(icon);

        bt_goto.setVisibility(View.VISIBLE);

        return false;
    }

    private long mExitTime;
    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {

            finish();
            System.exit(0);
        }
    }
}
