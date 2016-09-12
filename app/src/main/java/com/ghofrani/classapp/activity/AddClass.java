package com.ghofrani.classapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ghofrani.classapp.R;
import com.ghofrani.classapp.event.Update;
import com.ghofrani.classapp.model.SlimClass;
import com.ghofrani.classapp.module.DataSingleton;
import com.ghofrani.classapp.module.DatabaseHelper;
import com.ghofrani.classapp.module.Utils;

import org.greenrobot.eventbus.EventBus;

public class AddClass extends AppCompatActivity {

    private final int ID_TIMETABLE = 1;
    private final int RESULT_OK = 0;
    private final int RESULT_NO_CLASS_ADDED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Utils.setTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.add_class_toolbar);
        toolbar.setTitle("Add New Class");
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.add_class_color_frame_layout, new ColorFragment()).commit();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            final MaterialDialog.Builder materialDialogBuilder = new MaterialDialog.Builder(this);

            materialDialogBuilder.title("Discard changes?");
            materialDialogBuilder.content("This class will be deleted.");
            materialDialogBuilder.positiveText("YES");
            materialDialogBuilder.positiveColorRes(R.color.black);
            materialDialogBuilder.negativeText("CANCEL");
            materialDialogBuilder.negativeColorRes(R.color.black);

            materialDialogBuilder.onPositive(new MaterialDialog.SingleButtonCallback() {

                @Override
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                    materialDialog.dismiss();

                    setResult(RESULT_NO_CLASS_ADDED, new Intent());

                    AddClass.super.onBackPressed();

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

        } else if (menuItem.getItemId() == R.id.toolbar_check_check) {

            final EditText inputNameEditText = (EditText) findViewById(R.id.add_class_input_name);

            if (!inputNameEditText.getText().toString().isEmpty()) {

                DatabaseHelper databaseHelper = new DatabaseHelper(this);

                final EditText inputTeacherEditText = (EditText) findViewById(R.id.add_class_input_teacher);
                final EditText inputLocationEditText = (EditText) findViewById(R.id.add_class_input_location);

                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

                if (DataSingleton.getInstance().getAllClassNamesArrayList() != null) {

                    if (!DataSingleton.getInstance().getAllClassNamesArrayList().contains(inputNameEditText.getText().toString().trim())) {

                        try {

                            databaseHelper.addClass(new SlimClass(inputNameEditText.getText().toString().trim(),
                                    (inputLocationEditText.getText().toString().trim().isEmpty() ? "no-location" : inputLocationEditText.getText().toString().trim()),
                                    (inputTeacherEditText.getText().toString().trim().isEmpty() ? "no-teacher" : inputTeacherEditText.getText().toString().trim()),
                                    sharedPreferences.getInt("add_class_color", ContextCompat.getColor(this, R.color.teal))));

                        } finally {

                            databaseHelper.close();

                        }

                        EventBus.getDefault().post(new Update(false, false, false, true));

                        final View currentFocus = this.getCurrentFocus();

                        if (currentFocus != null) {

                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);

                        }

                        setResult(RESULT_OK, new Intent().putExtra("switch_to_timetable", ID_TIMETABLE).putExtra("class", "AddClass"));

                        finish();

                        return true;

                    } else {

                        databaseHelper.close();

                        Toast.makeText(this, "A class with this name exists already!", Toast.LENGTH_LONG).show();

                        return true;

                    }

                } else {

                    try {

                        databaseHelper.addClass(new SlimClass(inputNameEditText.getText().toString().trim(),
                                (inputLocationEditText.getText().toString().trim().isEmpty() ? "no-location" : inputLocationEditText.getText().toString().trim()),
                                (inputTeacherEditText.getText().toString().trim().isEmpty() ? "no-teacher" : inputTeacherEditText.getText().toString().trim()),
                                sharedPreferences.getInt("add_class_color", ContextCompat.getColor(this, R.color.teal))));

                    } finally {

                        databaseHelper.close();

                    }

                    EventBus.getDefault().post(new Update(false, false, false, true));

                    final View currentFocus = this.getCurrentFocus();

                    if (currentFocus != null) {

                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);

                    }

                    setResult(RESULT_OK, new Intent().putExtra("switch_to_timetable", ID_TIMETABLE).putExtra("class", "AddClass"));

                    finish();

                    return true;

                }

            } else {

                Toast.makeText(this, "Please add a name!", Toast.LENGTH_LONG).show();

                return true;

            }

        } else {

            return super.onOptionsItemSelected(menuItem);

        }

    }

    @Override
    public void onBackPressed() {

        final MaterialDialog.Builder materialDialogBuilder = new MaterialDialog.Builder(this);

        materialDialogBuilder.title("Discard changes?");
        materialDialogBuilder.content("This class will be deleted.");
        materialDialogBuilder.positiveText("YES");
        materialDialogBuilder.positiveColorRes(R.color.black);
        materialDialogBuilder.negativeText("CANCEL");
        materialDialogBuilder.negativeColorRes(R.color.black);

        materialDialogBuilder.onPositive(new MaterialDialog.SingleButtonCallback() {

            @Override
            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                materialDialog.dismiss();

                setResult(RESULT_NO_CLASS_ADDED, new Intent());

                AddClass.super.onBackPressed();

            }

        });

        materialDialogBuilder.onNegative(new MaterialDialog.SingleButtonCallback() {

            @Override
            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                materialDialog.dismiss();

            }

        });

        materialDialogBuilder.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_check, menu);

        return true;

    }

    public static class ColorFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(final Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_add_class);

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

            if (key.equals("add_class_color")) {

                setPreferenceScreen(null);
                addPreferencesFromResource(R.xml.preferences_add_class);

            }

        }

    }

}