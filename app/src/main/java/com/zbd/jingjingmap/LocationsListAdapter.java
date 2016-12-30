package com.zbd.jingjingmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Amarok on 2016/12/30.
 */

public class LocationsListAdapter extends BaseAdapter {

    List mList;
    Context mContext;

    public  LocationsListAdapter(List list, Context context){
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_locationmanager,null);
        mViewHolder.locationName = (TextView) convertView.findViewById(R.id.location_name);
        mViewHolder.locationDetail = (TextView) convertView.findViewById(R.id.location_detail);

        mViewHolder.locationName.setText("s");
        mViewHolder.locationDetail.setText("a");






        return convertView;
    }

    public class MViewHolder{
        TextView locationName;
        TextView locationDetail;
    }
}
