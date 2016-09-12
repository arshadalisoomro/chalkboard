package com.ghofrani.classapp.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.event.Update;
import com.ghofrani.classapp.module.DataSingleton;
import com.ghofrani.classapp.module.Utils;

import org.greenrobot.eventbus.EventBus;

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

            if (key.equals("detailed_notification") && DataSingleton.getInstance().getCurrentClass() != null
                    || key.equals("flip_colors") && DataSingleton.getInstance().getCurrentClass() != null
                    || key.equals("class_notification") && DataSingleton.getInstance().getCurrentClass() != null
                    || key.equals("next_class_notification_minutes") && DataSingleton.getInstance().getCurrentClass() != null
                    || key.equals("tomorrow_classes")
                    || key.equals("24_hour_time")) {

                EventBus.getDefault().post(new Update(true, false, false, false));

            } else if (key.equals("primary_color")
                    || key.equals("accent_color")) {

                DataSingleton.getInstance().setRecreate(true);

                getActivity().recreate();

            }

        }

    }

}