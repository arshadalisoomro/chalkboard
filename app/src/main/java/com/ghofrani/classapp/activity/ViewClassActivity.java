package com.ghofrani.classapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ghofrani.classapp.R;

public class ViewClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class);

        Intent classIntent = getIntent();

        Toolbar homeworkToolbar = (Toolbar) findViewById(R.id.add_view_class_toolbar);
        homeworkToolbar.setTitle(classIntent.getExtras().getString("class"));
        homeworkToolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(homeworkToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

}