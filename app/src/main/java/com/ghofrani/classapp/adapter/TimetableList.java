package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.model.StandardClass;

import java.util.ArrayList;

public class TimetableList extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private final ArrayList<StandardClass> classesArrayList;

    public TimetableList(Context context, ArrayList<StandardClass> classesArrayList) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.classesArrayList = classesArrayList;

    }

    public void setClassesArrayList(ArrayList<StandardClass> update) {

        classesArrayList.clear();
        classesArrayList.addAll(update);

        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return classesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return classesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.view_list_child, null);

        final StandardClass standardClass = classesArrayList.get(position);

        final TextView listChildTitleTextView = (TextView) convertView.findViewById(R.id.view_list_child_text);
        listChildTitleTextView.setText(standardClass.getName());

        final TextView listChildTimeTextView = (TextView) convertView.findViewById(R.id.view_list_child_time);
        listChildTimeTextView.setText(standardClass.getStartTimeString(true) + " - " + standardClass.getEndTimeString(true));

        final TextView listChildLocationTextView = (TextView) convertView.findViewById(R.id.view_list_child_location);
        listChildLocationTextView.setText(standardClass.getLocation());

        return convertView;

    }

}