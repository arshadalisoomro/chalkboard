package com.ghofrani.classapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.modules.DataStore;
import com.ghofrani.classapp.modules.Utils;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Utils.setTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        toolbar.setTitle("Settings");
        toolbar.setElevation(getPixelFromDP(4));
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.settings_frame_layout, new SettingsFragment()).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            super.onBackPressed();

            return true;

        } else {

            return onOptionsItemSelected(menuItem);

        }

    }

    private int getPixelFromDP(float dPtoConvert) {

        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dPtoConvert * scale + 0.5f);

    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(final Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_settings);

        }

        @Override
        public void onResume() {

            super.onResume();

            getPreferenceScreen()
                    .getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);

        }

        @Override
        public void onPause() {

            getPreferenceScreen()
                    .getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);

            super.onPause();

        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            if (key.equals("detailed_notification") && DataStore.isCurrentClass
                    || key.equals("flip_colors") && DataStore.isCurrentClass
                    || key.equals("class_notification") && DataStore.isCurrentClass
                    || key.equals("next_class_notification_minutes") && DataStore.isNextClasses
                    || key.equals("tomorrow_classes")
                    || key.equals("24_hour_time")) {

                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("update_data"));

            } else if (key.equals("primary_color")
                    || key.equals("accent_color")) {

                DataStore.recreate = true;

                getActivity().recreate();

            }

        }

    }

}