package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.model.StandardClass;

import java.util.LinkedList;

public class EditDayList extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private LinkedList<StandardClass> classesLinkedList;

    public EditDayList(Context context, LinkedList<StandardClass> initial) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        classesLinkedList = initial;

    }

    @Override
    public int getCount() {
        return classesLinkedList.size();
    }

    @Override
    public Object getItem(int position) {
        return classesLinkedList.get(position);
    }

    public void setClassesLinkedList(LinkedList<StandardClass> update) {

        classesLinkedList.clear();
        classesLinkedList.addAll(update);

        notifyDataSetChanged();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (classesLinkedList.get(position) == null) {

            if (convertView == null)
                convertView = layoutInflater.inflate(R.layout.view_edit_day_list_child, null);

            ImageView listChildIconImageView = (ImageView) convertView.findViewById(R.id.view_edit_day_list_child_icon);
            listChildIconImageView.setVisibility(View.VISIBLE);

            TextView listChildTitleTextView = (TextView) convertView.findViewById(R.id.view_edit_day_list_child_text);
            listChildTitleTextView.setVisibility(View.INVISIBLE);

            TextView listChildTimeTextView = (TextView) convertView.findViewById(R.id.view_edit_day_list_child_time);
            listChildTimeTextView.setVisibility(View.INVISIBLE);

            TextView listChildLocationTextView = (TextView) convertView.findViewById(R.id.view_edit_day_list_child_location);
            listChildLocationTextView.setVisibility(View.INVISIBLE);

            return convertView;

        } else {

            if (convertView == null)
                convertView = layoutInflater.inflate(R.layout.view_edit_day_list_child, null);

            ImageView listChildIconImageView = (ImageView) convertView.findViewById(R.id.view_edit_day_list_child_icon);
            listChildIconImageView.setVisibility(View.INVISIBLE);

            StandardClass standardClass = classesLinkedList.get(position);

            TextView listChildTitleTextView = (TextView) convertView.findViewById(R.id.view_edit_day_list_child_text);
            listChildTitleTextView.setVisibility(View.VISIBLE);
            listChildTitleTextView.setText(standardClass.getName());

            TextView listChildTimeTextView = (TextView) convertView.findViewById(R.id.view_edit_day_list_child_time);
            listChildTimeTextView.setVisibility(View.VISIBLE);
            listChildTimeTextView.setText(standardClass.getStartTimeString() + " - " + standardClass.getEndTimeString());

            TextView listChildLocationTextView = (TextView) convertView.findViewById(R.id.view_edit_day_list_child_location);
            listChildLocationTextView.setVisibility(View.VISIBLE);
            listChildLocationTextView.setText(standardClass.getLocation());

            return convertView;

        }

    }

}