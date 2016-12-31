package com.zbd.jingjingmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.zbd.jingjingmap.Database.CustomLocation;
import com.zbd.jingjingmap.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class LocationManagerActivity extends AppCompatActivity {

    ListView mListview;
    Context context;
    DatabaseHelper helper;
    CustomLocation customLocation;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationmanager);

        helper = new DatabaseHelper(context);
        mListview = (ListView)findViewById(R.id.locationlist);
        context = LocationManagerActivity.this;
        customLocation = new CustomLocation();

        List<String> mList  = new ArrayList<>();
        mList.add("a");
        mList.add("a");
        mList.add("a");
        mList.add("a");
        mList.add("a");
        mList.add("a");
        mList.add("a");
        LocationsListAdapter mAdapter = new LocationsListAdapter(mList,context);
        mListview.setAdapter(mAdapter);

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChooseLocationActivity.actionStart(context,"AA","BB");
            }
        });



    }
}
