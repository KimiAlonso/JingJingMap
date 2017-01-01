package com.zbd.jingjingmap;

import android.content.Context;
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
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.RouteBusLineItem;

import java.util.ArrayList;
import java.util.List;


public class BusPathListAdapter extends BaseAdapter {

    List <BusPath> pathList;
    Context mContext;
    String TAG = "path";

    public BusPathListAdapter(List <BusPath> list,Context context){
        pathList = list;
        mContext = context;
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
        MViewHolder mViewHolder = new MViewHolder();
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_buspathlist,null);
        mViewHolder.busPathInfo = (TextView) convertView.findViewById(R.id.buspathname);

//        String name = String.valueOf(pathList.get(position).getBusDistance());
//        mViewHolder.busPathInfo.setText(name);
        String Msg = initBusMsg(pathList,position);
        mViewHolder.busPathInfo.setText(Msg);



        return convertView;
    }
    public class MViewHolder{
        TextView busPathInfo;

    }

    public String initBusMsg(List<BusPath> mList,int position){
        String destence = String.valueOf(mList.get(position).getBusDistance());

        List<BusStep> stepList = mList.get(position).getSteps();
        String allMsg = null;
        String t = new String();
        for (int a=0;a<stepList.size();a++){
            List<RouteBusLineItem> rbList = stepList.get(a).getBusLines();
            t = t+"i-";
            String x = new String();
            if (rbList.size()==1){
                 x = rbList.get(0).getDepartureBusStation().getAdCode();
            }

//            Log.e(TAG, "initBusMsg: "+rbList.get(0).getDepartureBusStation().toString() );


//            for (int b=0;b<rbList.size();b++){
//                BusStationItem startStation = rbList.get(b).getDepartureBusStation();
//                Log.e(TAG, "start: "+startStation.toString());
//                BusStationItem endStation = rbList.get(b).getArrivalBusStation();
//                Log.e(TAG, "end: "+endStation.toString());
//                allMsg = allMsg+"---change---"+getBusNumbMsg(startStation,endStation);
//                List <LatLonPoint> llpList = rbList.get(b).getPolyline();
//
//            }
            Log.e(TAG, "initBusMsg: ______________________"+x );

        }
        return t;
    }

    public String getBusNumbMsg(BusStationItem startStation,BusStationItem endStation){
        String result = null;
        List<BusLineItem> startLines = startStation.getBusLineItems();

        List<BusLineItem> endLines = endStation.getBusLineItems();
        Log.e(TAG, "getBusNumbMsg: "+startStation.getBusStationName());

        List<String> busNames = new ArrayList<String>();
        for (int i=0;i<startLines.size();i++){


            String startMsg = startLines.get(i).getBusLineName();

            for (int a=0;a<endLines.size();a++){
                if (startMsg == endLines.get(a).getBusLineName()){
                    String t = startMsg;
                    busNames.add(t);
                }
            }
        }

        for (int b=0;b<busNames.size();b++){
            result = result+busNames.get(b);
        }

        return result;

    }
}
