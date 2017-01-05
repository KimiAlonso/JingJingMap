package com.zbd.jingjingmap;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
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
    Button add;
    String city;
    double startA;
    double startB;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationlist);

        city = getIntent().getStringExtra("city");
        startA = getIntent().getDoubleExtra("startA",0);
        startB = getIntent().getDoubleExtra("startB",0);
        helper = DatabaseHelper.getHelper(LocationListActivity.this);
        mListview = (ListView)findViewById(R.id.locationlist);
        context = LocationListActivity.this;
        customLocation = new CustomLocation();
        add  = (Button) findViewById(R.id.addlocation);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseLocationActivity.actionStart(context,"","",city);
            }
        });

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

                double endA = tList.get(position).getLatitude();
                double endB = tList.get(position).getLongitude();
                BusPathListActivity.actionStart(context,startA,startB,endA,endB,city);

            }
        });

        mListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showMDialog(position);
                return false;
            }
        });



    }

    public void showMDialog(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("编辑或删除");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHelper helper = new DatabaseHelper(context);
                try {
                    DeleteBuilder<CustomLocation, Integer> deleteBuilder = helper.getDao(CustomLocation.class).deleteBuilder();
                    deleteBuilder.where().eq("name",tList.get(position).getName());
                    deleteBuilder.delete();
                    tList.remove(position);
                    LocationListAdapter tAdapter = new LocationListAdapter(tList,context);
                    mListview.setAdapter(tAdapter);
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(context,"field",Toast.LENGTH_SHORT).show();
                }


                dialog.dismiss();
                helper.close();
            }
        });
        builder.setNegativeButton("编辑", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChooseLocationActivity.actionStart(context,tList.get(position).getName(),tList.get(position).getDetail(),city);
                dialog.dismiss();
            }
        });
        builder.create().show();


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
