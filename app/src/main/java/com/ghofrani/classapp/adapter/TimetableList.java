package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

        final RelativeLayout listChildRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.view_list_child_relative_layout);
        final TextView listChildTitleTextView = (TextView) convertView.findViewById(R.id.view_list_child_text);
        final TextView listChildLocationTeacherTextView = (TextView) convertView.findViewById(R.id.view_list_child_location_teacher);
        final TextView listChildTitleTextViewCentered = (TextView) convertView.findViewById(R.id.view_list_child_text_centered);

        if (standardClass.hasLocation()) {

            listChildTitleTextViewCentered.setVisibility(View.GONE);

            listChildRelativeLayout.setVisibility(View.VISIBLE);

            listChildTitleTextView.setVisibility(View.VISIBLE);
            listChildTitleTextView.setText(standardClass.getName());

            listChildLocationTeacherTextView.setVisibility(View.VISIBLE);

            if (standardClass.hasTeacher())
                listChildLocationTeacherTextView.setText(standardClass.getLocation() + " • " + standardClass.getTeacher());
            else
                listChildLocationTeacherTextView.setText(standardClass.getLocation());

        } else if (standardClass.hasTeacher()) {

            listChildTitleTextViewCentered.setVisibility(View.GONE);

            listChildRelativeLayout.setVisibility(View.VISIBLE);

            listChildTitleTextView.setVisibility(View.VISIBLE);
            listChildTitleTextView.setText(standardClass.getName());

            listChildLocationTeacherTextView.setVisibility(View.VISIBLE);
            listChildLocationTeacherTextView.setText(standardClass.getTeacher());

        } else {

            listChildRelativeLayout.setVisibility(View.GONE);

            listChildTitleTextView.setVisibility(View.GONE);
            listChildLocationTeacherTextView.setVisibility(View.GONE);

            listChildTitleTextViewCentered.setVisibility(View.VISIBLE);
            listChildTitleTextViewCentered.setText(standardClass.getName());

        }

        final TextView listChildTimeTextView = (TextView) convertView.findViewById(R.id.view_list_child_time);
        listChildTimeTextView.setText(standardClass.getStartTimeString(true) + "\n" + standardClass.getEndTimeString(true));

        final ImageView listChildColorIndicator = (ImageView) convertView.findViewById(R.id.view_list_child_color_indicator);
        listChildColorIndicator.setColorFilter(standardClass.getColor());

        return convertView;

    }

}