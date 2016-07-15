package com.ghofrani.classapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ghofrani.classapp.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar settingsToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        settingsToolbar.setTitle("Settings");
        settingsToolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(settingsToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.settings_frame_layout, new SettingsFragment()).commit();

    }

    @Override
    public void onBackPressed() {

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent("updateData"));

        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent("updateData"));

        return super.onOptionsItemSelected(item);

    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(final Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

        }

    }

}