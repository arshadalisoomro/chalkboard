package com.ghofrani.classapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.ghofrani.classapp.R;
import com.ghofrani.classapp.adapter.EditDayList;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.modules.DataStore;
import com.ghofrani.classapp.modules.DatabaseHelper;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class EditDay extends AppCompatActivity {

    private String startTimeTextAdd;
    private String endTimeTextAdd;
    private String startTimeTextEdit;
    private String endTimeTextEdit;
    private ListView listView;
    private EditDayList listAdapter;
    private ArrayList<StandardClass> standardClassArrayList;
    private List<String> startTimeStringForPosition;
    private List<String> endTimeStringForPosition;
    private List<Integer> noClassIndexList;
    private DateTimeFormatter dateTimeFormatterAMPM;
    private DateTimeFormatter dateTimeFormatter24Hour;
    private DateTimeFormatter dateTimeFormatter24HourNoColon;
    private boolean is24Hour;
    private int day;

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
        setContentView(R.layout.activity_edit_day);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.edit_day_toolbar);

        switch (getIntent().getIntExtra("day", 0)) {

            case 0:
                toolbar.setTitle("Edit Sunday");
                break;
            case 1:
                toolbar.setTitle("Edit Monday");
                break;
            case 2:
                toolbar.setTitle("Edit Tuesday");
                break;
            case 3:
                toolbar.setTitle("Edit Wednesday");
                break;
            case 4:
                toolbar.setTitle("Edit Thursday");
                break;
            case 5:
                toolbar.setTitle("Edit Friday");
                break;
            case 6:
                toolbar.setTitle("Edit Saturday");
                break;

        }

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setElevation(getPixelFromDP(4));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        day = getIntent().getIntExtra("day", 0) + 1;

    }

    @Override
    protected void onResume() {

        super.onResume();

        if (listView == null)
            listView = (ListView) findViewById(R.id.edit_day_list_view);

        if (standardClassArrayList == null)
            standardClassArrayList = new ArrayList<>();

        if (noClassIndexList == null)
            noClassIndexList = new ArrayList<>();

        if (startTimeStringForPosition == null)
            startTimeStringForPosition = new ArrayList<>();

        if (endTimeStringForPosition == null)
            endTimeStringForPosition = new ArrayList<>();

        if (dateTimeFormatterAMPM == null)
            dateTimeFormatterAMPM = DateTimeFormat.forPattern("h:mm a");

        if (dateTimeFormatter24Hour == null)
            dateTimeFormatter24Hour = DateTimeFormat.forPattern("HH:mm");

        if (dateTimeFormatter24HourNoColon == null)
            dateTimeFormatter24HourNoColon = DateTimeFormat.forPattern("HHmm");

        updateUI();

    }

    @Override
    public void onTrimMemory(int level) {

        if (level == TRIM_MEMORY_UI_HIDDEN) {

            listView = null;
            listAdapter = null;
            standardClassArrayList = null;

            startTimeStringForPosition = null;
            endTimeStringForPosition = null;
            noClassIndexList = null;

            dateTimeFormatterAMPM = null;
            dateTimeFormatter24Hour = null;
            dateTimeFormatter24HourNoColon = null;

        }

        super.onTrimMemory(level);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            super.onBackPressed();

            return true;

        } else {

            return super.onOptionsItemSelected(menuItem);

        }

    }

    private void setClassesArrayListOfDay(int day, ArrayList<StandardClass> standardClassArrayList) {

        switch (day) {

            case 1:

                DataStore.sundayClasses = standardClassArrayList;

                break;

            case 2:

                DataStore.mondayClasses = standardClassArrayList;

                break;

            case 3:

                DataStore.tuesdayClasses = standardClassArrayList;

                break;

            case 4:

                DataStore.wednesdayClasses = standardClassArrayList;

                break;

            case 5:

                DataStore.thursdayClasses = standardClassArrayList;

                break;

            case 6:

                DataStore.fridayClasses = standardClassArrayList;

                break;

            case 7:

                DataStore.saturdayClasses = standardClassArrayList;

                break;

        }

    }

    private ArrayList<StandardClass> getClassesArrayListOfDay(int day) {

        switch (day) {

            case 1:

                return DataStore.sundayClasses;

            case 2:

                return DataStore.mondayClasses;

            case 3:

                return DataStore.tuesdayClasses;

            case 4:

                return DataStore.wednesdayClasses;

            case 5:

                return DataStore.thursdayClasses;

            case 6:

                return DataStore.fridayClasses;

            case 7:

                return DataStore.saturdayClasses;

            default:

                return new ArrayList<>();

        }

    }

    private void updateUI() {

        noClassIndexList.clear();
        startTimeStringForPosition.clear();
        endTimeStringForPosition.clear();

        is24Hour = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("24_hour_time", true);

        if (getClassesArrayListOfDay(day) != null) {

            standardClassArrayList.clear();
            standardClassArrayList.addAll(getClassesArrayListOfDay(day));

            for (int i = 0; i <= standardClassArrayList.size(); i++) {

                if (i == 0) {

                    if (!standardClassArrayList.get(0).getStartTimeString(false).equals("00:00")) {

                        standardClassArrayList.add(0, null);
                        noClassIndexList.add(0);

                        String firstStartTimeHourString = standardClassArrayList.get(1).getStartTimeString(false).substring(0, 2);

                        if ((Integer.parseInt(firstStartTimeHourString) - 1) < 0) {

                            startTimeStringForPosition.add("00:00");

                        } else {

                            int firstStartTimeHourInteger = Integer.parseInt(firstStartTimeHourString) - 1;
                            String finalFirstStartTimeHourString = (firstStartTimeHourInteger < 10) ? ("0" + String.valueOf(firstStartTimeHourInteger)) : String.valueOf(firstStartTimeHourInteger);
                            startTimeStringForPosition.add(finalFirstStartTimeHourString + standardClassArrayList.get(1).getStartTimeString(false).substring(2));

                        }

                        endTimeStringForPosition.add(standardClassArrayList.get(1).getStartTimeString(false));

                    } else {

                        startTimeStringForPosition.add("00:00");
                        endTimeStringForPosition.add(i, standardClassArrayList.get(i).getEndTimeString(false));

                    }

                } else if (i == standardClassArrayList.size()) {

                    if (!standardClassArrayList.get(i - 1).getEndTimeString(false).equals("23:59")) {

                        standardClassArrayList.add(null);
                        noClassIndexList.add(i);

                        startTimeStringForPosition.add(standardClassArrayList.get(i - 1).getEndTimeString(false));

                        String lastStartTimeHourString = standardClassArrayList.get(i - 1).getEndTimeString(false).substring(0, 2);

                        if ((Integer.parseInt(lastStartTimeHourString) + 1) > 23) {

                            endTimeStringForPosition.add("23:59");

                        } else {

                            int lastStartTimeHourInteger = Integer.parseInt(lastStartTimeHourString) + 1;
                            String finalLastStartTimeHourString = (lastStartTimeHourInteger < 10) ? ("0" + String.valueOf(lastStartTimeHourInteger)) : String.valueOf(lastStartTimeHourInteger);
                            endTimeStringForPosition.add(finalLastStartTimeHourString + standardClassArrayList.get(i - 1).getEndTimeString(false).substring(2));

                        }

                    }

                    i++;

                } else if (i > 1 && !standardClassArrayList.get(i).getStartTimeString(false).equals(standardClassArrayList.get(i - 1).getEndTimeString(false))) {

                    startTimeStringForPosition.add(i, standardClassArrayList.get(i).getStartTimeString(false));
                    endTimeStringForPosition.add(i, standardClassArrayList.get(i).getEndTimeString(false));

                    standardClassArrayList.add(i, null);
                    noClassIndexList.add(i);

                    startTimeStringForPosition.add(i, standardClassArrayList.get(i - 1).getEndTimeString(false));
                    endTimeStringForPosition.add(i, standardClassArrayList.get(i + 1).getStartTimeString(false));

                    i++;

                } else {

                    startTimeStringForPosition.add(i, standardClassArrayList.get(i).getStartTimeString(false));
                    endTimeStringForPosition.add(i, standardClassArrayList.get(i).getEndTimeString(false));

                }

            }

        } else {

            standardClassArrayList = new ArrayList<>();
            standardClassArrayList.add(null);

            noClassIndexList.add(0);
            startTimeStringForPosition.add("08:00");
            endTimeStringForPosition.add("09:00");

        }

        if (listAdapter == null) {

            listAdapter = new EditDayList(this, standardClassArrayList);
            listView.setAdapter(listAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    if (noClassIndexList.contains(position)) {

                        final String startTimeRestriction;
                        final String endTimeRestriction;

                        if (standardClassArrayList.size() == 1) {

                            startTimeRestriction = "";
                            endTimeRestriction = "";

                        } else if (position == 0) {

                            startTimeRestriction = "";
                            endTimeRestriction = endTimeStringForPosition.get(position);

                        } else if ((position + 1) == standardClassArrayList.size()) {

                            startTimeRestriction = startTimeStringForPosition.get(position);
                            endTimeRestriction = "";

                        } else {

                            startTimeRestriction = startTimeStringForPosition.get(position);
                            endTimeRestriction = endTimeStringForPosition.get(position);

                        }

                        final MaterialDialog.Builder materialDialogBuilderAdd = new MaterialDialog.Builder(EditDay.this);

                        materialDialogBuilderAdd.title("Add Class");
                        materialDialogBuilderAdd.customView(R.layout.dialog_edit_day_add_class, false);
                        materialDialogBuilderAdd.positiveText("Done");
                        materialDialogBuilderAdd.positiveColorRes(R.color.black);
                        materialDialogBuilderAdd.negativeText("Edit Times");
                        materialDialogBuilderAdd.negativeColorRes(R.color.black);
                        materialDialogBuilderAdd.autoDismiss(false);

                        final MaterialDialog materialDialogAdd = materialDialogBuilderAdd.show();

                        materialDialogBuilderAdd.onPositive(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                                final Spinner classNameSpinner = (Spinner) materialDialog.getCustomView().findViewById(R.id.dialog_edit_day_add_class_spinner);
                                final String className = classNameSpinner.getSelectedItem().toString();

                                final TextView classStartTimeTextView = (TextView) materialDialog.getCustomView().findViewById(R.id.dialog_edit_day_add_class_start_time);
                                final TextView classEndTimeTextView = (TextView) materialDialog.getCustomView().findViewById(R.id.dialog_edit_day_add_class_end_time);

                                String classStartTime;
                                String classEndTime;

                                if (is24Hour) {

                                    classStartTime = classStartTimeTextView.getText().toString().replace(":", "");
                                    classEndTime = classEndTimeTextView.getText().toString().replace(":", "");

                                } else {

                                    classStartTime = dateTimeFormatter24HourNoColon.print(dateTimeFormatterAMPM.parseLocalTime(classStartTimeTextView.getText().toString()));
                                    classEndTime = dateTimeFormatter24HourNoColon.print(dateTimeFormatterAMPM.parseLocalTime(classEndTimeTextView.getText().toString()));

                                }

                                DatabaseHelper databaseHelper = new DatabaseHelper(EditDay.this);
                                ArrayList<StandardClass> currentClasses;

                                if (getClassesArrayListOfDay(day) != null) {

                                    currentClasses = getClassesArrayListOfDay(day);

                                    boolean inserted = false;
                                    int index = 0;

                                    while (!inserted) {

                                        if (classStartTime.equals(currentClasses.get(index).getEndTimeString(false).replace(":", ""))) {

                                            inserted = true;

                                            currentClasses.add(index + 1, new StandardClass(className,
                                                    dateTimeFormatter24HourNoColon.parseLocalTime(classStartTime),
                                                    dateTimeFormatter24HourNoColon.parseLocalTime(classEndTime),
                                                    is24Hour,
                                                    dateTimeFormatterAMPM,
                                                    databaseHelper.getClassLocation(className),
                                                    databaseHelper.getClassTeacher(className),
                                                    databaseHelper.getClassColor(className)));

                                        } else if (Integer.parseInt(classStartTime) < Integer.parseInt(currentClasses.get(index).getStartTimeString(false).replace(":", ""))) {

                                            inserted = true;

                                            currentClasses.add(index, new StandardClass(className,
                                                    dateTimeFormatter24HourNoColon.parseLocalTime(classStartTime),
                                                    dateTimeFormatter24HourNoColon.parseLocalTime(classEndTime),
                                                    is24Hour,
                                                    dateTimeFormatterAMPM,
                                                    databaseHelper.getClassLocation(className),
                                                    databaseHelper.getClassTeacher(className),
                                                    databaseHelper.getClassColor(className)));

                                        } else if ((currentClasses.size() - index) == 1) {

                                            inserted = true;

                                            currentClasses.add(new StandardClass(className,
                                                    dateTimeFormatter24HourNoColon.parseLocalTime(classStartTime),
                                                    dateTimeFormatter24HourNoColon.parseLocalTime(classEndTime),
                                                    is24Hour,
                                                    dateTimeFormatterAMPM,
                                                    databaseHelper.getClassLocation(className),
                                                    databaseHelper.getClassTeacher(className),
                                                    databaseHelper.getClassColor(className)));

                                        }

                                        index++;

                                    }

                                    databaseHelper.deleteAllClassesOfDay(day);

                                    databaseHelper.insertClassesIntoDayStandard(currentClasses, day);

                                } else {

                                    currentClasses = new ArrayList<>();
                                    currentClasses.add(new StandardClass(className,
                                            dateTimeFormatter24HourNoColon.parseLocalTime(classStartTime),
                                            dateTimeFormatter24HourNoColon.parseLocalTime(classEndTime),
                                            is24Hour,
                                            dateTimeFormatterAMPM,
                                            databaseHelper.getClassLocation(className),
                                            databaseHelper.getClassTeacher(className),
                                            databaseHelper.getClassColor(className)));

                                    databaseHelper.insertClassesIntoDayStandard(currentClasses, day);

                                }

                                databaseHelper.close();

                                setClassesArrayListOfDay(day, currentClasses);

                                materialDialog.dismiss();

                                updateUI();

                            }

                        });

                        materialDialogBuilderAdd.onNegative(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                                startTimeTextAdd = "";
                                endTimeTextAdd = "";

                                final TextView dialogAddClassStartTimeTextView = (TextView) materialDialog.getCustomView().findViewById(R.id.dialog_edit_day_add_class_start_time);
                                final TextView dialogAddClassEndTimeTextView = (TextView) materialDialog.getCustomView().findViewById(R.id.dialog_edit_day_add_class_end_time);

                                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {

                                        if (hourOfDay > hourOfDayEnd) {

                                            Toast.makeText(EditDay.this, "Your end time is before your start time!", Toast.LENGTH_LONG).show();

                                        } else if (hourOfDay == hourOfDayEnd) {

                                            if (minute == minuteEnd) {

                                                Toast.makeText(EditDay.this, "Your start time equals your end time!", Toast.LENGTH_LONG).show();

                                            } else {

                                                if (startTimeRestriction.isEmpty()) {

                                                    String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                    String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                    if (is24Hour) {

                                                        startTimeTextAdd = hourOfDayString + ":" + minuteString;

                                                    } else {

                                                        final LocalTime startTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayString + ":" + minuteString);
                                                        startTimeTextAdd = dateTimeFormatterAMPM.print(startTime);

                                                    }

                                                } else {

                                                    int startRestrictionHourOfDay = Integer.parseInt(startTimeRestriction.substring(0, 2));

                                                    if (hourOfDay < startRestrictionHourOfDay) {

                                                        Toast.makeText(EditDay.this, "Choose a start time after the previous class!", Toast.LENGTH_LONG).show();

                                                    } else if (hourOfDay == startRestrictionHourOfDay) {

                                                        int startRestrictionMinute = Integer.parseInt(startTimeRestriction.substring(3));

                                                        if (minute < startRestrictionMinute) {

                                                            Toast.makeText(EditDay.this, "Choose a start time after the previous class!", Toast.LENGTH_LONG).show();

                                                        } else {

                                                            String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                            String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                            if (is24Hour) {

                                                                startTimeTextAdd = hourOfDayString + ":" + minuteString;

                                                            } else {

                                                                final LocalTime startTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayString + ":" + minuteString);
                                                                startTimeTextAdd = dateTimeFormatterAMPM.print(startTime);

                                                            }

                                                        }

                                                    } else {

                                                        String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                        String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                        if (is24Hour) {

                                                            startTimeTextAdd = hourOfDayString + ":" + minuteString;

                                                        } else {

                                                            final LocalTime startTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayString + ":" + minuteString);
                                                            startTimeTextAdd = dateTimeFormatterAMPM.print(startTime);

                                                        }

                                                    }

                                                }

                                                if (!startTimeTextAdd.isEmpty()) {

                                                    if (endTimeRestriction.isEmpty()) {

                                                        String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                        String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                        if (is24Hour) {

                                                            endTimeTextAdd = hourOfDayEndString + ":" + minuteEndString;

                                                        } else {

                                                            final LocalTime endTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayEndString + ":" + minuteEndString);
                                                            endTimeTextAdd = dateTimeFormatterAMPM.print(endTime);

                                                        }

                                                    } else {

                                                        int endRestrictionHourOfDay = Integer.parseInt(endTimeRestriction.substring(0, 2));

                                                        if (hourOfDayEnd > endRestrictionHourOfDay) {

                                                            Toast.makeText(EditDay.this, "Choose an end time before the next class!", Toast.LENGTH_LONG).show();

                                                        } else if (hourOfDayEnd == endRestrictionHourOfDay) {

                                                            int endRestrictionMinute = Integer.parseInt(endTimeRestriction.substring(3));

                                                            if (minuteEnd > endRestrictionMinute) {

                                                                Toast.makeText(EditDay.this, "Choose an end time before the next class!", Toast.LENGTH_LONG).show();

                                                            } else {

                                                                String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                                String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                                if (is24Hour) {

                                                                    endTimeTextAdd = hourOfDayEndString + ":" + minuteEndString;

                                                                } else {

                                                                    final LocalTime endTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayEndString + ":" + minuteEndString);
                                                                    endTimeTextAdd = dateTimeFormatterAMPM.print(endTime);

                                                                }

                                                            }

                                                        } else {

                                                            String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                            String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                            if (is24Hour) {

                                                                endTimeTextAdd = hourOfDayEndString + ":" + minuteEndString;

                                                            } else {

                                                                final LocalTime endTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayEndString + ":" + minuteEndString);
                                                                endTimeTextAdd = dateTimeFormatterAMPM.print(endTime);

                                                            }

                                                        }

                                                    }

                                                    if (!endTimeTextAdd.isEmpty()) {

                                                        dialogAddClassStartTimeTextView.setText(startTimeTextAdd);
                                                        dialogAddClassEndTimeTextView.setText(endTimeTextAdd);

                                                    }

                                                }

                                            }

                                        } else {

                                            if (startTimeRestriction.isEmpty()) {

                                                String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                if (is24Hour) {

                                                    startTimeTextAdd = hourOfDayString + ":" + minuteString;

                                                } else {

                                                    final LocalTime startTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayString + ":" + minuteString);
                                                    startTimeTextAdd = dateTimeFormatterAMPM.print(startTime);

                                                }

                                            } else {

                                                int startRestrictionHourOfDay = Integer.parseInt(startTimeRestriction.substring(0, 2));

                                                if (hourOfDay < startRestrictionHourOfDay) {

                                                    Toast.makeText(EditDay.this, "Choose a start time after the previous class!", Toast.LENGTH_LONG).show();

                                                } else if (hourOfDay == startRestrictionHourOfDay) {

                                                    int startRestrictionMinute = Integer.parseInt(startTimeRestriction.substring(3));

                                                    if (minute < startRestrictionMinute) {

                                                        Toast.makeText(EditDay.this, "Choose a start time after the previous class!", Toast.LENGTH_LONG).show();

                                                    } else {

                                                        String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                        String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                        if (is24Hour) {

                                                            startTimeTextAdd = hourOfDayString + ":" + minuteString;

                                                        } else {

                                                            final LocalTime startTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayString + ":" + minuteString);
                                                            startTimeTextAdd = dateTimeFormatterAMPM.print(startTime);

                                                        }

                                                    }

                                                } else {

                                                    String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                    String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                    if (is24Hour) {

                                                        startTimeTextAdd = hourOfDayString + ":" + minuteString;

                                                    } else {

                                                        final LocalTime startTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayString + ":" + minuteString);
                                                        startTimeTextAdd = dateTimeFormatterAMPM.print(startTime);

                                                    }

                                                }

                                            }

                                            if (!startTimeTextAdd.isEmpty()) {

                                                if (endTimeRestriction.isEmpty()) {

                                                    String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                    String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                    if (is24Hour) {

                                                        endTimeTextAdd = hourOfDayEndString + ":" + minuteEndString;

                                                    } else {

                                                        final LocalTime endTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayEndString + ":" + minuteEndString);
                                                        endTimeTextAdd = dateTimeFormatterAMPM.print(endTime);

                                                    }

                                                } else {

                                                    int endRestrictionHourOfDay = Integer.parseInt(endTimeRestriction.substring(0, 2));

                                                    if (hourOfDayEnd > endRestrictionHourOfDay) {

                                                        Toast.makeText(EditDay.this, "Choose an end time before the next class!", Toast.LENGTH_LONG).show();

                                                    } else if (hourOfDayEnd == endRestrictionHourOfDay) {

                                                        int endRestrictionMinute = Integer.parseInt(endTimeRestriction.substring(3));

                                                        if (minuteEnd > endRestrictionMinute) {

                                                            Toast.makeText(EditDay.this, "Choose an end time before the next class!", Toast.LENGTH_LONG).show();

                                                        } else {

                                                            String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                            String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                            if (is24Hour) {

                                                                endTimeTextAdd = hourOfDayEndString + ":" + minuteEndString;

                                                            } else {

                                                                final LocalTime endTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayEndString + ":" + minuteEndString);
                                                                endTimeTextAdd = dateTimeFormatterAMPM.print(endTime);

                                                            }

                                                        }

                                                    } else {

                                                        String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                        String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                        if (is24Hour) {

                                                            endTimeTextAdd = hourOfDayEndString + ":" + minuteEndString;

                                                        } else {

                                                            final LocalTime endTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayEndString + ":" + minuteEndString);
                                                            endTimeTextAdd = dateTimeFormatterAMPM.print(endTime);

                                                        }

                                                    }

                                                }

                                                if (!endTimeTextAdd.isEmpty()) {

                                                    dialogAddClassStartTimeTextView.setText(startTimeTextAdd);
                                                    dialogAddClassEndTimeTextView.setText(endTimeTextAdd);

                                                }

                                            }

                                        }

                                    }

                                };

                                if (is24Hour) {

                                    String[] splitStartTimeString = dialogAddClassStartTimeTextView.getText().toString().split(":");

                                    final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(

                                            onTimeSetListener,
                                            Integer.parseInt(splitStartTimeString[0]),
                                            Integer.parseInt(splitStartTimeString[1].substring(0, 2)),
                                            true

                                    );

                                    timePickerDialog.show(getFragmentManager(), "time_picker_dialog");

                                } else {

                                    String startTimeString = dateTimeFormatter24Hour.print(dateTimeFormatterAMPM.parseLocalTime(dialogAddClassStartTimeTextView.getText().toString()));

                                    final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(

                                            onTimeSetListener,
                                            Integer.parseInt(startTimeString.substring(0, 2)),
                                            Integer.parseInt(startTimeString.substring(3)),
                                            false

                                    );

                                    timePickerDialog.show(getFragmentManager(), "time_picker_dialog");

                                }

                            }

                        });

                        final Spinner classNameSpinner = (Spinner) materialDialogAdd.getCustomView().findViewById(R.id.dialog_edit_day_add_class_spinner);

                        final ArrayAdapter<String> classNameSpinnerAdapter = new ArrayAdapter<>(EditDay.this, android.R.layout.simple_spinner_item, DataStore.allClassNamesArrayList);

                        classNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        classNameSpinner.setAdapter(classNameSpinnerAdapter);

                        final TextView dialogAddClassStartTimeTextView = (TextView) materialDialogAdd.getCustomView().findViewById(R.id.dialog_edit_day_add_class_start_time);
                        final TextView dialogAddClassEndTimeTextView = (TextView) materialDialogAdd.getCustomView().findViewById(R.id.dialog_edit_day_add_class_end_time);

                        if (is24Hour) {

                            dialogAddClassStartTimeTextView.setText(startTimeStringForPosition.get(position));
                            dialogAddClassEndTimeTextView.setText(endTimeStringForPosition.get(position));

                        } else {

                            final LocalTime startTime = dateTimeFormatter24Hour.parseLocalTime(startTimeStringForPosition.get(position));
                            final LocalTime endTime = dateTimeFormatter24Hour.parseLocalTime(endTimeStringForPosition.get(position));

                            dialogAddClassStartTimeTextView.setText(dateTimeFormatterAMPM.print(startTime));
                            dialogAddClassEndTimeTextView.setText(dateTimeFormatterAMPM.print(endTime));

                        }

                    } else {

                        final String startTimeRestriction;
                        final String endTimeRestriction;

                        if (standardClassArrayList.size() == 1) {

                            startTimeRestriction = "";
                            endTimeRestriction = "";

                        } else if (position == 0) {

                            startTimeRestriction = "";

                            if (noClassIndexList.contains(position + 1)) {

                                if ((position + 2) == standardClassArrayList.size()) {

                                    endTimeRestriction = "";

                                } else {

                                    endTimeRestriction = endTimeStringForPosition.get(position + 1);

                                }

                            } else {

                                endTimeRestriction = endTimeStringForPosition.get(position);

                            }

                        } else if (position == 1 && noClassIndexList.contains(0)) {

                            startTimeRestriction = "";

                            if (noClassIndexList.contains(position + 1)) {

                                if (position + 2 == standardClassArrayList.size()) {

                                    endTimeRestriction = "";

                                } else {

                                    endTimeRestriction = endTimeStringForPosition.get(position + 1);

                                }

                            } else {

                                endTimeRestriction = endTimeStringForPosition.get(position);

                            }

                        } else if ((position + 1) == standardClassArrayList.size()) {

                            if (noClassIndexList.contains(position - 1)) {

                                if ((position - 1) == 0) {

                                    startTimeRestriction = "";

                                } else {

                                    startTimeRestriction = startTimeStringForPosition.get(position - 1);

                                }

                            } else {

                                startTimeRestriction = startTimeStringForPosition.get(position);

                            }

                            endTimeRestriction = "";

                        } else if ((position + 2) == standardClassArrayList.size() && noClassIndexList.contains(position + 1)) {

                            if (noClassIndexList.contains(position - 1)) {

                                if ((position - 1) == 0) {

                                    startTimeRestriction = "";

                                } else {

                                    startTimeRestriction = startTimeStringForPosition.get(position - 1);

                                }

                            } else {

                                startTimeRestriction = startTimeStringForPosition.get(position);

                            }

                            endTimeRestriction = "";

                        } else {

                            if (noClassIndexList.contains(position - 1))
                                startTimeRestriction = startTimeStringForPosition.get(position - 1);
                            else
                                startTimeRestriction = startTimeStringForPosition.get(position);

                            if (noClassIndexList.contains(position + 1))
                                endTimeRestriction = endTimeStringForPosition.get(position + 1);
                            else
                                endTimeRestriction = endTimeStringForPosition.get(position);

                        }

                        final TextView startTimeTextView = (TextView) view.findViewById(R.id.view_edit_day_list_child_time);
                        final LocalTime selectedClassStartTime;

                        if (is24Hour) {

                            selectedClassStartTime = dateTimeFormatter24HourNoColon.parseLocalTime(startTimeTextView.getText().toString().substring(0, 5).replace(":", ""));

                        } else {

                            String[] selectedClassStartTimeArray = startTimeTextView.getText().toString().split(" - ");
                            selectedClassStartTime = dateTimeFormatterAMPM.parseLocalTime(selectedClassStartTimeArray[0]);

                        }

                        final TextView titleTextView = (TextView) view.findViewById(R.id.view_edit_day_list_child_text);
                        final String selectedClassName = titleTextView.getText().toString();

                        ArrayList<String> classNameList = DataStore.allClassNamesArrayList;
                        classNameList.remove(selectedClassName);
                        classNameList.add(0, selectedClassName);

                        final MaterialDialog.Builder materialDialogBuilderEditClass = new MaterialDialog.Builder(EditDay.this);

                        materialDialogBuilderEditClass.title("Edit Class");
                        materialDialogBuilderEditClass.customView(R.layout.dialog_edit_day_edit_class, false);
                        materialDialogBuilderEditClass.positiveText("Done");
                        materialDialogBuilderEditClass.positiveColorRes(R.color.black);
                        materialDialogBuilderEditClass.negativeText("Edit Times");
                        materialDialogBuilderEditClass.negativeColorRes(R.color.black);
                        materialDialogBuilderEditClass.neutralText("Delete");
                        materialDialogBuilderEditClass.neutralColorRes(R.color.black);
                        materialDialogBuilderEditClass.autoDismiss(false);

                        final MaterialDialog materialDialogEditClass = materialDialogBuilderEditClass.show();

                        final TextView dialogEditClassStartTimeTextView = (TextView) materialDialogEditClass.getCustomView().findViewById(R.id.dialog_edit_day_edit_class_start_time);
                        final TextView dialogEditClassEndTimeTextView = (TextView) materialDialogEditClass.getCustomView().findViewById(R.id.dialog_edit_day_edit_class_end_time);

                        if (is24Hour) {

                            dialogEditClassStartTimeTextView.setText(startTimeStringForPosition.get(position));
                            dialogEditClassEndTimeTextView.setText(endTimeStringForPosition.get(position));

                        } else {

                            final LocalTime startTime = dateTimeFormatter24Hour.parseLocalTime(startTimeStringForPosition.get(position));
                            final LocalTime endTime = dateTimeFormatter24Hour.parseLocalTime(endTimeStringForPosition.get(position));

                            dialogEditClassStartTimeTextView.setText(dateTimeFormatterAMPM.print(startTime));
                            dialogEditClassEndTimeTextView.setText(dateTimeFormatterAMPM.print(endTime));

                        }

                        final Spinner classNameSpinner = (Spinner) materialDialogEditClass.getCustomView().findViewById(R.id.dialog_edit_day_edit_class_spinner);

                        final ArrayAdapter<String> classNameSpinnerAdapter = new ArrayAdapter<>(EditDay.this, android.R.layout.simple_spinner_item, classNameList);

                        classNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        classNameSpinner.setAdapter(classNameSpinnerAdapter);

                        materialDialogBuilderEditClass.onPositive(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                                final Spinner classNameSpinner = (Spinner) materialDialog.getCustomView().findViewById(R.id.dialog_edit_day_edit_class_spinner);
                                String className = classNameSpinner.getSelectedItem().toString();

                                final TextView classStartTimeTextView = (TextView) materialDialog.getCustomView().findViewById(R.id.dialog_edit_day_edit_class_start_time);
                                final TextView classEndTimeTextView = (TextView) materialDialog.getCustomView().findViewById(R.id.dialog_edit_day_edit_class_end_time);

                                String classStartTime;
                                String classEndTime;

                                if (is24Hour) {

                                    classStartTime = classStartTimeTextView.getText().toString().replace(":", "");
                                    classEndTime = classEndTimeTextView.getText().toString().replace(":", "");

                                } else {

                                    classStartTime = dateTimeFormatter24HourNoColon.print(dateTimeFormatterAMPM.parseLocalTime(classStartTimeTextView.getText().toString()));
                                    classEndTime = dateTimeFormatter24HourNoColon.print(dateTimeFormatterAMPM.parseLocalTime(classEndTimeTextView.getText().toString()));

                                }

                                DatabaseHelper databaseHelper = new DatabaseHelper(EditDay.this);
                                ArrayList<StandardClass> currentClasses = getClassesArrayListOfDay(day);

                                boolean inserted = false;
                                int index = 0;

                                boolean is24Hour = PreferenceManager.getDefaultSharedPreferences(EditDay.this).getBoolean("24_hour_time", true);

                                while (!inserted) {

                                    if (classStartTime.equals(currentClasses.get(index).getStartTimeString(false).replace(":", ""))) {

                                        inserted = true;
                                        currentClasses.remove(index);

                                        currentClasses.add(index, new StandardClass(className,
                                                dateTimeFormatter24HourNoColon.parseLocalTime(classStartTime),
                                                dateTimeFormatter24HourNoColon.parseLocalTime(classEndTime),
                                                is24Hour,
                                                dateTimeFormatterAMPM,
                                                databaseHelper.getClassLocation(className),
                                                databaseHelper.getClassTeacher(className),
                                                databaseHelper.getClassColor(className)));

                                    } else if (classStartTime.equals(currentClasses.get(index).getEndTimeString(false).replace(":", ""))) {

                                        inserted = true;
                                        currentClasses.remove(index + 1);

                                        currentClasses.add(index + 1, new StandardClass(className,
                                                dateTimeFormatter24HourNoColon.parseLocalTime(classStartTime),
                                                dateTimeFormatter24HourNoColon.parseLocalTime(classEndTime),
                                                is24Hour,
                                                dateTimeFormatterAMPM,
                                                databaseHelper.getClassLocation(className),
                                                databaseHelper.getClassTeacher(className),
                                                databaseHelper.getClassColor(className)));

                                    } else if (Integer.parseInt(classStartTime) < Integer.parseInt(currentClasses.get(index).getStartTimeString(false).replace(":", ""))) {

                                        inserted = true;
                                        currentClasses.remove(index);

                                        currentClasses.add(index, new StandardClass(className,
                                                dateTimeFormatter24HourNoColon.parseLocalTime(classStartTime),
                                                dateTimeFormatter24HourNoColon.parseLocalTime(classEndTime),
                                                is24Hour,
                                                dateTimeFormatterAMPM,
                                                databaseHelper.getClassLocation(className),
                                                databaseHelper.getClassTeacher(className),
                                                databaseHelper.getClassColor(className)));

                                    } else if ((currentClasses.size() - index) == 1) {

                                        inserted = true;
                                        currentClasses.remove(currentClasses.size() - 1);

                                        currentClasses.add(new StandardClass(className,
                                                dateTimeFormatter24HourNoColon.parseLocalTime(classStartTime),
                                                dateTimeFormatter24HourNoColon.parseLocalTime(classEndTime),
                                                is24Hour,
                                                dateTimeFormatterAMPM,
                                                databaseHelper.getClassLocation(className),
                                                databaseHelper.getClassTeacher(className),
                                                databaseHelper.getClassColor(className)));

                                    }

                                    index++;

                                }

                                databaseHelper.deleteAllClassesOfDay(day);

                                databaseHelper.insertClassesIntoDayStandard(currentClasses, day);

                                databaseHelper.close();

                                setClassesArrayListOfDay(day, currentClasses);

                                materialDialog.dismiss();

                                updateUI();

                            }

                        });

                        materialDialogBuilderEditClass.onNegative(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                                startTimeTextEdit = "";
                                endTimeTextEdit = "";

                                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {

                                        if (hourOfDay > hourOfDayEnd) {

                                            Toast.makeText(EditDay.this, "Your end time is before your start time!", Toast.LENGTH_LONG).show();

                                        } else if (hourOfDay == hourOfDayEnd) {

                                            if (minute == minuteEnd) {

                                                Toast.makeText(EditDay.this, "Your start time equals your end time!", Toast.LENGTH_LONG).show();

                                            } else {

                                                if (startTimeRestriction.isEmpty()) {

                                                    String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                    String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                    if (is24Hour) {

                                                        startTimeTextEdit = hourOfDayString + ":" + minuteString;

                                                    } else {

                                                        final LocalTime startTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayString + ":" + minuteString);
                                                        startTimeTextEdit = dateTimeFormatterAMPM.print(startTime);

                                                    }

                                                } else {

                                                    int startRestrictionHourOfDay = Integer.parseInt(startTimeRestriction.substring(0, 2));

                                                    if (hourOfDay < startRestrictionHourOfDay) {

                                                        Toast.makeText(EditDay.this, "Choose a start time after the previous class!", Toast.LENGTH_LONG).show();

                                                    } else if (hourOfDay == startRestrictionHourOfDay) {

                                                        int startRestrictionMinute = Integer.parseInt(startTimeRestriction.substring(3));

                                                        if (minute < startRestrictionMinute) {

                                                            Toast.makeText(EditDay.this, "Choose a start time after the previous class!", Toast.LENGTH_LONG).show();

                                                        } else {

                                                            String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                            String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                            if (is24Hour) {

                                                                startTimeTextEdit = hourOfDayString + ":" + minuteString;

                                                            } else {

                                                                final LocalTime startTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayString + ":" + minuteString);
                                                                startTimeTextEdit = dateTimeFormatterAMPM.print(startTime);

                                                            }

                                                        }

                                                    } else {

                                                        String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                        String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                        if (is24Hour) {

                                                            startTimeTextEdit = hourOfDayString + ":" + minuteString;

                                                        } else {

                                                            final LocalTime startTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayString + ":" + minuteString);
                                                            startTimeTextEdit = dateTimeFormatterAMPM.print(startTime);

                                                        }

                                                    }

                                                }

                                                if (!startTimeTextEdit.isEmpty()) {

                                                    if (endTimeRestriction.isEmpty()) {

                                                        String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                        String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                        if (is24Hour) {

                                                            endTimeTextEdit = hourOfDayEndString + ":" + minuteEndString;

                                                        } else {

                                                            final LocalTime endTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayEndString + ":" + minuteEndString);
                                                            endTimeTextEdit = dateTimeFormatterAMPM.print(endTime);

                                                        }

                                                    } else {

                                                        int endRestrictionHourOfDay = Integer.parseInt(endTimeRestriction.substring(0, 2));

                                                        if (hourOfDayEnd > endRestrictionHourOfDay) {

                                                            Toast.makeText(EditDay.this, "Choose an end time before the next class!", Toast.LENGTH_LONG).show();

                                                        } else if (hourOfDayEnd == endRestrictionHourOfDay) {

                                                            int endRestrictionMinute = Integer.parseInt(endTimeRestriction.substring(3));

                                                            if (minuteEnd > endRestrictionMinute) {

                                                                Toast.makeText(EditDay.this, "Choose an end time before the next class!", Toast.LENGTH_LONG).show();

                                                            } else {

                                                                String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                                String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                                if (is24Hour) {

                                                                    endTimeTextEdit = hourOfDayEndString + ":" + minuteEndString;

                                                                } else {

                                                                    final LocalTime endTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayEndString + ":" + minuteEndString);
                                                                    endTimeTextEdit = dateTimeFormatterAMPM.print(endTime);

                                                                }

                                                            }

                                                        } else {

                                                            String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                            String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                            if (is24Hour) {

                                                                endTimeTextEdit = hourOfDayEndString + ":" + minuteEndString;

                                                            } else {

                                                                final LocalTime endTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayEndString + ":" + minuteEndString);
                                                                endTimeTextEdit = dateTimeFormatterAMPM.print(endTime);

                                                            }

                                                        }

                                                    }

                                                    if (!endTimeTextEdit.isEmpty()) {

                                                        dialogEditClassStartTimeTextView.setText(startTimeTextEdit);
                                                        dialogEditClassEndTimeTextView.setText(endTimeTextEdit);

                                                    }

                                                }

                                            }

                                        } else {

                                            if (startTimeRestriction.isEmpty()) {

                                                String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                if (is24Hour) {

                                                    startTimeTextEdit = hourOfDayString + ":" + minuteString;

                                                } else {

                                                    final LocalTime startTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayString + ":" + minuteString);
                                                    startTimeTextEdit = dateTimeFormatterAMPM.print(startTime);

                                                }

                                            } else {

                                                int startRestrictionHourOfDay = Integer.parseInt(startTimeRestriction.substring(0, 2));

                                                if (hourOfDay < startRestrictionHourOfDay) {

                                                    Toast.makeText(EditDay.this, "Choose a start time after the previous class!", Toast.LENGTH_LONG).show();

                                                } else if (hourOfDay == startRestrictionHourOfDay) {

                                                    int startRestrictionMinute = Integer.parseInt(startTimeRestriction.substring(3));

                                                    if (minute < startRestrictionMinute) {

                                                        Toast.makeText(EditDay.this, "Choose a start time after the previous class!", Toast.LENGTH_LONG).show();

                                                    } else {

                                                        String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                        String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                        if (is24Hour) {

                                                            startTimeTextEdit = hourOfDayString + ":" + minuteString;

                                                        } else {

                                                            final LocalTime startTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayString + ":" + minuteString);
                                                            startTimeTextEdit = dateTimeFormatterAMPM.print(startTime);

                                                        }

                                                    }

                                                } else {

                                                    String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                    String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                    if (is24Hour) {

                                                        startTimeTextEdit = hourOfDayString + ":" + minuteString;

                                                    } else {

                                                        final LocalTime startTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayString + ":" + minuteString);
                                                        startTimeTextEdit = dateTimeFormatterAMPM.print(startTime);

                                                    }

                                                }

                                            }

                                            if (!startTimeTextEdit.isEmpty()) {

                                                if (endTimeRestriction.isEmpty()) {

                                                    String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                    String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                    if (is24Hour) {

                                                        endTimeTextEdit = hourOfDayEndString + ":" + minuteEndString;

                                                    } else {

                                                        final LocalTime endTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayEndString + ":" + minuteEndString);
                                                        endTimeTextEdit = dateTimeFormatterAMPM.print(endTime);

                                                    }

                                                } else {

                                                    int endRestrictionHourOfDay = Integer.parseInt(endTimeRestriction.substring(0, 2));

                                                    if (hourOfDayEnd > endRestrictionHourOfDay) {

                                                        Toast.makeText(EditDay.this, "Choose an end time before the next class!", Toast.LENGTH_LONG).show();

                                                    } else if (hourOfDayEnd == endRestrictionHourOfDay) {

                                                        int endRestrictionMinute = Integer.parseInt(endTimeRestriction.substring(3));

                                                        if (minuteEnd > endRestrictionMinute) {

                                                            Toast.makeText(EditDay.this, "Choose an end time before the next class!", Toast.LENGTH_LONG).show();

                                                        } else {

                                                            String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                            String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                            if (is24Hour) {

                                                                endTimeTextEdit = hourOfDayEndString + ":" + minuteEndString;

                                                            } else {

                                                                final LocalTime endTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayEndString + ":" + minuteEndString);
                                                                endTimeTextEdit = dateTimeFormatterAMPM.print(endTime);

                                                            }

                                                        }

                                                    } else {

                                                        String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                        String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                        if (is24Hour) {

                                                            endTimeTextEdit = hourOfDayEndString + ":" + minuteEndString;

                                                        } else {

                                                            final LocalTime endTime = dateTimeFormatter24Hour.parseLocalTime(hourOfDayEndString + ":" + minuteEndString);
                                                            endTimeTextEdit = dateTimeFormatterAMPM.print(endTime);

                                                        }

                                                    }

                                                }

                                                if (!endTimeTextEdit.isEmpty()) {

                                                    dialogEditClassStartTimeTextView.setText(startTimeTextEdit);
                                                    dialogEditClassEndTimeTextView.setText(endTimeTextEdit);

                                                }

                                            }

                                        }

                                    }

                                };

                                if (is24Hour) {

                                    String[] splitStartTimeString = dialogEditClassStartTimeTextView.getText().toString().split(":");

                                    final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(

                                            onTimeSetListener,
                                            Integer.parseInt(splitStartTimeString[0]),
                                            Integer.parseInt(splitStartTimeString[1].substring(0, 2)),
                                            true

                                    );

                                    timePickerDialog.show(getFragmentManager(), "time_picker_dialog");

                                } else {

                                    String startTimeString = dateTimeFormatter24HourNoColon.print(dateTimeFormatterAMPM.parseLocalTime(dialogEditClassStartTimeTextView.getText().toString()));

                                    final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(

                                            onTimeSetListener,
                                            Integer.parseInt(startTimeString.substring(0, 2)),
                                            Integer.parseInt(startTimeString.substring(3)),
                                            false

                                    );

                                    timePickerDialog.show(getFragmentManager(), "time_picker_dialog");

                                }

                            }

                        });

                        materialDialogBuilderEditClass.onNeutral(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                                DatabaseHelper databaseHelper = new DatabaseHelper(EditDay.this);
                                databaseHelper.removeClassOutOfDay(day, selectedClassName, selectedClassStartTime);

                                final ArrayList<StandardClass> classesArrayList = new ArrayList<>();

                                Cursor classesCursor = databaseHelper.getClassesCursor(day);

                                while (classesCursor.moveToNext()) {

                                    classesArrayList.add(new StandardClass(classesCursor.getString(1),
                                            LocalTime.parse(classesCursor.getString(2)),
                                            LocalTime.parse(classesCursor.getString(3)),
                                            is24Hour,
                                            dateTimeFormatterAMPM,
                                            databaseHelper.getClassLocation(classesCursor.getString(1)),
                                            databaseHelper.getClassTeacher(classesCursor.getString(1)),
                                            databaseHelper.getClassColor(classesCursor.getString(1))));

                                }

                                if (!classesArrayList.isEmpty())
                                    setClassesArrayListOfDay(day, classesArrayList);
                                else
                                    setClassesArrayListOfDay(day, null);

                                databaseHelper.close();

                                materialDialog.dismiss();

                                updateUI();

                            }

                        });

                    }

                }

            });

        } else {

            listAdapter.setClassesArrayList((ArrayList<StandardClass>) standardClassArrayList.clone());

        }

        setListViewHeightBasedOnChildren();

    }

    @Override
    protected void onStop() {

        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("update_data"));

        super.onStop();

    }

    private int getPixelFromDP(float dPtoConvert) {

        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dPtoConvert * scale + 0.5f);

    }

    private void setListViewHeightBasedOnChildren() {

        if (listAdapter == null)
            return;

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {

            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();

        }

        ViewGroup.LayoutParams listViewLayoutParams = listView.getLayoutParams();
        listViewLayoutParams.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(listViewLayoutParams);

    }

}