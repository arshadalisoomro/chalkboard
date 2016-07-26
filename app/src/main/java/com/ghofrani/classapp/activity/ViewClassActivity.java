package com.ghofrani.classapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ghofrani.classapp.R;
import com.woalk.apps.lib.colorpicker.ColorPickerDialog;
import com.woalk.apps.lib.colorpicker.ColorPickerSwatch;

public class ViewClassActivity extends AppCompatActivity {

    int mSelectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class);

        Intent classIntent = getIntent();

        Toolbar viewClassToolbar = (Toolbar) findViewById(R.id.view_class_toolbar);
        viewClassToolbar.setTitle(classIntent.getExtras().getString("class"));
        viewClassToolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(viewClassToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

}