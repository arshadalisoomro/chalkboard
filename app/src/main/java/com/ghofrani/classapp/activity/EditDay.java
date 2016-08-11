package com.ghofrani.classapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.ghofrani.classapp.R;
import com.ghofrani.classapp.adapter.EditDayList;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.modules.DataStore;
import com.ghofrani.classapp.modules.DatabaseHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EditDay extends AppCompatActivity {

    ListView listView;
    EditDayList listAdapter;
    LinkedList<StandardClass> standardClassLinkedList;
    List<String> startTimeStringForPosition;
    List<String> endTimeStringPerPosition;
    List<Integer> noClassIndexList;

    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

        listView = (ListView) findViewById(R.id.edit_day_list_view);
        standardClassLinkedList = new LinkedList<>();

        day = getIntent().getIntExtra("day", 0) + 1;

        noClassIndexList = new ArrayList<>();
        startTimeStringForPosition = new ArrayList<>();
        endTimeStringPerPosition = new ArrayList<>();

        updateUI();

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

    private void updateUI() {

        noClassIndexList.clear();
        startTimeStringForPosition.clear();
        endTimeStringPerPosition.clear();

        if (DataStore.getClassesLinkedListOfDay(day) != null) {

            standardClassLinkedList.clear();
            standardClassLinkedList.addAll(DataStore.getClassesLinkedListOfDay(day));

            for (int i = 0; i <= standardClassLinkedList.size(); i++) {

                if (i == 0) {

                    standardClassLinkedList.add(0, null);
                    noClassIndexList.add(0);

                    String firstStartTimeHourString = standardClassLinkedList.get(1).getStartTimeString().substring(0, 2);
                    int firstStartTimeHourInteger = Integer.parseInt(firstStartTimeHourString) - 1;
                    String finalFirstStartTimeHourString = (firstStartTimeHourInteger < 10) ? ("0" + String.valueOf(firstStartTimeHourInteger)) : String.valueOf(firstStartTimeHourInteger);

                    startTimeStringForPosition.add(finalFirstStartTimeHourString + standardClassLinkedList.get(1).getStartTimeString().substring(2));
                    endTimeStringPerPosition.add(standardClassLinkedList.get(1).getStartTimeString());

                } else if (i == standardClassLinkedList.size()) {

                    standardClassLinkedList.add(null);
                    noClassIndexList.add(i);

                    startTimeStringForPosition.add(standardClassLinkedList.get(i - 1).getEndTimeString());

                    String lastStartTimeHourString = standardClassLinkedList.get(i - 1).getEndTimeString().substring(0, 2);
                    int lastStartTimeHourInteger = Integer.parseInt(lastStartTimeHourString) + 1;
                    String finalLastStartTimeHourString = (lastStartTimeHourInteger < 10) ? ("0" + String.valueOf(lastStartTimeHourInteger)) : String.valueOf(lastStartTimeHourInteger);

                    endTimeStringPerPosition.add(finalLastStartTimeHourString + standardClassLinkedList.get(i - 1).getEndTimeString().substring(2));

                    i++;

                } else if (i > 1 && !standardClassLinkedList.get(i).getStartTimeString().equals(standardClassLinkedList.get(i - 1).getEndTimeString())) {

                    startTimeStringForPosition.add(i, standardClassLinkedList.get(i).getStartTimeString());
                    endTimeStringPerPosition.add(i, standardClassLinkedList.get(i).getEndTimeString());

                    standardClassLinkedList.add(i, null);
                    noClassIndexList.add(i);

                    startTimeStringForPosition.add(i, standardClassLinkedList.get(i - 1).getEndTimeString());
                    endTimeStringPerPosition.add(i, standardClassLinkedList.get(i + 1).getStartTimeString());

                    i++;

                } else {

                    startTimeStringForPosition.add(i, standardClassLinkedList.get(i).getStartTimeString());
                    endTimeStringPerPosition.add(i, standardClassLinkedList.get(i).getEndTimeString());

                }

            }

        } else {

            standardClassLinkedList = new LinkedList<>();
            standardClassLinkedList.add(null);

            noClassIndexList.add(0);
            startTimeStringForPosition.add("08:00");
            endTimeStringPerPosition.add("09:00");

        }

        if (listAdapter == null) {

            listAdapter = new EditDayList(this, standardClassLinkedList);
            listView.setAdapter(listAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    if (noClassIndexList.contains(position)) {

                        final Dialog addDialog = new Dialog(EditDay.this);
                        addDialog.setContentView(R.layout.dialog_edit_day_add_class);
                        addDialog.setTitle("Add Class");

                        Spinner classSpinner = (Spinner) addDialog.findViewById(R.id.dialog_edit_day_add_class_spinner);

                        ArrayAdapter<String> classNameSpinnerAdapter = new ArrayAdapter<>(EditDay.this, android.R.layout.simple_spinner_item, DataStore.getAllClassNamesList());

                        classNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        classSpinner.setAdapter(classNameSpinnerAdapter);

                        TextView dialogAddClassStartTimeTextView = (TextView) addDialog.findViewById(R.id.dialog_edit_day_add_class_start_time);
                        dialogAddClassStartTimeTextView.setText(startTimeStringForPosition.get(position));

                        TextView dialogAddClassEndTimeTextView = (TextView) addDialog.findViewById(R.id.dialog_edit_day_add_class_end_time);
                        dialogAddClassEndTimeTextView.setText(endTimeStringPerPosition.get(position));

                        Button addClassDialogChangeTimeButton = (Button) addDialog.findViewById(R.id.dialog_edit_day_add_class_time_picker_button);
                        addClassDialogChangeTimeButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {

                                        TextView startTimeTextView = (TextView) addDialog.findViewById(R.id.dialog_edit_day_add_class_start_time);
                                        TextView endTimeTextView = (TextView) addDialog.findViewById(R.id.dialog_edit_day_add_class_end_time);

                                        if (hourOfDay > hourOfDayEnd) {

                                            Toast.makeText(EditDay.this, "Your end time is before your start time!", Toast.LENGTH_LONG).show();

                                        } else if (hourOfDay == hourOfDayEnd) {

                                            if (minute == minuteEnd) {

                                                Toast.makeText(EditDay.this, "Your start time equals your end time!", Toast.LENGTH_LONG).show();

                                            } else {

                                                if (position != 0) {

                                                    if (Integer.parseInt(startTimeTextView.getText().toString().substring(0, 2)) < hourOfDay) {

                                                        String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                        String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                        startTimeTextView.setText(hourOfDayString + ":" + minuteString);

                                                    } else if (Integer.parseInt(startTimeTextView.getText().toString().substring(0, 2)) == hourOfDay) {

                                                        if (Integer.parseInt(startTimeTextView.getText().toString().substring(3)) < minute) {

                                                            String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                            String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                            startTimeTextView.setText(hourOfDayString + ":" + minuteString);

                                                        } else {

                                                            Toast.makeText(EditDay.this, "Please select a start time after the previous class!", Toast.LENGTH_LONG).show();
                                                            return;

                                                        }

                                                    } else {

                                                        Toast.makeText(EditDay.this, "Please select a start time after the previous class!", Toast.LENGTH_LONG).show();
                                                        return;

                                                    }

                                                } else {

                                                    String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                    String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                    startTimeTextView.setText(hourOfDayString + ":" + minuteString);

                                                }

                                                if (position != (standardClassLinkedList.size() - 1)) {

                                                    if (Integer.parseInt(endTimeTextView.getText().toString().substring(0, 2)) > hourOfDayEnd) {

                                                        String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                        String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                        endTimeTextView.setText(hourOfDayEndString + ":" + minuteEndString);

                                                    } else if (Integer.parseInt(endTimeTextView.getText().toString().substring(0, 2)) == hourOfDayEnd) {

                                                        if (Integer.parseInt(endTimeTextView.getText().toString().substring(3)) > minute) {

                                                            String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                            String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                            endTimeTextView.setText(hourOfDayEndString + ":" + minuteEndString);

                                                        } else {

                                                            Toast.makeText(EditDay.this, "Please select an end time before the next class!", Toast.LENGTH_LONG).show();

                                                        }

                                                    } else {

                                                        Toast.makeText(EditDay.this, "Please select an end time before the next class!", Toast.LENGTH_LONG).show();

                                                    }

                                                } else {

                                                    String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                    String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                    endTimeTextView.setText(hourOfDayEndString + ":" + minuteEndString);

                                                }

                                            }

                                        } else {

                                            if (position != 0) {

                                                if (Integer.parseInt(startTimeTextView.getText().toString().substring(0, 2)) < hourOfDay) {

                                                    String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                    String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                    startTimeTextView.setText(hourOfDayString + ":" + minuteString);

                                                } else if (Integer.parseInt(startTimeTextView.getText().toString().substring(0, 2)) == hourOfDay) {

                                                    if (Integer.parseInt(startTimeTextView.getText().toString().substring(3)) < minute) {

                                                        String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                        String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                        startTimeTextView.setText(hourOfDayString + ":" + minuteString);

                                                    } else {

                                                        Toast.makeText(EditDay.this, "Please select a start time after the previous class!", Toast.LENGTH_LONG).show();
                                                        return;

                                                    }

                                                } else {

                                                    Toast.makeText(EditDay.this, "Please select a start time after the previous class!", Toast.LENGTH_LONG).show();
                                                    return;

                                                }

                                            } else {

                                                String hourOfDayString = (hourOfDay < 10) ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                                                String minuteString = (minute < 10) ? ("0" + String.valueOf(minute)) : String.valueOf(minute);

                                                startTimeTextView.setText(hourOfDayString + ":" + minuteString);

                                            }

                                            if (position != (standardClassLinkedList.size() - 1)) {

                                                if (Integer.parseInt(endTimeTextView.getText().toString().substring(0, 2)) > hourOfDayEnd) {

                                                    String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                    String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                    endTimeTextView.setText(hourOfDayEndString + ":" + minuteEndString);

                                                } else if (Integer.parseInt(endTimeTextView.getText().toString().substring(0, 2)) == hourOfDayEnd) {

                                                    if (Integer.parseInt(endTimeTextView.getText().toString().substring(3)) > minute) {

                                                        String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                        String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                        endTimeTextView.setText(hourOfDayEndString + ":" + minuteEndString);

                                                    } else {

                                                        Toast.makeText(EditDay.this, "Please select an end time before the next class!", Toast.LENGTH_LONG).show();

                                                    }

                                                } else {

                                                    Toast.makeText(EditDay.this, "Please select an end time before the next class!", Toast.LENGTH_LONG).show();

                                                }

                                            } else {

                                                String hourOfDayEndString = (hourOfDayEnd < 10) ? ("0" + String.valueOf(hourOfDayEnd)) : String.valueOf(hourOfDayEnd);
                                                String minuteEndString = (minuteEnd < 10) ? ("0" + String.valueOf(minuteEnd)) : String.valueOf(minuteEnd);

                                                endTimeTextView.setText(hourOfDayEndString + ":" + minuteEndString);

                                            }

                                        }

                                    }

                                };

                                TextView startTimeTextView = (TextView) addDialog.findViewById(R.id.dialog_edit_day_add_class_start_time);

                                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(

                                        onTimeSetListener,
                                        Integer.parseInt(startTimeTextView.getText().toString().substring(0, 2)),
                                        Integer.parseInt(startTimeTextView.getText().toString().substring(3)),
                                        true

                                );

                                timePickerDialog.show(getFragmentManager(), "timepickerdialog");

                            }

                        });

                        Button addClassDialogAddButton = (Button) addDialog.findViewById(R.id.dialog_edit_day_add_class_add_button);
                        addClassDialogAddButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                Spinner nameSpinner = (Spinner) addDialog.findViewById(R.id.dialog_edit_day_add_class_spinner);
                                String name = nameSpinner.getSelectedItem().toString();

                                TextView startTimeTextView = (TextView) addDialog.findViewById(R.id.dialog_edit_day_add_class_start_time);
                                String startTimeString = startTimeTextView.getText().toString().replace(":", "");

                                TextView endTimeTextView = (TextView) addDialog.findViewById(R.id.dialog_edit_day_add_class_end_time);
                                String endTimeString = endTimeTextView.getText().toString().replace(":", "");

                                if (DataStore.getClassesLinkedListOfDay(day) != null) {

                                    LinkedList<StandardClass> currentClasses = (LinkedList<StandardClass>) DataStore.getClassesLinkedListOfDay(day).clone();

                                    boolean inserted = false;
                                    int index = 0;

                                    while (!inserted) {

                                        if (startTimeString.equals(currentClasses.get(index).getEndTimeString().replace(":", ""))) {

                                            inserted = true;
                                            currentClasses.add(index + 1, new StandardClass(EditDay.this, name, startTimeString, endTimeString));

                                        } else if (Integer.parseInt(startTimeString) < Integer.parseInt(currentClasses.get(index).getStartTimeString().replace(":", ""))) {

                                            inserted = true;
                                            currentClasses.add(index, new StandardClass(EditDay.this, name, startTimeString, endTimeString));

                                        } else if ((currentClasses.size() - index) == 1) {

                                            inserted = true;
                                            currentClasses.add(new StandardClass(EditDay.this, name, startTimeString, endTimeString));

                                        }

                                        index++;

                                    }

                                    DatabaseHelper databaseHelper = new DatabaseHelper(EditDay.this);

                                    databaseHelper.deleteAllClassesOfDay(day);

                                    for (int i = 0; i < currentClasses.size(); i++) {

                                        databaseHelper.insertClassIntoDay(new String[]{currentClasses.get(i).getName(), currentClasses.get(i).getStartTimeString().replace(":", ""), currentClasses.get(i).getEndTimeString().replace(":", "")}, day);

                                    }

                                    databaseHelper.close();

                                    DataStore.setClassesLinkedListOfDay(day, currentClasses);

                                } else {

                                    DatabaseHelper databaseHelper = new DatabaseHelper(EditDay.this);

                                    databaseHelper.insertClassIntoDay(new String[]{name, startTimeString.replace(":", ""), endTimeString.replace(":", "")}, day);

                                    databaseHelper.close();

                                    LinkedList<StandardClass> currentClasses = new LinkedList<>();
                                    currentClasses.add(new StandardClass(EditDay.this, name, startTimeString, endTimeString));

                                    DataStore.setClassesLinkedListOfDay(day, currentClasses);

                                }

                                addDialog.dismiss();

                                updateUI();

                            }

                        });

                        addDialog.show();

                    } else {

                        TextView startTimeTextView = (TextView) view.findViewById(R.id.view_edit_day_list_child_time);
                        final String selectedClassStartTime = startTimeTextView.getText().toString().substring(0, 5).replace(":", "");

                        TextView titleTextView = (TextView) view.findViewById(R.id.view_edit_day_list_child_text);
                        final String selectedClassName = titleTextView.getText().toString();

                        List<String> classNameList = DataStore.getAllClassNamesList();
                        classNameList.remove(selectedClassName);
                        classNameList.add(0, selectedClassName);

                        final Dialog editDialog = new Dialog(EditDay.this);
                        editDialog.setContentView(R.layout.dialog_edit_day_edit_class);
                        editDialog.setTitle("Edit Class");

                        TextView dialogEditClassStartTimeTextView = (TextView) editDialog.findViewById(R.id.dialog_edit_day_edit_class_start_time);
                        dialogEditClassStartTimeTextView.setText(startTimeStringForPosition.get(position));

                        TextView dialogEditClassEndTimeTextView = (TextView) editDialog.findViewById(R.id.dialog_edit_day_edit_class_end_time);
                        dialogEditClassEndTimeTextView.setText(endTimeStringPerPosition.get(position));

                        Spinner classSpinner = (Spinner) editDialog.findViewById(R.id.dialog_edit_day_edit_class_spinner);

                        ArrayAdapter<String> classNameSpinnerAdapter = new ArrayAdapter<>(EditDay.this, android.R.layout.simple_spinner_item, classNameList);

                        classNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        classSpinner.setAdapter(classNameSpinnerAdapter);

                        Button editClassDialogDeleteButton = (Button) editDialog.findViewById(R.id.dialog_edit_day_edit_class_delete_button);
                        editClassDialogDeleteButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                DatabaseHelper databaseHelper = new DatabaseHelper(EditDay.this);
                                databaseHelper.removeClassOutOfDay(day, selectedClassName, selectedClassStartTime);

                                LinkedList<StandardClass> classesLinkedList = new LinkedList<>();

                                Cursor cursor = databaseHelper.getClasses(day);

                                while (cursor.moveToNext()) {

                                    classesLinkedList.add(new StandardClass(getApplicationContext(), cursor.getString(1), cursor.getString(2), cursor.getString(3)));

                                }

                                if (!classesLinkedList.isEmpty())
                                    DataStore.setClassesLinkedListOfDay(day, classesLinkedList);
                                else
                                    DataStore.setClassesLinkedListOfDay(day, null);

                                databaseHelper.close();

                                editDialog.dismiss();

                                updateUI();

                            }

                        });

                        editDialog.show();

                    }

                }

            });

        } else {

            listAdapter.setClassesLinkedList((LinkedList<StandardClass>) standardClassLinkedList.clone());

        }

        setListViewHeightBasedOnChildren(listView);

    }

    @Override
    protected void onStop() {

        super.onStop();

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent("update_data"));

    }

    private int getPixelFromDP(float dPtoConvert) {

        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dPtoConvert * scale + 0.5f);

    }

    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

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