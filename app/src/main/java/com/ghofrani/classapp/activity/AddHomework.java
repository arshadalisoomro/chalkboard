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
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.modules.DataStore;
import com.ghofrani.classapp.modules.DatabaseHelper;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;

import java.util.ArrayList;
import java.util.Calendar;

public class AddHomework extends AppCompatActivity {

    private boolean originNotification = false;
    private Spinner classNameSpinner;
    private RadioButton nextClassRadioButton;
    private RadioButton specificClassRadioButton;
    private RadioButton customTimeRadioButton;
    private MutableDateTime pickedDateTime;
    private TextView homeworkNameTextView;
    private boolean firstRunSpecific = true;
    private int selectedIndex;
    private ArrayList<StandardClass> listItemClasses;
    private ArrayList<String> listItemTitles;
    private ArrayList<Integer> daySwitches;

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

                    if (specificClassRadioButton.isChecked())
                        specificClassRadioButton.setChecked(false);

                    if (isClassInTimetable(adapterView.getSelectedItem().toString())) {

                        nextClassRadioButton.setEnabled(true);
                        specificClassRadioButton.setEnabled(true);

                    } else {

                        nextClassRadioButton.setEnabled(false);
                        nextClassRadioButton.setChecked(false);

                        specificClassRadioButton.setEnabled(false);
                        specificClassRadioButton.setChecked(false);

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }

            });

        }

        if (listItemClasses == null)
            listItemClasses = new ArrayList<>();

        if (listItemTitles == null)
            listItemTitles = new ArrayList<>();

        if (daySwitches == null)
            daySwitches = new ArrayList<>();

        if (nextClassRadioButton == null)
            nextClassRadioButton = (RadioButton) findViewById(R.id.radio_next);

        if (DataStore.isCurrentClass)
            nextClassRadioButton.setChecked(true);

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

                case R.id.radio_specific:

                    listItemClasses.clear();
                    listItemTitles.clear();

                    if (DataStore.sundayClasses != null) {

                        for (int i = 0; i < DataStore.sundayClasses.size(); i++) {

                            if (DataStore.sundayClasses.get(i).getName().equals(classNameSpinner.getSelectedItem().toString())) {

                                listItemTitles.add("Sunday's class at " + DataStore.sundayClasses.get(i).getStartTimeString(true));
                                listItemClasses.add(DataStore.sundayClasses.get(i));

                            }

                        }

                    }

                    if (DataStore.mondayClasses != null) {

                        for (int i = 0; i < DataStore.mondayClasses.size(); i++) {

                            if (DataStore.mondayClasses.get(i).getName().equals(classNameSpinner.getSelectedItem().toString())) {

                                listItemTitles.add("Monday's class at " + DataStore.mondayClasses.get(i).getStartTimeString(true));
                                listItemClasses.add(DataStore.mondayClasses.get(i));

                            }

                        }

                    }

                    if (DataStore.tuesdayClasses != null) {

                        for (int i = 0; i < DataStore.tuesdayClasses.size(); i++) {

                            if (DataStore.tuesdayClasses.get(i).getName().equals(classNameSpinner.getSelectedItem().toString())) {

                                listItemTitles.add("Tuesday's class at " + DataStore.tuesdayClasses.get(i).getStartTimeString(true));
                                listItemClasses.add(DataStore.tuesdayClasses.get(i));

                            }

                        }

                    }

                    if (DataStore.wednesdayClasses != null) {

                        for (int i = 0; i < DataStore.wednesdayClasses.size(); i++) {

                            if (DataStore.wednesdayClasses.get(i).getName().equals(classNameSpinner.getSelectedItem().toString())) {

                                listItemTitles.add("Wednesday's class at " + DataStore.wednesdayClasses.get(i).getStartTimeString(true));
                                listItemClasses.add(DataStore.wednesdayClasses.get(i));

                            }

                        }

                    }

                    if (DataStore.thursdayClasses != null) {

                        for (int i = 0; i < DataStore.thursdayClasses.size(); i++) {

                            if (DataStore.thursdayClasses.get(i).getName().equals(classNameSpinner.getSelectedItem().toString())) {

                                listItemTitles.add("Thursday's class at " + DataStore.thursdayClasses.get(i).getStartTimeString(true));
                                listItemClasses.add(DataStore.thursdayClasses.get(i));

                            }

                        }

                    }

                    if (DataStore.fridayClasses != null) {

                        for (int i = 0; i < DataStore.fridayClasses.size(); i++) {

                            if (DataStore.fridayClasses.get(i).getName().equals(classNameSpinner.getSelectedItem().toString())) {

                                listItemTitles.add("Friday's class at " + DataStore.fridayClasses.get(i).getStartTimeString(true));
                                listItemClasses.add(DataStore.fridayClasses.get(i));

                            }

                        }

                    }

                    if (DataStore.saturdayClasses != null) {

                        for (int i = 0; i < DataStore.saturdayClasses.size(); i++) {

                            if (DataStore.saturdayClasses.get(i).getName().equals(classNameSpinner.getSelectedItem().toString())) {

                                listItemTitles.add("Saturday's class at " + DataStore.saturdayClasses.get(i).getStartTimeString(true));
                                listItemClasses.add(DataStore.saturdayClasses.get(i));

                            }

                        }

                    }

                    final String[] listItemsArray = new String[listItemTitles.size()];

                    for (int i = 0; i < listItemTitles.size(); i++)
                        listItemsArray[i] = listItemTitles.get(i);

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            new MaterialDialog.Builder(AddHomework.this)
                                    .title("Due next...")
                                    .items(listItemsArray)
                                    .itemsCallbackSingleChoice(firstRunSpecific ? 0 : selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {

                                        @Override
                                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                            firstRunSpecific = false;
                                            selectedIndex = which;

                                            return true;

                                        }

                                    })
                                    .show();

                        }

                    }, 100);

                    break;

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

                    }, 100);

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

    private DateTime getSundayDateTimeOfClass(String className, LocalTime now) {

        if (DataStore.sundayClasses != null) {

            for (int i = 0; i < DataStore.sundayClasses.size(); i++) {

                if (DataStore.sundayClasses.get(i).getName().equals(className)) {

                    if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

                        if (DataStore.sundayClasses.get(i).getStartTime().isAfter(now) || daySwitches.contains(Calendar.SUNDAY)) {

                            return new DateTime().withTime(DataStore.sundayClasses.get(i).getStartTime());

                        }

                    } else {

                        return new DateTime().withTime(DataStore.sundayClasses.get(i).getStartTime());

                    }

                }

            }

        }

        daySwitches.add(Calendar.SUNDAY);

        return getMondayDateTimeOfClass(className, now).plusDays(1);

    }

    private DateTime getMondayDateTimeOfClass(String className, LocalTime now) {

        if (DataStore.mondayClasses != null) {

            for (int i = 0; i < DataStore.mondayClasses.size(); i++) {

                if (DataStore.mondayClasses.get(i).getName().equals(className)) {

                    if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {

                        if (DataStore.mondayClasses.get(i).getStartTime().isAfter(now) || daySwitches.contains(Calendar.MONDAY)) {

                            return new DateTime().withTime(DataStore.mondayClasses.get(i).getStartTime());

                        }

                    } else {

                        return new DateTime().withTime(DataStore.mondayClasses.get(i).getStartTime());

                    }

                }

            }

        }

        daySwitches.add(Calendar.MONDAY);

        return getTuesdayDateTimeOfClass(className, now).plusDays(1);

    }

    private DateTime getTuesdayDateTimeOfClass(String className, LocalTime now) {

        if (DataStore.tuesdayClasses != null) {

            for (int i = 0; i < DataStore.tuesdayClasses.size(); i++) {

                if (DataStore.tuesdayClasses.get(i).getName().equals(className)) {

                    if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {

                        if (DataStore.tuesdayClasses.get(i).getStartTime().isAfter(now) || daySwitches.contains(Calendar.TUESDAY)) {

                            return new DateTime().withTime(DataStore.tuesdayClasses.get(i).getStartTime());

                        }

                    } else {

                        return new DateTime().withTime(DataStore.tuesdayClasses.get(i).getStartTime());

                    }

                }

            }

        }

        daySwitches.add(Calendar.TUESDAY);

        return getWednesdayDateTimeOfClass(className, now).plusDays(1);

    }

    private DateTime getWednesdayDateTimeOfClass(String className, LocalTime now) {

        if (DataStore.wednesdayClasses != null) {

            for (int i = 0; i < DataStore.wednesdayClasses.size(); i++) {

                if (DataStore.wednesdayClasses.get(i).getName().equals(className)) {

                    if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {

                        if (DataStore.wednesdayClasses.get(i).getStartTime().isAfter(now) || daySwitches.contains(Calendar.WEDNESDAY)) {

                            return new DateTime().withTime(DataStore.wednesdayClasses.get(i).getStartTime());

                        }

                    } else {

                        return new DateTime().withTime(DataStore.wednesdayClasses.get(i).getStartTime());

                    }

                }

            }

        }

        daySwitches.add(Calendar.WEDNESDAY);

        return getThursdayDateTimeOfClass(className, now).plusDays(1);

    }

    private DateTime getThursdayDateTimeOfClass(String className, LocalTime now) {

        if (DataStore.thursdayClasses != null) {

            for (int i = 0; i < DataStore.thursdayClasses.size(); i++) {

                if (DataStore.thursdayClasses.get(i).getName().equals(className)) {

                    if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {

                        if (DataStore.thursdayClasses.get(i).getStartTime().isAfter(now) || daySwitches.contains(Calendar.THURSDAY)) {

                            return new DateTime().withTime(DataStore.thursdayClasses.get(i).getStartTime());

                        }

                    } else {

                        return new DateTime().withTime(DataStore.thursdayClasses.get(i).getStartTime());

                    }

                }

            }

        }

        daySwitches.add(Calendar.THURSDAY);

        return getFridayDateTimeOfClass(className, now).plusDays(1);

    }

    private DateTime getFridayDateTimeOfClass(String className, LocalTime now) {

        if (DataStore.fridayClasses != null) {

            for (int i = 0; i < DataStore.fridayClasses.size(); i++) {

                if (DataStore.fridayClasses.get(i).getName().equals(className)) {

                    if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {

                        if (DataStore.fridayClasses.get(i).getStartTime().isAfter(now) || daySwitches.contains(Calendar.FRIDAY)) {

                            return new DateTime().withTime(DataStore.fridayClasses.get(i).getStartTime());

                        }

                    } else {

                        return new DateTime().withTime(DataStore.fridayClasses.get(i).getStartTime());

                    }

                }

            }

        }

        daySwitches.add(Calendar.FRIDAY);

        return getSaturdayDateTimeOfClass(className, now).plusDays(1);

    }

    private DateTime getSaturdayDateTimeOfClass(String className, LocalTime now) {

        if (DataStore.saturdayClasses != null) {

            for (int i = 0; i < DataStore.saturdayClasses.size(); i++) {

                if (DataStore.saturdayClasses.get(i).getName().equals(className)) {

                    if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

                        if (DataStore.saturdayClasses.get(i).getStartTime().isAfter(now) || daySwitches.contains(Calendar.SATURDAY)) {

                            return new DateTime().withTime(DataStore.saturdayClasses.get(i).getStartTime());

                        }

                    } else {

                        return new DateTime().withTime(DataStore.saturdayClasses.get(i).getStartTime());

                    }

                }

            }

        }

        daySwitches.add(Calendar.SATURDAY);

        return getSundayDateTimeOfClass(className, now).plusDays(1);

    }

    private DateTime getNextDateTimeOfClass(String className) {

        LocalTime now = LocalTime.now();

        switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {

            case Calendar.SUNDAY:

                return getSundayDateTimeOfClass(className, now);

            case Calendar.MONDAY:

                return getMondayDateTimeOfClass(className, now);

            case Calendar.TUESDAY:

                return getTuesdayDateTimeOfClass(className, now);

            case Calendar.WEDNESDAY:

                return getWednesdayDateTimeOfClass(className, now);

            case Calendar.THURSDAY:

                return getThursdayDateTimeOfClass(className, now);

            case Calendar.FRIDAY:

                return getFridayDateTimeOfClass(className, now);

            case Calendar.SATURDAY:

                return getSaturdayDateTimeOfClass(className, now);

        }

        return new DateTime();

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
                Homework homeworkToAdd;

                if (nextClassRadioButton.isChecked()) {

                    homeworkToAdd = new Homework(homeworkNameTextView.getText().toString(), classNameSpinner.getSelectedItem().toString(), getNextDateTimeOfClass(classNameSpinner.getSelectedItem().toString()), true, databaseHelper.getClassColor(classNameSpinner.getSelectedItem().toString()));

                } else if (specificClassRadioButton.isChecked()) {

                    DateTime pickedDateTimeSpecific = new DateTime();
                    int plusDays = 0;

                    if (listItemTitles.get(selectedIndex).startsWith("Sun")) {

                        if (pickedDateTimeSpecific.getDayOfWeek() == 7) {

                            plusDays = 7;

                        } else {

                            plusDays = 7 - pickedDateTimeSpecific.getDayOfWeek();

                        }

                    } else if (listItemTitles.get(selectedIndex).startsWith("Mon")) {

                        plusDays = 8 - pickedDateTimeSpecific.getDayOfWeek();

                    } else if (listItemTitles.get(selectedIndex).startsWith("Tue")) {

                        if (pickedDateTimeSpecific.getDayOfWeek() == 2) {

                            plusDays = 7;

                        } else if (pickedDateTimeSpecific.getDayOfWeek() < 2) {

                            plusDays = 2 - pickedDateTimeSpecific.getDayOfWeek();

                        } else {

                            plusDays = 9 - pickedDateTimeSpecific.getDayOfWeek();

                        }

                    } else if (listItemTitles.get(selectedIndex).startsWith("Wed")) {

                        if (pickedDateTimeSpecific.getDayOfWeek() == 3) {

                            plusDays = 7;

                        } else if (pickedDateTimeSpecific.getDayOfWeek() < 3) {

                            plusDays = 3 - pickedDateTimeSpecific.getDayOfWeek();

                        } else {

                            plusDays = 10 - pickedDateTimeSpecific.getDayOfWeek();

                        }

                    } else if (listItemTitles.get(selectedIndex).startsWith("Thu")) {

                        if (pickedDateTimeSpecific.getDayOfWeek() == 4) {

                            plusDays = 7;

                        } else if (pickedDateTimeSpecific.getDayOfWeek() < 4) {

                            plusDays = 4 - pickedDateTimeSpecific.getDayOfWeek();

                        } else {

                            plusDays = 11 - pickedDateTimeSpecific.getDayOfWeek();

                        }

                    } else if (listItemTitles.get(selectedIndex).startsWith("Fri")) {

                        if (pickedDateTimeSpecific.getDayOfWeek() == 5) {

                            plusDays = 7;

                        } else if (pickedDateTimeSpecific.getDayOfWeek() < 5) {

                            plusDays = 5 - pickedDateTimeSpecific.getDayOfWeek();

                        } else {

                            plusDays = 12 - pickedDateTimeSpecific.getDayOfWeek();

                        }

                    } else if (listItemTitles.get(selectedIndex).startsWith("Sat")) {

                        if (pickedDateTimeSpecific.getDayOfWeek() == 6) {

                            plusDays = 7;

                        } else if (pickedDateTimeSpecific.getDayOfWeek() < 6) {

                            plusDays = 6 - pickedDateTimeSpecific.getDayOfWeek();

                        } else {

                            plusDays = 12 - pickedDateTimeSpecific.getDayOfWeek();

                        }

                    }

                    homeworkToAdd = new Homework(homeworkNameTextView.getText().toString(), classNameSpinner.getSelectedItem().toString(), pickedDateTimeSpecific.plusDays(plusDays).withTime(listItemClasses.get(selectedIndex).getStartTime()), true, databaseHelper.getClassColor(classNameSpinner.getSelectedItem().toString()));

                } else if (customTimeRadioButton.isChecked()) {

                    homeworkToAdd = new Homework(homeworkNameTextView.getText().toString(), classNameSpinner.getSelectedItem().toString(), pickedDateTime.toDateTime(), false, databaseHelper.getClassColor(classNameSpinner.getSelectedItem().toString()));

                } else {

                    databaseHelper.close();

                    Toast.makeText(this, "Please select a due date!", Toast.LENGTH_LONG).show();

                    return true;

                }

                boolean inserted = false;
                int index;

                ArrayList<Homework> homeworkArrayList = new ArrayList<>();

                homeworkArrayList.addAll(DataStore.todayHomeworkArrayList);
                homeworkArrayList.addAll(DataStore.tomorrowHomeworkArrayList);
                homeworkArrayList.addAll(DataStore.thisWeekHomeworkArrayList);
                homeworkArrayList.addAll(DataStore.nextWeekHomeworkArrayList);
                homeworkArrayList.addAll(DataStore.thisMonthHomeworkArrayList);
                homeworkArrayList.addAll(DataStore.beyondThisMonthHomeworkArrayList);

                if (!homeworkArrayList.isEmpty()) {

                    index = 0;

                    while (!inserted && index < homeworkArrayList.size()) {

                        if (index == 0) {

                            if (homeworkToAdd.getDateTime().isBefore(homeworkArrayList.get(index).getDateTime()) || homeworkToAdd.getDateTime().isEqual(homeworkArrayList.get(index).getDateTime())) {

                                inserted = true;
                                homeworkArrayList.add(0, homeworkToAdd);

                            } else if (homeworkArrayList.size() == 1) {

                                inserted = true;
                                homeworkArrayList.add(homeworkToAdd);

                            }

                        } else if (homeworkToAdd.getDateTime().isBefore(homeworkArrayList.get(index).getDateTime()) || homeworkToAdd.getDateTime().isEqual(homeworkArrayList.get(index).getDateTime())) {

                            homeworkArrayList.add(index, homeworkToAdd);
                            inserted = true;

                        } else if (index + 1 == homeworkArrayList.size()) {

                            homeworkArrayList.add(homeworkToAdd);
                            inserted = true;

                        }

                        index++;

                    }

                } else {

                    homeworkArrayList.add(homeworkToAdd);

                }

                databaseHelper.deleteAllHomework();
                databaseHelper.addHomework(homeworkArrayList);

                final ArrayList<Homework> todayHomeworkArrayList = new ArrayList<>();
                final ArrayList<Homework> tomorrowHomeworkArrayList = new ArrayList<>();
                final ArrayList<Homework> thisWeekHomeworkArrayList = new ArrayList<>();
                final ArrayList<Homework> nextWeekHomeworkArrayList = new ArrayList<>();
                final ArrayList<Homework> thisMonthHomeworkArrayList = new ArrayList<>();
                final ArrayList<Homework> beyondThisMonthHomeworkArrayList = new ArrayList<>();

                DateTime today = DateTime.now();
                DateTime tomorrow = today.plusDays(1);

                boolean thisWeekEnabled = true;
                Interval thisWeek;
                Interval nextWeek;

                switch (today.getDayOfWeek()) {

                    case DateTimeConstants.FRIDAY:

                        thisWeekEnabled = false;
                        thisWeek = new Interval(today, today);
                        nextWeek = new Interval(today.plusDays(2).withTime(0, 0, 0, 0), today.plusDays(9).withTime(0, 0, 0, 0));

                        break;

                    case DateTimeConstants.SATURDAY:

                        thisWeekEnabled = false;
                        thisWeek = new Interval(today, today);
                        nextWeek = new Interval(today.plusDays(1).withTime(0, 0, 0, 0), today.plusDays(8).withTime(0, 0, 0, 0));

                        break;

                    default:

                        thisWeek = new Interval(tomorrow.plusDays(1).withTime(0, 0, 0, 0), tomorrow.plusDays(DateTimeConstants.SUNDAY - tomorrow.getDayOfWeek()).withTime(0, 0, 0, 0));
                        nextWeek = new Interval(thisWeek.getEnd(), thisWeek.getEnd().plusDays(7).withTimeAtStartOfDay());

                }

                boolean thisMonthEnabled = false;
                Interval thisMonth = new Interval(today, today);

                if (nextWeek.getEnd().getMonthOfYear() == today.getMonthOfYear()) {

                    if (today.dayOfMonth().withMaximumValue().getDayOfMonth() > nextWeek.getEnd().getDayOfMonth() - 1) {

                        thisMonthEnabled = true;
                        thisMonth = new Interval(nextWeek.getEnd(), today.dayOfMonth().withMaximumValue());

                    }

                }

                for (int i = 0; i < homeworkArrayList.size(); i++) {

                    Homework homework = homeworkArrayList.get(i);

                    if (today.withTimeAtStartOfDay().isEqual(homework.getDateTime().withTimeAtStartOfDay())) {

                        todayHomeworkArrayList.add(homework);

                    } else if (tomorrow.withTimeAtStartOfDay().isEqual(homework.getDateTime().withTimeAtStartOfDay())) {

                        tomorrowHomeworkArrayList.add(homework);

                    } else if (thisWeek.contains(homework.getDateTime()) && thisWeekEnabled) {

                        thisWeekHomeworkArrayList.add(homework);

                    } else if (nextWeek.contains(homework.getDateTime())) {

                        nextWeekHomeworkArrayList.add(homework);

                    } else if (thisMonth.contains(homework.getDateTime()) && thisMonthEnabled) {

                        thisMonthHomeworkArrayList.add(homework);

                    } else {

                        beyondThisMonthHomeworkArrayList.add(homework);

                    }

                }

                DataStore.todayHomeworkArrayList = todayHomeworkArrayList;
                DataStore.tomorrowHomeworkArrayList = tomorrowHomeworkArrayList;
                DataStore.thisWeekHomeworkArrayList = thisWeekHomeworkArrayList;
                DataStore.nextWeekHomeworkArrayList = nextWeekHomeworkArrayList;
                DataStore.thisMonthHomeworkArrayList = thisMonthHomeworkArrayList;
                DataStore.beyondThisMonthHomeworkArrayList = beyondThisMonthHomeworkArrayList;

                databaseHelper.close();

                final View currentFocus = this.getCurrentFocus();

                if (currentFocus != null) {

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);

                }

                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("update_data").putExtra("skip_homework", ""));

                if (originNotification) {

                    finish();
                    startActivity(new Intent(AddHomework.this, Main.class).putExtra("fragment", 3));

                } else {

                    AddHomework.super.onBackPressed();

                }

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