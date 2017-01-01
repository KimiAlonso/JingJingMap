package com.zbd.jingjingmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.route.BusPath;

import java.util.List;


public class BusPathListAdapter extends BaseAdapter {

    List <BusPath> pathList;
    Context mContext;

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

        String name = String.valueOf(pathList.get(position).getBusDistance());
        mViewHolder.busPathInfo.setText(name);




        return convertView;
    }
    public class MViewHolder{
        TextView busPathInfo;

    }
}
