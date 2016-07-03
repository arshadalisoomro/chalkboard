package com.ghofrani.classapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ghofrani.classapp.R;

public class HomeworkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);

        Toolbar homeworkToolbar = (Toolbar) findViewById(R.id.add_homework_toolbar);
        homeworkToolbar.setTitle("Add Homework");
        homeworkToolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(homeworkToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
            startActivity(new Intent(this, HomeActivity.class));

            return true;

        } else {

            return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onBackPressed() {

        finish();
        startActivity(new Intent(this, HomeActivity.class));

    }

}