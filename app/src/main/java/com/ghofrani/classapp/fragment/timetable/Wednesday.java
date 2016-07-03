package com.ghofrani.classapp.fragment.timetable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.adapter.TimetableListAdapter;
import com.ghofrani.classapp.modules.DataStore;

import java.util.Calendar;

public class Wednesday extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_wednesday, container, false);

    }

    @Override
    public void onStart() {

        super.onStart();

        ListView listView = (ListView) getView().findViewById(R.id.wednesday_list_view);
        TextView noClasses = (TextView) getView().findViewById(R.id.wednesday_no_classes);

        if (DataStore.getClassesArray(Calendar.WEDNESDAY) != null) {

            noClasses.setVisibility(View.GONE);

            TimetableListAdapter listAdapter = new TimetableListAdapter(getContext(), DataStore.getClassesArray(Calendar.WEDNESDAY));
            listView.setAdapter(listAdapter);

            setListViewHeightBasedOnChildren(listView);

        } else {

            listView.setVisibility(View.GONE);

            noClasses.setVisibility(View.VISIBLE);

        }

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

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);

    }

}