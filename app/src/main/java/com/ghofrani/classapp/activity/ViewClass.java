package com.ghofrani.classapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ghofrani.classapp.R;
import com.ghofrani.classapp.event.Update;
import com.ghofrani.classapp.module.DatabaseHelper;
import com.ghofrani.classapp.module.Utils;

import org.greenrobot.eventbus.EventBus;

public class ViewClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Utils.setTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.view_class_toolbar);
        toolbar.setTitle(getIntent().getStringExtra("class"));
        toolbar.setElevation(getPixelFromDP(4));
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            super.onBackPressed();

            return true;

        } else if (menuItem.getItemId() == R.id.toolbar_delete_edit_delete) {

            final MaterialDialog.Builder materialDialogBuilder = new MaterialDialog.Builder(this);

            materialDialogBuilder.title("Delete class?");
            materialDialogBuilder.content("All timetable entries and homework will be deleted for this class.");
            materialDialogBuilder.positiveText("YES");
            materialDialogBuilder.positiveColorRes(R.color.black);
            materialDialogBuilder.negativeText("CANCEL");
            materialDialogBuilder.negativeColorRes(R.color.black);

            materialDialogBuilder.onPositive(new MaterialDialog.SingleButtonCallback() {

                @Override
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                    materialDialog.dismiss();

                    DatabaseHelper databaseHelper = new DatabaseHelper(ViewClass.this);

                    try {

                        databaseHelper.deleteClass(getIntent().getStringExtra("class"));

                    } finally {

                        databaseHelper.close();

                    }

                    EventBus.getDefault().post(new Update(true, true, true, true));

                    ViewClass.super.onBackPressed();

                }

            });

            materialDialogBuilder.onNegative(new MaterialDialog.SingleButtonCallback() {

                @Override
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                    materialDialog.dismiss();

                }

            });

            materialDialogBuilder.show();

            return true;

        } else {

            return super.onOptionsItemSelected(menuItem);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_delete_edit, menu);

        return true;

    }

    private int getPixelFromDP(float dPtoConvert) {

        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dPtoConvert * scale + 0.5f);

    }

}