package com.ghofrani.classapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.modules.DatabaseHelper;

public class AddClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.add_class_toolbar);
        toolbar.setTitle("Add New Class");
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.add_class_color_frame_layout, new ColorFragment()).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Discard changes?");
            builder.setMessage("This class will be deleted.");

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

        } else if (menuItem.getItemId() == R.id.toolbar_check_check) {

            final EditText inputNameEditText = (EditText) findViewById(R.id.add_class_input_name);

            if (!inputNameEditText.getText().toString().isEmpty()) {

                DatabaseHelper databaseHelper = new DatabaseHelper(this);

                if (!databaseHelper.checkIfClassExists(inputNameEditText.getText().toString().trim())) {

                    final EditText inputTeacherEditText = (EditText) findViewById(R.id.add_class_input_teacher);
                    final EditText inputLocationEditText = (EditText) findViewById(R.id.add_class_input_location);

                    if (!inputTeacherEditText.getText().toString().isEmpty() && !inputLocationEditText.getText().toString().isEmpty()) {

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

                        final String classToAddInfo[] = new String[4];
                        classToAddInfo[0] = inputNameEditText.getText().toString().trim();
                        classToAddInfo[1] = inputTeacherEditText.getText().toString().trim();
                        classToAddInfo[2] = inputLocationEditText.getText().toString().trim();
                        classToAddInfo[3] = String.valueOf(sharedPreferences.getInt("add_class_color", getResources().getColor(R.color.green)));

                        if (databaseHelper.addClass(classToAddInfo)) {

                            databaseHelper.close();

                            final View view = this.getCurrentFocus();

                            if (view != null) {

                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                            }

                            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("update_classes"));

                            setResult(0, new Intent().putExtra("switch_to_timetable", 1));

                            finish();

                            return true;

                        } else {

                            databaseHelper.close();

                            Toast.makeText(this, "Error, try again!", Toast.LENGTH_LONG).show();

                            return true;

                        }

                    } else {

                        Toast.makeText(this, "Please add a teacher and location!", Toast.LENGTH_LONG).show();

                        return true;

                    }

                } else {

                    databaseHelper.close();

                    Toast.makeText(this, "A class with this name exists already!", Toast.LENGTH_LONG).show();

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

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Discard changes?");
        builder.setMessage("This class will be deleted.");

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

    private void callSuperOnBackPressed() {

        setResult(1, new Intent());

        super.onBackPressed();

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

            super.onPause();

            getPreferenceScreen()
                    .getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);

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