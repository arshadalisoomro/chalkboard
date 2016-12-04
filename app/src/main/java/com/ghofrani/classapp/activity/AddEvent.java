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
import com.ghofrani.classapp.model.Event;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.module.DataSingleton;
import com.ghofrani.classapp.module.DatabaseHelper;
import com.ghofrani.classapp.module.Utils;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;

import java.util.ArrayList;

public class AddEvent extends AppCompatActivity {

    private boolean originNotification = false;
    private Spinner classNameSpinner;
    private RadioButton nextClassRadioButton;
    private RadioButton specificClassRadioButton;
    private RadioButton customTimeRadioButton;
    private MutableDateTime pickedDateTime;
    private EditText eventNameEditText;
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
        setContentView(R.layout.activity_add_event);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.add_event_toolbar);
        toolbar.setTitle("Add Event");
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

        classNameSpinner = (Spinner) findViewById(R.id.add_event_class_spinner);

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
            classNameSpinner = (Spinner) findViewById(R.id.add_event_class_spinner);

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

        if (eventNameEditText == null)
            eventNameEditText = (EditText) findViewById(R.id.add_event_input_name);

        if (priorityCheckBox == null)
            priorityCheckBox = (CheckBox) findViewById(R.id.add_event_high_priority_check_box);

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

        for (int i = DateTimeConstants.MONDAY; i <= DateTimeConstants.SUNDAY; i++) {

            if (Utils.getClassesArrayListOfDay(i) != null) {

                for (final StandardClass standardClass : Utils.getClassesArrayListOfDay(i)) {

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

                    for (int i = DateTimeConstants.MONDAY; i <= DateTimeConstants.SUNDAY; i++) {

                        if (Utils.getClassesArrayListOfDay(i) != null) {

                            for (final StandardClass standardClass : Utils.getClassesArrayListOfDay(i)) {

                                if (standardClass.getName().equals(classNameSpinner.getSelectedItem().toString())) {

                                    switch (i) {

                                        case DateTimeConstants.MONDAY:

                                            listItemTitles.add("Monday's class at " + standardClass.getStartTimeString(true));

                                            break;

                                        case DateTimeConstants.TUESDAY:

                                            listItemTitles.add("Tuesday's class at " + standardClass.getStartTimeString(true));

                                            break;

                                        case DateTimeConstants.WEDNESDAY:

                                            listItemTitles.add("Wednesday's class at " + standardClass.getStartTimeString(true));

                                            break;

                                        case DateTimeConstants.THURSDAY:

                                            listItemTitles.add("Thursday's class at " + standardClass.getStartTimeString(true));

                                            break;

                                        case DateTimeConstants.FRIDAY:

                                            listItemTitles.add("Friday's class at " + standardClass.getStartTimeString(true));

                                            break;

                                        case DateTimeConstants.SATURDAY:

                                            listItemTitles.add("Saturday's class at " + standardClass.getStartTimeString(true));

                                            break;

                                        case DateTimeConstants.SUNDAY:

                                            listItemTitles.add("Sunday's class at " + standardClass.getStartTimeString(true));

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

                            new MaterialDialog.Builder(AddEvent.this)
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

                    }, 50);

                    break;

                case R.id.radio_custom:

                    final View currentFocus = this.getCurrentFocus();

                    if (currentFocus != null) {

                        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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

                                                    Toast.makeText(AddEvent.this, "Choose a time after now!", Toast.LENGTH_LONG).show();

                                                    pickedDateTime.setYear(now.plusDays(1).getYear());
                                                    pickedDateTime.setMonthOfYear(now.plusDays(1).getMonthOfYear());
                                                    pickedDateTime.setDayOfMonth(now.plusDays(1).getDayOfMonth());
                                                    pickedDateTime.setTime(0, 0, 0, 0);

                                                }

                                            }

                                        };

                                        new android.app.TimePickerDialog(AddEvent.this, onTimeSetListener, now.plusHours(1).getHourOfDay(), now.plusHours(1).getMinuteOfHour(), true).show();

                                    } else {

                                        pickedDateTime.setYear(now.plusDays(1).getYear());
                                        pickedDateTime.setMonthOfYear(now.plusDays(1).getMonthOfYear());
                                        pickedDateTime.setDayOfMonth(now.plusDays(1).getDayOfMonth());
                                        pickedDateTime.setTime(0, 0, 0, 0);

                                        Toast.makeText(AddEvent.this, "Choose a day after yesterday!", Toast.LENGTH_LONG).show();

                                    }

                                }

                            };

                            new android.app.DatePickerDialog(AddEvent.this, onDateSetListener, pickedDateTime.getYear(), pickedDateTime.getMonthOfYear() - 1, pickedDateTime.getDayOfMonth()).show();

                        }

                    }, 50);

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

            eventNameEditText = null;

            listItemClasses = null;
            listItemTitles = null;
            daySwitches = null;

            priorityCheckBox = null;

        }

        super.onTrimMemory(level);

    }

    private DateTime getDateTimeOfNextClass(String className) {

        int day = new DateTime().getDayOfWeek();
        int iterations = -1;

        final LocalTime now = new LocalTime().withHourOfDay(LocalTime.now().getHourOfDay()).withMinuteOfHour(LocalTime.now().getMinuteOfHour()).withSecondOfMinute(0).withMillisOfSecond(0);
        DateTime returnDateTime = null;

        while (returnDateTime == null) {

            iterations++;

            if (Utils.getClassesArrayListOfDay(day) != null) {

                for (final StandardClass standardClass : Utils.getClassesArrayListOfDay(day)) {

                    if (standardClass.getName().equals(className)) {

                        if (iterations == 0) {

                            if (standardClass.getStartTime().isAfter(now))
                                returnDateTime = new DateTime().withTime(standardClass.getStartTime());

                        } else {

                            returnDateTime = new DateTime().withTime(standardClass.getStartTime());

                        }

                        break;

                    }

                }

            }

            if (day == DateTimeConstants.SUNDAY)
                day = DateTimeConstants.MONDAY;
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
            materialDialogBuilder.content("This event will be deleted.");
            materialDialogBuilder.positiveText("YES");
            materialDialogBuilder.positiveColorRes(R.color.black);
            materialDialogBuilder.negativeText("CANCEL");
            materialDialogBuilder.negativeColorRes(R.color.black);

            materialDialogBuilder.onPositive(new MaterialDialog.SingleButtonCallback() {

                @Override
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                    materialDialog.dismiss();

                    pickedDateTime = null;

                    if (originNotification) {

                        finish();
                        startActivity(new Intent(AddEvent.this, Main.class).putExtra("fragment", 6));

                    } else {

                        AddEvent.super.onBackPressed();

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

        } else if (menuItem.getItemId() == R.id.toolbar_check_check_item) {

            if (!eventNameEditText.getText().toString().isEmpty()) {

                DatabaseHelper databaseHelper = new DatabaseHelper(this);
                Event eventToAdd;

                if (nextClassRadioButton.isChecked()) {

                    daySwitches.clear();

                    eventToAdd = new Event(eventNameEditText.getText().toString(), "", Event.TYPE_HOMEWORK, classNameSpinner.getSelectedItem().toString(), getDateTimeOfNextClass(classNameSpinner.getSelectedItem().toString()), true, new ArrayList<DateTime>(), databaseHelper.getClassColor(classNameSpinner.getSelectedItem().toString()));

                } else if (specificClassRadioButton.isChecked()) {

                    final DateTime pickedDateTimeSpecific = new DateTime();
                    int plusDays = 0;

                    if (listItemTitles.get(selectedIndex).startsWith("Mon")) {

                        plusDays = Utils.getPlusDaysTill(DateTimeConstants.MONDAY);

                    } else if (listItemTitles.get(selectedIndex).startsWith("Tue")) {

                        plusDays = Utils.getPlusDaysTill(DateTimeConstants.TUESDAY);

                    } else if (listItemTitles.get(selectedIndex).startsWith("Wed")) {

                        plusDays = Utils.getPlusDaysTill(DateTimeConstants.WEDNESDAY);

                    } else if (listItemTitles.get(selectedIndex).startsWith("Thu")) {

                        plusDays = Utils.getPlusDaysTill(DateTimeConstants.THURSDAY);

                    } else if (listItemTitles.get(selectedIndex).startsWith("Fri")) {

                        plusDays = Utils.getPlusDaysTill(DateTimeConstants.FRIDAY);

                    } else if (listItemTitles.get(selectedIndex).startsWith("Sat")) {

                        plusDays = Utils.getPlusDaysTill(DateTimeConstants.SATURDAY);

                    } else if (listItemTitles.get(selectedIndex).startsWith("Sun")) {

                        plusDays = Utils.getPlusDaysTill(DateTimeConstants.SUNDAY);

                    }

                    eventToAdd = new Event(eventNameEditText.getText().toString(), "", Event.TYPE_HOMEWORK, classNameSpinner.getSelectedItem().toString(), pickedDateTimeSpecific.plusDays(plusDays).withTime(listItemClasses.get(selectedIndex).getStartTime()), true, new ArrayList<DateTime>(), databaseHelper.getClassColor(classNameSpinner.getSelectedItem().toString()));

                } else if (customTimeRadioButton.isChecked()) {

                    eventToAdd = new Event(eventNameEditText.getText().toString(), "", Event.TYPE_HOMEWORK, classNameSpinner.getSelectedItem().toString(), pickedDateTime.toDateTime(), false, new ArrayList<DateTime>(), databaseHelper.getClassColor(classNameSpinner.getSelectedItem().toString()));

                } else {

                    databaseHelper.close();

                    Toast.makeText(this, "Please select a due date!", Toast.LENGTH_LONG).show();

                    return true;

                }

                boolean inserted = false;
                int index;

                ArrayList<Event> eventArrayList = new ArrayList<>();

                eventArrayList.addAll(DataSingleton.getInstance().getTodayEventArrayList());
                eventArrayList.addAll(DataSingleton.getInstance().getTomorrowEventArrayList());
                eventArrayList.addAll(DataSingleton.getInstance().getThisWeekEventArrayList());
                eventArrayList.addAll(DataSingleton.getInstance().getNextWeekEventArrayList());
                eventArrayList.addAll(DataSingleton.getInstance().getThisMonthEventArrayList());
                eventArrayList.addAll(DataSingleton.getInstance().getBeyondThisMonthEventArrayList());
                eventArrayList.addAll(DataSingleton.getInstance().getPastEventArrayList());

                if (!eventArrayList.isEmpty()) {

                    index = 0;

                    while (!inserted && index < eventArrayList.size()) {

                        if (index == 0) {

                            if (eventToAdd.getDateTime().isBefore(eventArrayList.get(index).getDateTime()) || eventToAdd.getDateTime().isEqual(eventArrayList.get(index).getDateTime())) {

                                inserted = true;
                                eventArrayList.add(0, eventToAdd);

                            } else if (eventArrayList.size() == 1) {

                                inserted = true;
                                eventArrayList.add(eventToAdd);

                            }

                        } else if (eventToAdd.getDateTime().isBefore(eventArrayList.get(index).getDateTime()) || eventToAdd.getDateTime().isEqual(eventArrayList.get(index).getDateTime())) {

                            eventArrayList.add(index, eventToAdd);
                            inserted = true;

                        } else if (index + 1 == eventArrayList.size()) {

                            eventArrayList.add(eventToAdd);
                            inserted = true;

                        }

                        index++;

                    }

                } else {

                    eventArrayList.add(eventToAdd);

                }

                try {

                    databaseHelper.flushEvents(eventArrayList);

                } finally {

                    databaseHelper.close();

                }

                EventBus.getDefault().post(new Update(false, true, false, false));

                final View currentFocus = this.getCurrentFocus();

                if (currentFocus != null) {

                    final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);

                }

                if (originNotification) {

                    finish();
                    startActivity(new Intent(AddEvent.this, Main.class).putExtra("fragment", 6));

                } else {

                    AddEvent.super.onBackPressed();

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
        materialDialogBuilder.content("This event will be deleted.");
        materialDialogBuilder.positiveText("YES");
        materialDialogBuilder.positiveColorRes(R.color.black);
        materialDialogBuilder.negativeText("CANCEL");
        materialDialogBuilder.negativeColorRes(R.color.black);

        materialDialogBuilder.onPositive(new MaterialDialog.SingleButtonCallback() {

            @Override
            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                materialDialog.dismiss();

                pickedDateTime = null;

                if (originNotification) {

                    finish();
                    startActivity(new Intent(AddEvent.this, Main.class).putExtra("fragment", 6));

                } else {

                    AddEvent.super.onBackPressed();

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