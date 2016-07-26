package com.ghofrani.classapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Discard changes?");

            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {

                    dialog.dismiss();

                    callSuperOnBackPressed();

                }

            });

            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {

                    dialog.dismiss();

                }

            });

            builder.create().show();

            return true;

        } else {

            return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Discard changes?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();

                callSuperOnBackPressed();

            }

        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();

            }

        });

        builder.create().show();

    }

    private void callSuperOnBackPressed(){

        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_check, menu);

        return true;

    }

}