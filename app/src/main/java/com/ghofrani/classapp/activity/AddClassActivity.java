package com.ghofrani.classapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.ghofrani.classapp.R;

public class AddClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        Toolbar addClassToolbar = (Toolbar) findViewById(R.id.add_class_toolbar);
        addClassToolbar.setTitle("Add Class");
        addClassToolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(addClassToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_add_class, menu);

        return true;

    }

}