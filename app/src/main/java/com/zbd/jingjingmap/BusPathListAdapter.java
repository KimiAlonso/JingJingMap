package com.zbd.jingjingmap;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.Doorway;
import com.amap.api.services.route.RouteBusLineItem;
import com.zbd.jingjingmap.Util.AMapUtil;

import java.util.ArrayList;
import java.util.List;



public class BusPathListAdapter extends BaseAdapter {

    List <BusPath> pathList;
    Context mContext;
    String TAG = "path";
    BusRouteResult mBusRouteResult;

    public BusPathListAdapter(List <BusPath> list, Context context, BusRouteResult busRouteResult){
        pathList = list;
        mContext = context;
        mBusRouteResult =busRouteResult;
    }


    @Override
    public int getCount() {
        return pathList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MViewHolder mViewHolder = null;
        if (convertView == null){
            mViewHolder = new MViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_buspathlist,null);
            mViewHolder.busPathInfo = (TextView) convertView.findViewById(R.id.buspathname);
            mViewHolder.walk = (TextView) convertView.findViewById(R.id.walk);
            mViewHolder.distance = (TextView) convertView.findViewById(R.id.distance);

            convertView.setTag(mViewHolder);

        }
        else {
            mViewHolder = (MViewHolder)convertView.getTag();
        }


        final BusPath item = pathList.get(position);

        mViewHolder.busPathInfo.setText(AMapUtil.getBusPathTitle(item));
        String distance_walk = "步行距离"+Tools.subZeroAndDot(String.valueOf(item.getWalkDistance()))+"m";
        String distance_all = "总距离"+Tools.subZeroAndDot(String.valueOf(item.getDistance()))+"m";
        mViewHolder.walk.setText(distance_walk);
        mViewHolder.distance.setText(distance_all);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(),
                        BusDetailActivity.class);
                intent.putExtra("bus_path", item);
                intent.putExtra("bus_result", mBusRouteResult);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
    public class MViewHolder{
        TextView busPathInfo;
        TextView walk;
        TextView distance;

    }




}
