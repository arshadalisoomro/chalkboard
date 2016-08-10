package com.ghofrani.classapp.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.adapter.AddClassIntoTimetableList;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.modules.DataStore;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AddClassIntoTimetable extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class_into_timetable);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.add_class_into_timetable_toolbar);
        toolbar.setTitle("Add Class");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setElevation(getPixelFromDP(4));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.add_class_into_timetable_list_view);

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

        LinkedList<StandardClass> standardClassLinkedList = null;

        if (DataStore.getClassesLinkedListOfDay(getIntent().getIntExtra("day", 0) + 1) != null)
            standardClassLinkedList = (LinkedList<StandardClass>) DataStore.getClassesLinkedListOfDay(getIntent().getIntExtra("day", 0) + 1).clone();

        if (standardClassLinkedList != null) {

            final List<Integer> noClassIndexList = new ArrayList<>();

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

            AddClassIntoTimetableList listAdapter = new AddClassIntoTimetableList(this, standardClassLinkedList);
            listView.setAdapter(listAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (noClassIndexList.contains(position)) {

                        final Dialog dialog = new Dialog(AddClassIntoTimetable.this);
                        dialog.setContentView(R.layout.dialog_add_class_into_timetable);
                        dialog.setTitle("Add Class");

                        dialog.show();

                        Spinner classSpinner = (Spinner) dialog.findViewById(R.id.dialog_add_class_into_timetable_spinner);

                        ArrayAdapter<String> classNameSpinnerAdapter = new ArrayAdapter<>(AddClassIntoTimetable.this, android.R.layout.simple_spinner_item, DataStore.getAllClassNamesList());

                        classNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        classSpinner.setAdapter(classNameSpinnerAdapter);

                    } else {

                        TextView classNameTextView = (TextView) view.findViewById(R.id.view_add_class_into_timetable_list_child_text);
                        startActivity(new Intent(AddClassIntoTimetable.this, ViewClass.class).putExtra("class", classNameTextView.getText().toString()));

                    }

                }

            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    if (!noClassIndexList.contains(position)) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(AddClassIntoTimetable.this);

                        builder.setTitle("Delete class?");
                        builder.setMessage("This class will be deleted out of the timetable.");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                dialog.dismiss();

                            }

                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                dialog.dismiss();

                            }

                        });

                        builder.create().show();

                    }

                    return true;
                }

            });

            setListViewHeightBasedOnChildren(listView);

        } else {

            listView.setVisibility(View.GONE);

        }

    }

    @Override
    protected void onStart() {

        super.onStart();

        updateUI();

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