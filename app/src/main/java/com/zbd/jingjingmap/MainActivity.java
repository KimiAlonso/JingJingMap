package com.zbd.jingjingmap;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


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
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.zbd.jingjingmap.Database.DatabaseHelper;


public class MainActivity extends AppCompatActivity   implements GeocodeSearch.OnGeocodeSearchListener{
    AMap aMap = null;

    MapView mMapView = null;

    LocationSource.OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;
    UiSettings mUiSettings;//定义一个UiSettings对象



    Button button;
    EditText edittext;
    Button settings;

    int cameraState = 0;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();


        button = (Button)findViewById(R.id.search_bt);
        edittext = (EditText) findViewById(R.id.search_edit);
        settings = (Button) findViewById(R.id.settings);
        aMap.showIndoorMap(true);
//        initMarker();
        aMap.setTrafficEnabled(true);
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setScaleControlsEnabled(true);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addr = edittext.getText().toString();
                GeocodeSearch search = new GeocodeSearch(MainActivity.this);
                search.setOnGeocodeSearchListener(MainActivity.this);
                GeocodeQuery quera = new GeocodeQuery(addr,"郑州");
                search.getFromLocationNameAsyn(quera);


            }
        });








    }



    public void initMarker(double a, double b){
        LatLng latLng = new LatLng(a,b);
        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("Home").snippet("DefaultMarker"));
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

        GeocodeAddress geo = geocodeResult.getGeocodeAddressList().get(0);
        LatLonPoint pos = geo.getLatLonPoint();
        LatLng targetPos = new LatLng(pos.getLatitude(),pos.getLongitude());
        CameraUpdate cu = CameraUpdateFactory.changeLatLng(targetPos);
        aMap.moveCamera(cu);
        double a = pos.getLatitude();
        double b = pos.getLongitude();
        initMarker(a,b);


    }
}
