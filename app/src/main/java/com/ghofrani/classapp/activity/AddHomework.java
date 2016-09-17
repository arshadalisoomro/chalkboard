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

        if (pickedDateTime == null) {

            final DateTime tomorrow = new DateTime().plusDays(1);

            pickedDateTime = new MutableDateTime();

            pickedDateTime.setYear(tomorrow.getYear());
            pickedDateTime.setMonthOfYear(tomorrow.getMonthOfYear());
            pickedDateTime.setDayOfMonth(tomorrow.getDayOfMonth());
            pickedDateTime.setTime(0, 0, 0, 0);

        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    private boolean isClassInTimetable(String className) {

        for (int i = 1; i < 8; i++) {

            if (Utils.getClassesArrayListOfDay(i) != null) {

                for (StandardClass standardClass : Utils.getClassesArrayListOfDay(i)) {

                    if (className.equals(standardClass.getName()))
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

                            for (StandardClass standardClass : Utils.getClassesArrayListOfDay(i)) {

                                if (standardClass.getName().equals(classNameSpinner.getSelectedItem().toString())) {

                                    switch (i) {

                                        case 1:

                                            listItemTitles.add("Sunday's class at " + standardClass.getStartTimeString(true));

                                            break;

                                        case 2:

                                            listItemTitles.add("Monday's class at " + standardClass.getStartTimeString(true));

                                            break;

                                        case 3:

                                            listItemTitles.add("Tuesday's class at " + standardClass.getStartTimeString(true));

                                            break;

                                        case 4:

                                            listItemTitles.add("Wednesday's class at " + standardClass.getStartTimeString(true));

                                            break;

                                        case 5:

                                            listItemTitles.add("Thursday's class at " + standardClass.getStartTimeString(true));

                                            break;

                                        case 6:

                                            listItemTitles.add("Friday's class at " + standardClass.getStartTimeString(true));

                                            break;

                                        case 7:

                                            listItemTitles.add("Saturday's class at " + standardClass.getStartTimeString(true));

                                            break;


                                    }

                                    listItemClasses.add(standardClass);

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

                                    final DateTime now = new DateTime();

                                    if (!pickedDateTime.isBefore(now.withTimeAtStartOfDay())) {

                                        final android.app.TimePickerDialog.OnTimeSetListener onTimeSetListener = new android.app.TimePickerDialog.OnTimeSetListener() {

                                            @Override
                                            public void onTimeSet(TimePicker datePicker, int hourOfDay, int minuteOfHour) {

                                                pickedDateTime.setTime(hourOfDay, minuteOfHour, 0, 0);

                                                if (pickedDateTime.isBefore(now.withTime(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour(), 0, 0))) {

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
            homeworkNameEditText = null;
            listItemClasses = null;
            listItemTitles = null;
            daySwitches = null;
            priorityCheckBox = null;

        }

        super.onTrimMemory(level);

    }

    private DateTime getDateTimeOfNextClass(String className) {

        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int iterations = -1;

        final LocalTime now = new LocalTime().withHourOfDay(LocalTime.now().getHourOfDay()).withMinuteOfHour(LocalTime.now().getMinuteOfHour()).withSecondOfMinute(0).withMillisOfSecond(0);
        DateTime returnDateTime = null;

        while (returnDateTime == null) {

            iterations++;

            if (Utils.getClassesArrayListOfDay(day) != null) {

                for (StandardClass standardClass : Utils.getClassesArrayListOfDay(day)) {

                    if (standardClass.getName().equals(className)) {

                        if (iterations == 0) {

                            if (standardClass.getStartTime().isAfter(now))
                                returnDateTime = new DateTime().withTime(standardClass.getStartTime());

                        } else {

                            returnDateTime = new DateTime().withTime(standardClass.getStartTime());

                        }

                    }

                }

            }

            if (day == 7)
                day = 1;
            else
                day++;

        }

        return returnDateTime.plusDays(iterations);

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

                        pickedDateTime = null;

                        finish();
                        startActivity(new Intent(AddHomework.this, Main.class).putExtra("fragment", 3));

                    } else {

                        pickedDateTime = null;

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

                    daySwitches.clear();

                    homeworkToAdd = new Homework(homeworkNameEditText.getText().toString(), classNameSpinner.getSelectedItem().toString(), getDateTimeOfNextClass(classNameSpinner.getSelectedItem().toString()), true, databaseHelper.getClassColor(classNameSpinner.getSelectedItem().toString()), priorityCheckBox.isChecked());

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

                    databaseHelper.flushHomework(homeworkArrayList);

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
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        if (intent.hasExtra("origin_notification"))
            originNotification = true;

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

                    pickedDateTime = null;

                    finish();
                    startActivity(new Intent(AddHomework.this, Main.class).putExtra("fragment", 3));

                } else {

                    pickedDateTime = null;

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