package com.ghofrani.classapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ghofrani.classapp.R;

public class ViewClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_class_toolbar);
        toolbar.setTitle(getIntent().getExtras().getString("class"));
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

}