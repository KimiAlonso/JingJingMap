package com.zbd.jingjingmap;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SettingsActivity  extends AppCompatActivity{

    Button locations;
    String city;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        city = getIntent().getStringExtra("city");
        locations = (Button) findViewById(R.id.location);

        locations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this,LocationListActivity.class);
                intent.putExtra("city",city);
                startActivity(intent);
            }
        });

    }
}
