package com.zbd.jingjingmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import java.util.List;


public class BusPathListActivity extends AppCompatActivity implements RouteSearch.OnRouteSearchListener{

    RouteSearch.FromAndTo fromAndTo;
    LatLonPoint start;
    LatLonPoint end;
    RouteSearch search;
    ListView busPathList;
    BusPathListAdapter mAdapter;

    public static void actionStart(Context context, double startA, double startB,double endA,double endB){
        Intent intent = new Intent(context,BusPathListActivity.class);
        intent.putExtra("startA",startA);
        intent.putExtra("startB",startB);
        intent.putExtra("endA",endA);
        intent.putExtra("endB",endB);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buspathlist);
        initFromAndTo();

        search = new RouteSearch(this);
        search.setRouteSearchListener(this);

        busPathList = (ListView) findViewById(R.id.buspathlist);




        RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, RouteSearch.BusLeaseWalk, "南阳",1);
        search.calculateBusRouteAsyn(query);


    }

    public void initFromAndTo(){
        start = new LatLonPoint(getIntentMsg("startA"), getIntentMsg("startB"));
        end = new LatLonPoint(getIntentMsg("endA"),getIntentMsg("endB"));
        fromAndTo = new RouteSearch.FromAndTo(start,end);
    }

    public double getIntentMsg(String a){
        return getIntent().getDoubleExtra(a,0);
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
        List<BusPath> paths = busRouteResult.getPaths();

        mAdapter = new BusPathListAdapter(paths,BusPathListActivity.this,busRouteResult);
        busPathList.setAdapter(mAdapter);
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
