package com.ghofrani.classapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.modules.DataStore;

public class Settings extends AppCompatActivity {

    private boolean resultSet = false;

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

            super.onPause();

            getPreferenceScreen()
                    .getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);

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