package com.ghofrani.classapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ghofrani.classapp.R;

public class AddHomework extends AppCompatActivity {

    private boolean originNotification = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int colorPrimary = PreferenceManager.getDefaultSharedPreferences(this).getInt("primary_color", ContextCompat.getColor(this, R.color.teal));

        if (colorPrimary == ContextCompat.getColor(this, R.color.red))
            getTheme().applyStyle(R.style.primary_red, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.pink))
            getTheme().applyStyle(R.style.primary_pink, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.purple))
            getTheme().applyStyle(R.style.primary_purple, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.deep_purple))
            getTheme().applyStyle(R.style.primary_deep_purple, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.indigo))
            getTheme().applyStyle(R.style.primary_indigo, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.blue))
            getTheme().applyStyle(R.style.primary_blue, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.light_blue))
            getTheme().applyStyle(R.style.primary_light_blue, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.cyan))
            getTheme().applyStyle(R.style.primary_cyan, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.teal))
            getTheme().applyStyle(R.style.primary_teal, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.green))
            getTheme().applyStyle(R.style.primary_green, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.light_green))
            getTheme().applyStyle(R.style.primary_light_green, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.lime))
            getTheme().applyStyle(R.style.primary_lime, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.orange))
            getTheme().applyStyle(R.style.primary_orange, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.deep_orange))
            getTheme().applyStyle(R.style.primary_deep_orange, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.blue_grey))
            getTheme().applyStyle(R.style.primary_blue_grey, true);

        int colorAccent = PreferenceManager.getDefaultSharedPreferences(this).getInt("accent_color", ContextCompat.getColor(this, R.color.deep_orange_accent));

        if (colorAccent == ContextCompat.getColor(this, R.color.red_accent))
            getTheme().applyStyle(R.style.accent_red, true);
        else if (colorAccent == ContextCompat.getColor(this, R.color.pink_accent))
            getTheme().applyStyle(R.style.accent_pink, true);
        else if (colorAccent == ContextCompat.getColor(this, R.color.purple_accent))
            getTheme().applyStyle(R.style.accent_purple, true);
        else if (colorAccent == ContextCompat.getColor(this, R.color.deep_purple_accent))
            getTheme().applyStyle(R.style.accent_deep_purple, true);
        else if (colorAccent == ContextCompat.getColor(this, R.color.indigo_accent))
            getTheme().applyStyle(R.style.accent_indigo, true);
        else if (colorAccent == ContextCompat.getColor(this, R.color.blue_accent))
            getTheme().applyStyle(R.style.accent_blue, true);
        else if (colorAccent == ContextCompat.getColor(this, R.color.light_blue_accent))
            getTheme().applyStyle(R.style.accent_light_blue, true);
        else if (colorAccent == ContextCompat.getColor(this, R.color.cyan_accent))
            getTheme().applyStyle(R.style.accent_cyan, true);
        else if (colorAccent == ContextCompat.getColor(this, R.color.teal_accent))
            getTheme().applyStyle(R.style.accent_teal, true);
        else if (colorAccent == ContextCompat.getColor(this, R.color.green_accent))
            getTheme().applyStyle(R.style.accent_green, true);
        else if (colorAccent == ContextCompat.getColor(this, R.color.lime_accent))
            getTheme().applyStyle(R.style.accent_lime, true);
        else if (colorAccent == ContextCompat.getColor(this, R.color.yellow_accent))
            getTheme().applyStyle(R.style.accent_yellow, true);
        else if (colorAccent == ContextCompat.getColor(this, R.color.amber_accent))
            getTheme().applyStyle(R.style.accent_amber, true);
        else if (colorAccent == ContextCompat.getColor(this, R.color.orange_accent))
            getTheme().applyStyle(R.style.accent_orange, true);
        else if (colorAccent == ContextCompat.getColor(this, R.color.deep_orange_accent))
            getTheme().applyStyle(R.style.accent_deep_orange, true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_homework);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.add_homework_toolbar);
        toolbar.setTitle("Add Homework");
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra("origin_notification"))
            originNotification = true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Discard changes?");
            builder.setMessage("This homework will be deleted.");

            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {

                    dialog.dismiss();

                    if (originNotification) {

                        finish();
                        startActivity(new Intent(AddHomework.this, Main.class).putExtra("fragment", 3));

                    } else {

                        callSuperOnBackPressed();

                    }

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

            return super.onOptionsItemSelected(menuItem);

        }

    }

    @Override
    public void onBackPressed() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Discard changes?");
        builder.setMessage("This homework will be deleted.");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();

                if (originNotification) {

                    finish();
                    startActivity(new Intent(AddHomework.this, Main.class).putExtra("fragment", 3));

                } else {

                    callSuperOnBackPressed();

                }

            }

        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();

            }

        });

        builder.create().show();

    }

    private void callSuperOnBackPressed() {

        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_check, menu);

        return true;

    }

}