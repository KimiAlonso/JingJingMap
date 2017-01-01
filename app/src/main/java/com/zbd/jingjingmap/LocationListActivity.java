package com.zbd.jingjingmap;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.j256.ormlite.dao.Dao;
import com.zbd.jingjingmap.Database.CustomLocation;
import com.zbd.jingjingmap.Database.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class LocationListActivity extends AppCompatActivity {

    ListView mListview;
    Context context;
    DatabaseHelper helper;
    CustomLocation customLocation;
    List <CustomLocation>tList = new ArrayList<>();
    LocationListAdapter mAdapter ;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationmanager);

        helper = DatabaseHelper.getHelper(LocationListActivity.this);
        mListview = (ListView)findViewById(R.id.locationlist);
        context = LocationListActivity.this;
        customLocation = new CustomLocation();

        try {
            Dao dao = helper.getDao(CustomLocation.class);
            tList = dao.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        mAdapter = new LocationListAdapter(tList,context);
        mListview.setAdapter(mAdapter);

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChooseLocationActivity.actionStart(context,tList.get(position).getName(),tList.get(position).getDetail());
            }
        });



    }

    @Override
    protected void onRestart() {
        initList();
        LocationListAdapter tAdapter = new LocationListAdapter(tList,context);
        mAdapter.notifyDataSetChanged();
        mListview.setAdapter(tAdapter);
        super.onRestart();
    }

//    @Override
//    protected void onStart() {
//
//
//        super.onStart();
//    }

    public void initList(){
        try {
            Dao dao = helper.getDao(CustomLocation.class);
            tList = dao.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
