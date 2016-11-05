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

public class EditDayList extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private final ArrayList<StandardClass> classesArrayList;

    public EditDayList(Context context, ArrayList<StandardClass> classesArrayList) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.classesArrayList = classesArrayList;

    }

    @Override
    public int getCount() {
        return classesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return classesArrayList.get(position);
    }

    public void setClassesArrayList(ArrayList<StandardClass> update) {

        classesArrayList.clear();
        classesArrayList.addAll(update);

        notifyDataSetChanged();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.view_edit_day_list_child, null);

        final ImageView listChildIconImageView = (ImageView) convertView.findViewById(R.id.view_edit_day_list_child_icon);
        final RelativeLayout listChildRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.view_edit_day_list_child_relative_layout);
        final TextView listChildTitleTextView = (TextView) convertView.findViewById(R.id.view_edit_day_list_child_text);
        final TextView listChildTitleTextViewCentered = (TextView) convertView.findViewById(R.id.view_edit_day_list_child_text_centered);
        final TextView listChildTimeTextView = (TextView) convertView.findViewById(R.id.view_edit_day_list_child_time);
        final TextView listChildLocationTeacherTextView = (TextView) convertView.findViewById(R.id.view_edit_day_list_child_location_teacher);
        final ImageView listChildColorIndicator = (ImageView) convertView.findViewById(R.id.view_edit_day_list_child_color_indicator);

        if (classesArrayList.get(position) == null) {

            listChildIconImageView.setVisibility(View.VISIBLE);

            listChildRelativeLayout.setVisibility(View.GONE);
            listChildTitleTextView.setVisibility(View.GONE);
            listChildTitleTextViewCentered.setVisibility(View.GONE);
            listChildTimeTextView.setVisibility(View.GONE);
            listChildLocationTeacherTextView.setVisibility(View.GONE);
            listChildColorIndicator.setVisibility(View.GONE);

            return convertView;

        } else {

            listChildIconImageView.setVisibility(View.GONE);

            if (classesArrayList.get(position).hasLocation()) {

                listChildTitleTextViewCentered.setVisibility(View.GONE);

                listChildRelativeLayout.setVisibility(View.VISIBLE);

                listChildTitleTextView.setVisibility(View.VISIBLE);
                listChildTitleTextView.setText(classesArrayList.get(position).getName());

                listChildLocationTeacherTextView.setVisibility(View.VISIBLE);

                if (classesArrayList.get(position).hasTeacher())
                    listChildLocationTeacherTextView.setText(classesArrayList.get(position).getLocation() + " • " + classesArrayList.get(position).getTeacher());
                else
                    listChildLocationTeacherTextView.setText(classesArrayList.get(position).getLocation());

            } else if (classesArrayList.get(position).hasTeacher()) {

                listChildTitleTextViewCentered.setVisibility(View.GONE);

                listChildRelativeLayout.setVisibility(View.VISIBLE);

                listChildTitleTextView.setVisibility(View.VISIBLE);
                listChildTitleTextView.setText(classesArrayList.get(position).getName());

                listChildLocationTeacherTextView.setVisibility(View.VISIBLE);
                listChildLocationTeacherTextView.setText(classesArrayList.get(position).getTeacher());

            } else {

                listChildRelativeLayout.setVisibility(View.GONE);

                listChildTitleTextView.setVisibility(View.GONE);
                listChildLocationTeacherTextView.setVisibility(View.GONE);

                listChildTitleTextViewCentered.setVisibility(View.VISIBLE);
                listChildTitleTextViewCentered.setText(classesArrayList.get(position).getName());

            }

            listChildTimeTextView.setVisibility(View.VISIBLE);
            listChildTimeTextView.setText(classesArrayList.get(position).getStartTimeString(true) + "\n" + classesArrayList.get(position).getEndTimeString(true));

            listChildColorIndicator.setVisibility(View.VISIBLE);
            listChildColorIndicator.setColorFilter(classesArrayList.get(position).getColor());

            return convertView;

        }

    }

}