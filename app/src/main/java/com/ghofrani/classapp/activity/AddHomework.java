package com.ghofrani.classapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ghofrani.classapp.R;
import com.ghofrani.classapp.model.Homework;
import com.ghofrani.classapp.modules.DataStore;
import com.ghofrani.classapp.modules.DatabaseHelper;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import java.util.ArrayList;

public class AddHomework extends AppCompatActivity {

    private boolean originNotification = false;
    private Spinner classNameSpinner;
    private RadioButton nextClassRadioButton;
    private RadioButton specificClassRadioButton;
    private RadioButton customTimeRadioButton;
    private MutableDateTime pickedDateTime;
    private TextView homeworkNameTextView;

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
    protected void onResume() {

        super.onResume();

        if (pickedDateTime == null) {

            DateTime tomorrow = new DateTime().plusDays(1);

            pickedDateTime = new MutableDateTime();

            pickedDateTime.setYear(tomorrow.getYear());
            pickedDateTime.setMonthOfYear(tomorrow.getMonthOfYear());
            pickedDateTime.setDayOfMonth(tomorrow.getDayOfMonth());
            pickedDateTime.setTime(0, 0, 0, 0);

        }

        if (classNameSpinner == null) {

            classNameSpinner = (Spinner) findViewById(R.id.add_homework_class_spinner);

            ArrayList<String> allClassNamesArrayList;

            if (DataStore.isCurrentClass) {

                allClassNamesArrayList = DataStore.allClassNamesArrayList;
                allClassNamesArrayList.remove(DataStore.currentClass.getName());
                allClassNamesArrayList.add(0, DataStore.currentClass.getName());

            } else {

                allClassNamesArrayList = DataStore.allClassNamesArrayList;

            }

            final ArrayAdapter<String> classNameSpinnerAdapter = new ArrayAdapter<>(this, R.layout.view_spinner_item, allClassNamesArrayList);

            classNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            classNameSpinner.setAdapter(classNameSpinnerAdapter);

            classNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if (isClassInTimetable(adapterView.getSelectedItem().toString())) {

                        nextClassRadioButton.setEnabled(true);
                        specificClassRadioButton.setEnabled(true);

                    } else {

                        nextClassRadioButton.setEnabled(false);
                        specificClassRadioButton.setEnabled(false);

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }

            });

        }

        if (nextClassRadioButton == null)
            nextClassRadioButton = (RadioButton) findViewById(R.id.radio_next);

        if (specificClassRadioButton == null)
            specificClassRadioButton = (RadioButton) findViewById(R.id.radio_specific);

        if (customTimeRadioButton == null)
            customTimeRadioButton = (RadioButton) findViewById(R.id.radio_custom);

        if (homeworkNameTextView == null)
            homeworkNameTextView = (TextView) findViewById(R.id.add_homework_input_name);

    }

    private boolean isClassInTimetable(String className) {

        int i = 0;

        if (DataStore.sundayClasses != null) {

            while (i < DataStore.sundayClasses.size()) {

                if (className.equals(DataStore.sundayClasses.get(i).getName()))
                    return true;

                i++;

            }

            i = 0;

        }

        if (DataStore.mondayClasses != null) {

            while (i < DataStore.mondayClasses.size()) {

                if (className.equals(DataStore.mondayClasses.get(i).getName()))
                    return true;

                i++;

            }

            i = 0;

        }

        if (DataStore.tuesdayClasses != null) {

            while (i < DataStore.tuesdayClasses.size()) {

                if (className.equals(DataStore.tuesdayClasses.get(i).getName()))
                    return true;

                i++;

            }

            i = 0;

        }

        if (DataStore.wednesdayClasses != null) {

            while (i < DataStore.wednesdayClasses.size()) {

                if (className.equals(DataStore.wednesdayClasses.get(i).getName()))
                    return true;

                i++;

            }

            i = 0;

        }

        if (DataStore.thursdayClasses != null) {

            while (i < DataStore.thursdayClasses.size()) {

                if (className.equals(DataStore.thursdayClasses.get(i).getName()))
                    return true;

                i++;

            }

            i = 0;

        }

        if (DataStore.fridayClasses != null) {

            while (i < DataStore.fridayClasses.size()) {

                if (className.equals(DataStore.fridayClasses.get(i).getName()))
                    return true;

                i++;

            }

            i = 0;

        }

        if (DataStore.saturdayClasses != null) {

            while (i < DataStore.saturdayClasses.size()) {

                if (className.equals(DataStore.saturdayClasses.get(i).getName()))
                    return true;

                i++;

            }

        }

        return false;

    }

    public void onRadioButtonClicked(View view) {

        if (((RadioButton) view).isChecked()) {

            switch (view.getId()) {

                case R.id.radio_custom:

                    final View currentFocus = this.getCurrentFocus();

                    if (currentFocus != null) {

                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);

                    }

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            android.app.DatePickerDialog.OnDateSetListener onDateSetListener = new android.app.DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                                    pickedDateTime.setYear(year);
                                    pickedDateTime.setMonthOfYear(monthOfYear + 1);
                                    pickedDateTime.setDayOfMonth(dayOfMonth);
                                    pickedDateTime.setTime(0, 0, 0, 0);

                                    final DateTime now = DateTime.now();

                                    if (!pickedDateTime.isBefore(now.withTimeAtStartOfDay())) {

                                        android.app.TimePickerDialog.OnTimeSetListener onTimeSetListener = new android.app.TimePickerDialog.OnTimeSetListener() {

                                            @Override
                                            public void onTimeSet(TimePicker datePicker, int hourOfDay, int minuteOfHour) {

                                                pickedDateTime.setTime(hourOfDay, minuteOfHour, 0, 0);

                                                if (pickedDateTime.isBefore(now.withTime(now.getHourOfDay(), now.getMinuteOfHour(), 0, 0))) {

                                                    Toast.makeText(AddHomework.this, "Choose a time after now!", Toast.LENGTH_LONG).show();

                                                    pickedDateTime.setYear(now.plusDays(1).getYear());
                                                    pickedDateTime.setMonthOfYear(now.plusDays(1).getMonthOfYear());
                                                    pickedDateTime.setDayOfMonth(now.plusDays(1).getDayOfMonth());
                                                    pickedDateTime.setTime(0, 0, 0, 0);

                                                }

                                            }

                                        };

                                        new android.app.TimePickerDialog(AddHomework.this, onTimeSetListener, now.plusHours(1).getHourOfDay(), now.plusHours(1).getMinuteOfHour(), true).show();

                                    } else {

                                        pickedDateTime.setYear(now.plusDays(1).getYear());
                                        pickedDateTime.setMonthOfYear(now.plusDays(1).getMonthOfYear());
                                        pickedDateTime.setDayOfMonth(now.plusDays(1).getDayOfMonth());
                                        pickedDateTime.setTime(0, 0, 0, 0);

                                        Toast.makeText(AddHomework.this, "Choose a day after yesterday!", Toast.LENGTH_LONG).show();

                                    }

                                }

                            };

                            new android.app.DatePickerDialog(AddHomework.this, onDateSetListener, pickedDateTime.getYear(), pickedDateTime.getMonthOfYear() - 1, pickedDateTime.getDayOfMonth()).show();

                        }

                    }, 150);

                    break;

            }

        }

    }

    @Override
    public void onTrimMemory(int level) {

        if (level == TRIM_MEMORY_UI_HIDDEN) {

            classNameSpinner = null;
            nextClassRadioButton = null;
            specificClassRadioButton = null;
            customTimeRadioButton = null;
            pickedDateTime = null;
            homeworkNameTextView = null;

        }

        super.onTrimMemory(level);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            final MaterialDialog.Builder materialDialogBuilder = new MaterialDialog.Builder(this);

            materialDialogBuilder.title("Discard changes?");
            materialDialogBuilder.content("This homework will be deleted.");
            materialDialogBuilder.positiveText("YES");
            materialDialogBuilder.positiveColorRes(R.color.black);
            materialDialogBuilder.negativeText("CANCEL");
            materialDialogBuilder.negativeColorRes(R.color.black);

            materialDialogBuilder.onPositive(new MaterialDialog.SingleButtonCallback() {

                @Override
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                    materialDialog.dismiss();

                    if (originNotification) {

                        finish();
                        startActivity(new Intent(AddHomework.this, Main.class).putExtra("fragment", 3));

                    } else {

                        AddHomework.super.onBackPressed();

                    }

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

            if (!homeworkNameTextView.getText().toString().isEmpty()) {

                DatabaseHelper databaseHelper = new DatabaseHelper(this);

                if (nextClassRadioButton.isChecked()) {

                    //databaseHelper.addHomework(new Homework(homeworkNameTextView.getText().toString(), classNameSpinner.getSelectedItem().toString(), databaseHelper.getNextDateTimeOfClass(classNameSpinner.getSelectedItem().toString()), true, databaseHelper.getClassColor(classNameSpinner.getSelectedItem().toString())));

                } else if (specificClassRadioButton.isChecked()) {

                    //databaseHelper.addHomework(new Homework(homeworkNameTextView.getText().toString(), classNameSpinner.getSelectedItem().toString(), pickedDateTime.toDateTime(), true, databaseHelper.getClassColor(classNameSpinner.getSelectedItem().toString())));

                } else if (customTimeRadioButton.isChecked()) {

                    databaseHelper.addHomework(new Homework(homeworkNameTextView.getText().toString(), classNameSpinner.getSelectedItem().toString(), pickedDateTime.toDateTime(), false, databaseHelper.getClassColor(classNameSpinner.getSelectedItem().toString())));

                }

                databaseHelper.close();

                final View currentFocus = this.getCurrentFocus();

                if (currentFocus != null) {

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);

                }

                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("update_data"));

                finish();

            } else {

                Toast.makeText(this, "Please add a name!", Toast.LENGTH_LONG).show();

            }

            return true;

        } else {

            return super.onOptionsItemSelected(menuItem);

        }

    }

    @Override
    public void onBackPressed() {

        final MaterialDialog.Builder materialDialogBuilder = new MaterialDialog.Builder(this);

        materialDialogBuilder.title("Discard changes?");
        materialDialogBuilder.content("This homework will be deleted.");
        materialDialogBuilder.positiveText("YES");
        materialDialogBuilder.positiveColorRes(R.color.black);
        materialDialogBuilder.negativeText("CANCEL");
        materialDialogBuilder.negativeColorRes(R.color.black);

        materialDialogBuilder.onPositive(new MaterialDialog.SingleButtonCallback() {

            @Override
            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                materialDialog.dismiss();

                if (originNotification) {

                    finish();
                    startActivity(new Intent(AddHomework.this, Main.class).putExtra("fragment", 3));

                } else {

                    AddHomework.super.onBackPressed();

                }

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

}