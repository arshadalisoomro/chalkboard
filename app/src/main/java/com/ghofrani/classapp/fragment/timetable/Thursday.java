package com.ghofrani.classapp.fragment.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.activity.ViewClass;
import com.ghofrani.classapp.adapter.TimetableList;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.modules.DataStore;

import java.util.LinkedList;

public class Thursday extends Fragment {

    private ListView listView;
    private TimetableList listAdapter;
    private LinkedList<StandardClass> standardClassLinkedList;
    private TextView noClassesTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_thursday, container, false);

    }

    @Override
    public void onResume() {

        super.onResume();

        if (listView == null)
            listView = (ListView) getView().findViewById(R.id.thursday_list_view);

        if (noClassesTextView == null)
            noClassesTextView = (TextView) getView().findViewById(R.id.thursday_no_classes);

        if (standardClassLinkedList == null)
            standardClassLinkedList = new LinkedList<>();

        updateUI();

    }

    @Override
    public void onDestroyView() {

        listView = null;
        noClassesTextView = null;
        standardClassLinkedList = null;
        listAdapter = null;

        super.onDestroyView();

    }

    private void updateUI() {

        if (DataStore.thursdayClasses != null) {

            noClassesTextView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            standardClassLinkedList.clear();
            standardClassLinkedList.addAll(DataStore.thursdayClasses);

            if (listAdapter == null) {

                listAdapter = new TimetableList(getContext(), standardClassLinkedList);
                listView.setAdapter(listAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        TextView classNameTextView = (TextView) view.findViewById(R.id.view_list_child_text);
                        startActivity(new Intent(getContext(), ViewClass.class).putExtra("class", classNameTextView.getText().toString()));

                    }

                });

            } else {

                listAdapter.setClassesLinkedList((LinkedList<StandardClass>) standardClassLinkedList.clone());

            }

            setListViewHeightBasedOnChildren();

        } else {

            listView.setVisibility(View.GONE);
            noClassesTextView.setVisibility(View.VISIBLE);

        }

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