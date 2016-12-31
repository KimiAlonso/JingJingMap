package com.zbd.jingjingmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
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
import com.zbd.jingjingmap.Database.CustomLocation;
import com.zbd.jingjingmap.Database.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ChooseLocationActivity extends AppCompatActivity implements GeocodeSearch.OnGeocodeSearchListener{

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
    Button locate;
    Button saveLocation;
    Context context;

    String mName;
    String mDetail;

    List<Map<Double,Double>> resultList = new ArrayList<>();
    List<GeocodeAddress> geoList = new ArrayList<>();

    AMap aMap = null;

    MapView mMapView = null;
    UiSettings mUiSettings;//定义一个UiSettings对象

    double markerLatitude;
    double markerLongitude;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooselocation);

        context = ChooseLocationActivity.this;

        nameEdit = (EditText) findViewById(R.id.name_deit);
        detailEdit = (EditText) findViewById(R.id.detail_deit);
        locate = (Button) findViewById(R.id.locate);
        saveLocation = (Button) findViewById(R.id.save_location);
        saveLocation.setEnabled(false);




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


        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addr = detailEdit.getText().toString();
                GeocodeSearch search = new GeocodeSearch(ChooseLocationActivity.this);
                search.setOnGeocodeSearchListener(ChooseLocationActivity.this);
                GeocodeQuery quera = new GeocodeQuery(addr,"郑州");
                search.getFromLocationNameAsyn(quera);
                mName = nameEdit.getText().toString();
                mDetail = detailEdit.getText().toString();

            }
        });

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                markerLatitude = marker.getPosition().latitude;
                markerLongitude = marker.getPosition().longitude;
                saveLocation.setEnabled(true);
                return true;
            }
        });

        saveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper helper = new DatabaseHelper(context);
                CustomLocation customLocation = new CustomLocation();
                try {
                    List<CustomLocation> mList = helper.getDao(customLocation.getClass()).queryBuilder().where().eq("name",mName).query();
                    customLocation.setName(mName);

                    if(mList.size() == 0){
                        helper.getDao(customLocation.getClass()).create(customLocation);

                    }else {
                        customLocation.setId(mList.get(0).getId());
                        helper.getDao(customLocation.getClass()).update(customLocation);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Toast.makeText(context,String.valueOf(markerLatitude),Toast.LENGTH_SHORT).show();
            }
        });




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
            initMarker(a,b);
        }





    }

    public void initMarker(double a,double b){
        LatLng latLng = new LatLng(a,b);
        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("Home").snippet("DefaultMarker"));
    }
}
