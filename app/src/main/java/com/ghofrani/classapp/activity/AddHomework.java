package com.ghofrani.classapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ghofrani.classapp.R;
import com.ghofrani.classapp.event.Update;
import com.ghofrani.classapp.model.Homework;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.module.DataSingleton;
import com.ghofrani.classapp.module.DatabaseHelper;
import com.ghofrani.classapp.module.Utils;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
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
    private EditText homeworkNameEditText;
    private boolean firstRunSpecific = true;
    private int selectedIndex;
    private ArrayList<StandardClass> listItemClasses;
    private ArrayList<String> listItemTitles;
    private ArrayList<Integer> daySwitches;
    private CheckBox priorityCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Utils.setTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_homework);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.add_homework_toolbar);
        toolbar.setTitle("Add Homework");
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra("origin_notification"))
            originNotification = true;

        final DateTime tomorrow = new DateTime().plusDays(1);

        pickedDateTime = new MutableDateTime();

        pickedDateTime.setYear(tomorrow.getYear());
        pickedDateTime.setMonthOfYear(tomorrow.getMonthOfYear());
        pickedDateTime.setDayOfMonth(tomorrow.getDayOfMonth());
        pickedDateTime.setTime(0, 0, 0, 0);

        ArrayList<String> allClassNamesArrayList = DataSingleton.getInstance().getAllClassNamesArrayList();

        if (DataSingleton.getInstance().getCurrentClass() != null) {

            allClassNamesArrayList.remove(DataSingleton.getInstance().getCurrentClass().getName());
            allClassNamesArrayList.add(0, DataSingleton.getInstance().getCurrentClass().getName());

        }

        classNameSpinner = (Spinner) findViewById(R.id.add_homework_class_spinner);

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

        nextClassRadioButton = (RadioButton) findViewById(R.id.radio_next);

        if (DataSingleton.getInstance().getCurrentClass() != null)
            nextClassRadioButton.setChecked(true);

    }

    @Override
    protected void onResume() {

        super.onResume();

        if (classNameSpinner == null)
            classNameSpinner = (Spinner) findViewById(R.id.add_homework_class_spinner);

        if (listItemClasses == null)
            listItemClasses = new ArrayList<>();

        if (listItemTitles == null)
            listItemTitles = new ArrayList<>();

        if (daySwitches == null)
            daySwitches = new ArrayList<>();

        if (nextClassRadioButton == null)
            nextClassRadioButton = (RadioButton) findViewById(R.id.radio_next);

        if (specificClassRadioButton == null)
            specificClassRadioButton = (RadioButton) findViewById(R.id.radio_specific);

        if (customTimeRadioButton == null)
            customTimeRadioButton = (RadioButton) findViewById(R.id.radio_custom);

        if (homeworkNameEditText == null)
            homeworkNameEditText = (EditText) findViewById(R.id.add_homework_input_name);

        if (priorityCheckBox == null)
            priorityCheckBox = (CheckBox) findViewById(R.id.add_homework_high_priority_check_box);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    private boolean isClassInTimetable(String className) {

        for (int i = 1; i < 8; i++) {

            if (Utils.getClassesArrayListOfDay(i) != null) {

                for (int i2 = 0; i2 < Utils.getClassesArrayListOfDay(i).size(); i2++) {

                    if (className.equals(Utils.getClassesArrayListOfDay(i).get(i2).getName()))
                        return true;

                }

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

                    for (int i = 1; i < 8; i++) {

                        if (Utils.getClassesArrayListOfDay(i) != null) {

                            for (int i2 = 0; i2 < Utils.getClassesArrayListOfDay(i).size(); i2++) {

                                if (Utils.getClassesArrayListOfDay(i).get(i2).getName().equals(classNameSpinner.getSelectedItem().toString())) {

                                    switch (i) {

                                        case 1:

                                            listItemTitles.add("Sunday's class at " + DataSingleton.getInstance().getSundayClasses().get(i2).getStartTimeString(true));

                                            break;

                                        case 2:

                                            listItemTitles.add("Monday's class at " + DataSingleton.getInstance().getMondayClasses().get(i2).getStartTimeString(true));

                                            break;

                                        case 3:

                                            listItemTitles.add("Tuesday's class at " + DataSingleton.getInstance().getTuesdayClasses().get(i2).getStartTimeString(true));

                                            break;

                                        case 4:

                                            listItemTitles.add("Wednesday's class at " + DataSingleton.getInstance().getWednesdayClasses().get(i2).getStartTimeString(true));

                                            break;

                                        case 5:

                                            listItemTitles.add("Thursday's class at " + DataSingleton.getInstance().getThursdayClasses().get(i2).getStartTimeString(true));

                                            break;

                                        case 6:

                                            listItemTitles.add("Friday's class at " + DataSingleton.getInstance().getFridayClasses().get(i2).getStartTimeString(true));

                                            break;

                                        case 7:

                                            listItemTitles.add("Saturday's class at " + DataSingleton.getInstance().getSaturdayClasses().get(i2).getStartTimeString(true));

                                            break;


                                    }

                                    listItemClasses.add(Utils.getClassesArrayListOfDay(i).get(i2));

                                }

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

                            final android.app.DatePickerDialog.OnDateSetListener onDateSetListener = new android.app.DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                                    pickedDateTime.setYear(year);
                                    pickedDateTime.setMonthOfYear(monthOfYear + 1);
                                    pickedDateTime.setDayOfMonth(dayOfMonth);
                                    pickedDateTime.setTime(0, 0, 0, 0);

                                    final DateTime now = DateTime.now();

                                    if (!pickedDateTime.isBefore(now.withTimeAtStartOfDay())) {

                                        final android.app.TimePickerDialog.OnTimeSetListener onTimeSetListener = new android.app.TimePickerDialog.OnTimeSetListener() {

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
            homeworkNameEditText = null;
            listItemClasses = null;
            listItemTitles = null;
            daySwitches = null;
            priorityCheckBox = null;

        }

        super.onTrimMemory(level);

    }

    private DateTime getSundayDateTimeOfClass(String className, LocalTime now) {

        if (DataSingleton.getInstance().getSundayClasses() != null) {

            for (int i = 0; i < DataSingleton.getInstance().getSundayClasses().size(); i++) {

                if (DataSingleton.getInstance().getSundayClasses().get(i).getName().equals(className)) {

                    if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

                        if (DataSingleton.getInstance().getSundayClasses().get(i).getStartTime().isAfter(now) || daySwitches.contains(Calendar.SUNDAY)) {

                            return new DateTime().withTime(DataSingleton.getInstance().getSundayClasses().get(i).getStartTime());

                        }

                    } else {

                        return new DateTime().withTime(DataSingleton.getInstance().getSundayClasses().get(i).getStartTime());

                    }

                }

            }

        }

        daySwitches.add(Calendar.SUNDAY);

        return getMondayDateTimeOfClass(className, now).plusDays(1);

    }

    private DateTime getMondayDateTimeOfClass(String className, LocalTime now) {

        if (DataSingleton.getInstance().getMondayClasses() != null) {

            for (int i = 0; i < DataSingleton.getInstance().getMondayClasses().size(); i++) {

                if (DataSingleton.getInstance().getMondayClasses().get(i).getName().equals(className)) {

                    if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {

                        if (DataSingleton.getInstance().getMondayClasses().get(i).getStartTime().isAfter(now) || daySwitches.contains(Calendar.MONDAY)) {

                            return new DateTime().withTime(DataSingleton.getInstance().getMondayClasses().get(i).getStartTime());

                        }

                    } else {

                        return new DateTime().withTime(DataSingleton.getInstance().getMondayClasses().get(i).getStartTime());

                    }

                }

            }

        }

        daySwitches.add(Calendar.MONDAY);

        return getTuesdayDateTimeOfClass(className, now).plusDays(1);

    }

    private DateTime getTuesdayDateTimeOfClass(String className, LocalTime now) {

        if (DataSingleton.getInstance().getTuesdayClasses() != null) {

            for (int i = 0; i < DataSingleton.getInstance().getTuesdayClasses().size(); i++) {

                if (DataSingleton.getInstance().getTuesdayClasses().get(i).getName().equals(className)) {

                    if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {

                        if (DataSingleton.getInstance().getTuesdayClasses().get(i).getStartTime().isAfter(now) || daySwitches.contains(Calendar.TUESDAY)) {

                            return new DateTime().withTime(DataSingleton.getInstance().getTuesdayClasses().get(i).getStartTime());

                        }

                    } else {

                        return new DateTime().withTime(DataSingleton.getInstance().getTuesdayClasses().get(i).getStartTime());

                    }

                }

            }

        }

        daySwitches.add(Calendar.TUESDAY);

        return getWednesdayDateTimeOfClass(className, now).plusDays(1);

    }

    private DateTime getWednesdayDateTimeOfClass(String className, LocalTime now) {

        if (DataSingleton.getInstance().getWednesdayClasses() != null) {

            for (int i = 0; i < DataSingleton.getInstance().getWednesdayClasses().size(); i++) {

                if (DataSingleton.getInstance().getWednesdayClasses().get(i).getName().equals(className)) {

                    if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {

                        if (DataSingleton.getInstance().getWednesdayClasses().get(i).getStartTime().isAfter(now) || daySwitches.contains(Calendar.WEDNESDAY)) {

                            return new DateTime().withTime(DataSingleton.getInstance().getWednesdayClasses().get(i).getStartTime());

                        }

                    } else {

                        return new DateTime().withTime(DataSingleton.getInstance().getWednesdayClasses().get(i).getStartTime());

                    }

                }

            }

        }

        daySwitches.add(Calendar.WEDNESDAY);

        return getThursdayDateTimeOfClass(className, now).plusDays(1);

    }

    private DateTime getThursdayDateTimeOfClass(String className, LocalTime now) {

        if (DataSingleton.getInstance().getThursdayClasses() != null) {

            for (int i = 0; i < DataSingleton.getInstance().getThursdayClasses().size(); i++) {

                if (DataSingleton.getInstance().getThursdayClasses().get(i).getName().equals(className)) {

                    if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {

                        if (DataSingleton.getInstance().getThursdayClasses().get(i).getStartTime().isAfter(now) || daySwitches.contains(Calendar.THURSDAY)) {

                            return new DateTime().withTime(DataSingleton.getInstance().getThursdayClasses().get(i).getStartTime());

                        }

                    } else {

                        return new DateTime().withTime(DataSingleton.getInstance().getThursdayClasses().get(i).getStartTime());

                    }

                }

            }

        }

        daySwitches.add(Calendar.THURSDAY);

        return getFridayDateTimeOfClass(className, now).plusDays(1);

    }

    private DateTime getFridayDateTimeOfClass(String className, LocalTime now) {

        if (DataSingleton.getInstance().getFridayClasses() != null) {

            for (int i = 0; i < DataSingleton.getInstance().getFridayClasses().size(); i++) {

                if (DataSingleton.getInstance().getFridayClasses().get(i).getName().equals(className)) {

                    if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {

                        if (DataSingleton.getInstance().getFridayClasses().get(i).getStartTime().isAfter(now) || daySwitches.contains(Calendar.FRIDAY)) {

                            return new DateTime().withTime(DataSingleton.getInstance().getFridayClasses().get(i).getStartTime());

                        }

                    } else {

                        return new DateTime().withTime(DataSingleton.getInstance().getFridayClasses().get(i).getStartTime());

                    }

                }

            }

        }

        daySwitches.add(Calendar.FRIDAY);

        return getSaturdayDateTimeOfClass(className, now).plusDays(1);

    }

    private DateTime getSaturdayDateTimeOfClass(String className, LocalTime now) {

        if (DataSingleton.getInstance().getSaturdayClasses() != null) {

            for (int i = 0; i < DataSingleton.getInstance().getSaturdayClasses().size(); i++) {

                if (DataSingleton.getInstance().getSaturdayClasses().get(i).getName().equals(className)) {

                    if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

                        if (DataSingleton.getInstance().getSaturdayClasses().get(i).getStartTime().isAfter(now) || daySwitches.contains(Calendar.SATURDAY)) {

                            return new DateTime().withTime(DataSingleton.getInstance().getSaturdayClasses().get(i).getStartTime());

                        }

                    } else {

                        return new DateTime().withTime(DataSingleton.getInstance().getSaturdayClasses().get(i).getStartTime());

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

            if (!homeworkNameEditText.getText().toString().isEmpty()) {

                DatabaseHelper databaseHelper = new DatabaseHelper(this);
                Homework homeworkToAdd;

                if (nextClassRadioButton.isChecked()) {

                    homeworkToAdd = new Homework(homeworkNameEditText.getText().toString(), classNameSpinner.getSelectedItem().toString(), getNextDateTimeOfClass(classNameSpinner.getSelectedItem().toString()), true, databaseHelper.getClassColor(classNameSpinner.getSelectedItem().toString()), priorityCheckBox.isChecked());

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

                    homeworkToAdd = new Homework(homeworkNameEditText.getText().toString(), classNameSpinner.getSelectedItem().toString(), pickedDateTimeSpecific.plusDays(plusDays).withTime(listItemClasses.get(selectedIndex).getStartTime()), true, databaseHelper.getClassColor(classNameSpinner.getSelectedItem().toString()), priorityCheckBox.isChecked());

                } else if (customTimeRadioButton.isChecked()) {

                    homeworkToAdd = new Homework(homeworkNameEditText.getText().toString(), classNameSpinner.getSelectedItem().toString(), pickedDateTime.toDateTime(), false, databaseHelper.getClassColor(classNameSpinner.getSelectedItem().toString()), priorityCheckBox.isChecked());

                } else {

                    databaseHelper.close();

                    Toast.makeText(this, "Please select a due date!", Toast.LENGTH_LONG).show();

                    return true;

                }

                boolean inserted = false;
                int index;

                ArrayList<Homework> homeworkArrayList = new ArrayList<>();

                homeworkArrayList.addAll(DataSingleton.getInstance().getTodayHomeworkArrayList());
                homeworkArrayList.addAll(DataSingleton.getInstance().getTomorrowHomeworkArrayList());
                homeworkArrayList.addAll(DataSingleton.getInstance().getThisWeekHomeworkArrayList());
                homeworkArrayList.addAll(DataSingleton.getInstance().getNextWeekHomeworkArrayList());
                homeworkArrayList.addAll(DataSingleton.getInstance().getThisMonthHomeworkArrayList());
                homeworkArrayList.addAll(DataSingleton.getInstance().getBeyondThisMonthHomeworkArrayList());
                homeworkArrayList.addAll(DataSingleton.getInstance().getPastHomeworkArrayList());

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

                try {

                    databaseHelper.deleteAllHomework();
                    databaseHelper.addHomework(homeworkArrayList);

                } finally {

                    databaseHelper.close();

                }

                EventBus.getDefault().post(new Update(false, true, false, false));

                final View currentFocus = this.getCurrentFocus();

                if (currentFocus != null) {

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);

                }

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