package com.zbd.jingjingmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;


public class ChooseLocationActivity extends AppCompatActivity{

    public static void actionStart(Context context,String locationName,String locationDetail){
        Intent intent = new Intent(context,ChooseLocationActivity.class);
        intent.putExtra("locationName",locationName);
        intent.putExtra("locationDetail",locationDetail);
        context.startActivity(intent);
    }

    String name;
    String detail;
    EditText nameEdit;
    EditText detailEdit;

    AMap aMap = null;

    MapView mMapView = null;
    UiSettings mUiSettings;//定义一个UiSettings对象



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooselocation);

        nameEdit = (EditText) findViewById(R.id.name_deit);
        detailEdit = (EditText) findViewById(R.id.detail_deit);
        name = getIntent().getStringExtra("locationName");
        detail = getIntent().getStringExtra("locationDetail");
        nameEdit.setText(name);
        detailEdit.setText(detail);

        mMapView = (MapView) findViewById(R.id.map_choose);

        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        aMap.setTrafficEnabled(true);
        aMap.setTrafficEnabled(true);
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setScaleControlsEnabled(true);




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
}
