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
    int day;
    LinkedList<StandardClass> standardClassLinkedList;

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
        day = getIntent().getIntExtra("day", 0) + 1;
        standardClassLinkedList = new LinkedList<>();

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

        final List<Integer> noClassIndexList = new ArrayList<>();

        if (DataStore.getClassesLinkedListOfDay(day) != null) {

            standardClassLinkedList.clear();
            standardClassLinkedList.addAll(DataStore.getClassesLinkedListOfDay(day));

            for (int i = 0; i <= standardClassLinkedList.size(); i++) {

                if (i == 0) {

                    standardClassLinkedList.add(0, null);
                    noClassIndexList.add(i);

                } else if (i == standardClassLinkedList.size()) {

                    standardClassLinkedList.add(null);
                    noClassIndexList.add(i);
                    i++;

                } else if (i > 1 && !standardClassLinkedList.get(i).getStartTimeString().equals(standardClassLinkedList.get(i - 1).getEndTimeString())) {

                    standardClassLinkedList.add(i, null);
                    noClassIndexList.add(i);
                    i++;

                }

            }

        } else {

            standardClassLinkedList = new LinkedList<>();
            standardClassLinkedList.add(null);

            noClassIndexList.add(0);

        }

        if (listAdapter == null) {

            listAdapter = new EditDayList(this, standardClassLinkedList);
            listView.setAdapter(listAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (noClassIndexList.contains(position)) {

                        final Dialog dialog = new Dialog(EditDay.this);
                        dialog.setContentView(R.layout.dialog_edit_day_add_class);
                        dialog.setTitle("Add Class");

                        Spinner classSpinner = (Spinner) dialog.findViewById(R.id.dialog_edit_day_add_class_spinner);

                        ArrayAdapter<String> classNameSpinnerAdapter = new ArrayAdapter<>(EditDay.this, android.R.layout.simple_spinner_item, DataStore.getAllClassNamesList());

                        classNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        classSpinner.setAdapter(classNameSpinnerAdapter);

                        dialog.show();

                    } else {

                        TextView startTimeTextView = (TextView) view.findViewById(R.id.view_edit_day_list_child_time);
                        final String selectedClassStartTime = startTimeTextView.getText().toString().substring(0, 5).replace(":", "");

                        TextView titleTextView = (TextView) view.findViewById(R.id.view_edit_day_list_child_text);
                        final String selectedClassName = titleTextView.getText().toString();

                        List<String> classNameList = DataStore.getAllClassNamesList();
                        classNameList.remove(selectedClassName);
                        classNameList.add(0, selectedClassName);

                        final Dialog dialog = new Dialog(EditDay.this);
                        dialog.setContentView(R.layout.dialog_edit_day_edit_class);
                        dialog.setTitle("Edit Class");

                        Spinner classSpinner = (Spinner) dialog.findViewById(R.id.dialog_edit_day_edit_class_spinner);

                        ArrayAdapter<String> classNameSpinnerAdapter = new ArrayAdapter<>(EditDay.this, android.R.layout.simple_spinner_item, classNameList);

                        classNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        classSpinner.setAdapter(classNameSpinnerAdapter);

                        Button editClassDialogDeleteButton = (Button) dialog.findViewById(R.id.dialog_edit_day_edit_class_delete_button);
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

                                dialog.dismiss();

                                updateUI();

                            }

                        });

                        dialog.show();

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