package com.zbd.jingjingmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbd.jingjingmap.Database.CustomLocation;

import java.util.List;



public class LocationListAdapter extends BaseAdapter {

    List<CustomLocation> mList;
    Context mContext;




    public LocationListAdapter(List list, Context context){
        mList = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_locationlist,null);
        mViewHolder.locationName = (TextView) convertView.findViewById(R.id.location_name);
        mViewHolder.locationDetail = (TextView) convertView.findViewById(R.id.location_detail);



        mViewHolder.locationName.setText(mList.get(position).getName());
        mViewHolder.locationDetail.setText(mList.get(position).getDetail());






        return convertView;
    }

    public class MViewHolder{
        TextView locationName;
        TextView locationDetail;
    }
}
