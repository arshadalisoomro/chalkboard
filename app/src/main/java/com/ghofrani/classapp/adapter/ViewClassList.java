package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.model.DatedStandardClass;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.module.DataSingleton;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class ViewClassList extends BaseExpandableListAdapter {

    private final ArrayList<DatedStandardClass> datedStandardClassArrayList;
    private final String groupTitle;
    private final LayoutInflater layoutInflater;
    private final DateTimeFormatter dayOfWeekString;
    private final DateTimeFormatter shortDate;

    public ViewClassList(Context context, ArrayList<DatedStandardClass> datedStandardClassArrayList, String groupTitle) {

        this.datedStandardClassArrayList = datedStandardClassArrayList;
        this.groupTitle = groupTitle;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        dayOfWeekString = DateTimeFormat.forPattern("EEEE");
        shortDate = DateTimeFormat.forPattern("dd/MM/yy");

    }

    public void updateLinkedList(ArrayList<DatedStandardClass> update) {

        datedStandardClassArrayList.clear();
        datedStandardClassArrayList.addAll(update);

        notifyDataSetChanged();

    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return datedStandardClassArrayList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return datedStandardClassArrayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return datedStandardClassArrayList.get(childPosition);
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
            convertView = layoutInflater.inflate(R.layout.view_class_list_child, parent, false);

        final StandardClass standardClass = datedStandardClassArrayList.get(childPosition).getStandardClass();
        final DateTime standardClassDateTime = datedStandardClassArrayList.get(childPosition).getDateTime();

        final RelativeLayout listChildRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.view_class_list_child_relative_layout);
        final TextView listChildTitleTextView = (TextView) convertView.findViewById(R.id.view_class_list_child_text);
        final TextView listChildTeacherLocationTextView = (TextView) convertView.findViewById(R.id.view_class_list_child_teacher_location);
        final TextView listChildTitleTextViewCentered = (TextView) convertView.findViewById(R.id.view_class_list_child_text_centered);

        if (standardClass.hasLocation()) {

            listChildTitleTextViewCentered.setVisibility(View.GONE);

            listChildRelativeLayout.setVisibility(View.VISIBLE);

            listChildTitleTextView.setVisibility(View.VISIBLE);

            if (new DateTime().withTimeAtStartOfDay().isEqual(standardClassDateTime.withTimeAtStartOfDay())) {

                listChildTitleTextView.setText("Today");

            } else if (new DateTime().plusDays(1).withTimeAtStartOfDay().isEqual(standardClassDateTime.withTimeAtStartOfDay())) {

                listChildTitleTextView.setText("Tomorrow");

            } else if (standardClassDateTime.isBefore(DataSingleton.getInstance().getThisWeekEnd())) {

                listChildTitleTextView.setText(dayOfWeekString.print(standardClassDateTime));

            } else if (standardClassDateTime.isBefore(DataSingleton.getInstance().getNextWeekEnd())) {

                listChildTitleTextView.setText("Next " + dayOfWeekString.print(standardClassDateTime));

            } else {

                listChildTitleTextView.setText(shortDate.print(standardClassDateTime));

            }

            listChildTeacherLocationTextView.setVisibility(View.VISIBLE);

            if (standardClass.hasTeacher())
                listChildTeacherLocationTextView.setText(standardClass.getTeacher() + " • " + standardClass.getLocation());
            else
                listChildTeacherLocationTextView.setText(standardClass.getLocation());

        } else if (standardClass.hasTeacher()) {

            listChildTitleTextViewCentered.setVisibility(View.GONE);

            listChildRelativeLayout.setVisibility(View.VISIBLE);

            listChildTitleTextView.setVisibility(View.VISIBLE);

            if (new DateTime().withTimeAtStartOfDay().isEqual(standardClassDateTime.withTimeAtStartOfDay())) {

                listChildTitleTextView.setText("Today");

            } else if (new DateTime().plusDays(1).withTimeAtStartOfDay().isEqual(standardClassDateTime.withTimeAtStartOfDay())) {

                listChildTitleTextView.setText("Tomorrow");

            } else if (standardClassDateTime.isBefore(DataSingleton.getInstance().getThisWeekEnd())) {

                listChildTitleTextView.setText(dayOfWeekString.print(standardClassDateTime));

            } else if (standardClassDateTime.isBefore(DataSingleton.getInstance().getNextWeekEnd())) {

                listChildTitleTextView.setText("Next " + dayOfWeekString.print(standardClassDateTime));

            } else {

                listChildTitleTextView.setText(shortDate.print(standardClassDateTime));

            }

            listChildTeacherLocationTextView.setVisibility(View.VISIBLE);
            listChildTeacherLocationTextView.setText(standardClass.getTeacher());

        } else {

            listChildRelativeLayout.setVisibility(View.GONE);

            listChildTitleTextView.setVisibility(View.GONE);
            listChildTeacherLocationTextView.setVisibility(View.GONE);

            listChildTitleTextViewCentered.setVisibility(View.VISIBLE);

            if (new DateTime().withTimeAtStartOfDay().isEqual(standardClassDateTime.withTimeAtStartOfDay())) {

                listChildTitleTextViewCentered.setText("Today");

            } else if (new DateTime().plusDays(1).withTimeAtStartOfDay().isEqual(standardClassDateTime.withTimeAtStartOfDay())) {

                listChildTitleTextViewCentered.setText("Tomorrow");

            } else if (standardClassDateTime.isBefore(DataSingleton.getInstance().getThisWeekEnd())) {

                listChildTitleTextViewCentered.setText(dayOfWeekString.print(standardClassDateTime));

            } else if (standardClassDateTime.isBefore(DataSingleton.getInstance().getNextWeekEnd())) {

                listChildTitleTextViewCentered.setText("Next " + dayOfWeekString.print(standardClassDateTime));

            } else {

                listChildTitleTextViewCentered.setText(shortDate.print(standardClassDateTime));

            }

        }

        final TextView listChildTimeTextView = (TextView) convertView.findViewById(R.id.view_class_list_child_time);
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