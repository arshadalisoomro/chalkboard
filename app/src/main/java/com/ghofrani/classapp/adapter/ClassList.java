package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.model.StandardClass;

import java.util.ArrayList;

public class ClassList extends BaseExpandableListAdapter {

    private final ArrayList<StandardClass> classesArrayList;
    private final String groupTitle;
    private final LayoutInflater layoutInflater;

    public ClassList(Context context, ArrayList<StandardClass> classesArrayList, String groupTitle) {

        this.classesArrayList = classesArrayList;
        this.groupTitle = groupTitle;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void updateLinkedList(ArrayList<StandardClass> update) {

        classesArrayList.clear();
        classesArrayList.addAll(update);

        notifyDataSetChanged();

    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return classesArrayList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return classesArrayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return classesArrayList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.view_list_group, parent, false);

        final TextView listGroupTitleTextView = (TextView) convertView.findViewById(R.id.view_list_group_text);
        listGroupTitleTextView.setText(groupTitle);

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.view_list_child, parent, false);

        final StandardClass standardClass = classesArrayList.get(childPosition);

        final RelativeLayout listChildRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.view_list_child_relative_layout);
        final TextView listChildTitleTextView = (TextView) convertView.findViewById(R.id.view_list_child_text);
        final TextView listChildTeacherLocationTextView = (TextView) convertView.findViewById(R.id.view_list_child_teacher_location);
        final TextView listChildTitleTextViewCentered = (TextView) convertView.findViewById(R.id.view_list_child_text_centered);

        if (standardClass.hasLocation()) {

            listChildTitleTextViewCentered.setVisibility(View.GONE);

            listChildRelativeLayout.setVisibility(View.VISIBLE);

            listChildTitleTextView.setVisibility(View.VISIBLE);
            listChildTitleTextView.setText(standardClass.getName());

            listChildTeacherLocationTextView.setVisibility(View.VISIBLE);

            if (standardClass.hasTeacher())
                listChildTeacherLocationTextView.setText(standardClass.getTeacher() + " • " + standardClass.getLocation());
            else
                listChildTeacherLocationTextView.setText(standardClass.getLocation());

        } else if (standardClass.hasTeacher()) {

            listChildTitleTextViewCentered.setVisibility(View.GONE);

            listChildRelativeLayout.setVisibility(View.VISIBLE);

            listChildTitleTextView.setVisibility(View.VISIBLE);
            listChildTitleTextView.setText(standardClass.getName());

            listChildTeacherLocationTextView.setVisibility(View.VISIBLE);
            listChildTeacherLocationTextView.setText(standardClass.getTeacher());

        } else {

            listChildRelativeLayout.setVisibility(View.GONE);

            listChildTitleTextView.setVisibility(View.GONE);
            listChildTeacherLocationTextView.setVisibility(View.GONE);

            listChildTitleTextViewCentered.setVisibility(View.VISIBLE);
            listChildTitleTextViewCentered.setText(standardClass.getName());

        }

        final TextView listChildTimeTextView = (TextView) convertView.findViewById(R.id.view_list_child_time);
        listChildTimeTextView.setText(standardClass.getStartTimeString(true) + " - " + standardClass.getEndTimeString(true));

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

}